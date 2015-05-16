package com.billingserver.connection;
import com.billingserver.BillingServer;
import com.billingserver.calls.Call;
import com.billingserver.calls.PostPayedCall;
import com.billingserver.calls.PrePayedCall;
import com.billingserver.data.clients.Client;
import com.billingserver.data.clients.PostPayedClient;
import com.billingserver.data.clients.PrePayedClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.regex.Pattern;
import static com.billingserver.connection.ServerConnectionConstants.*;

/**
 *
 */
public class ClientHandler implements Runnable
{
    private SenderReceiverTCP handler;
    private BillingServer server;
    private String phoneNumber;
    private boolean enabled = false;

    public ClientHandler(SenderReceiverTCP handler, BillingServer server)
    {
        this.handler = handler;
        this.server = server;
    }

    private boolean validate() throws IOException
    {
        String mess = handler.receive();
        String[] data = mess.split( Pattern.quote(SEPARATOR) );

        if ( data.length == 2 && data[0].equals(CONNECT) )
        {
            if (server.getClients().getClient(data[1]) != null)
            {
                phoneNumber = data[1];
                return true;
            }
        }

        return false;
    }

    @Override
    public void run()
    {
        try
        {
            if (validate())
            {
                //enabled = true;
                sendMessage(CONNECT);
                server.getClientHandlerMap().put(phoneNumber, this);        // not thread-safe
            }
            else
                sendMessage( INVALID_PHONE_NUMBER );
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        listen();
    }

    private void listen()
    {
        String message = null;

        do
        {
            try
            {
                message = handler.receive();
                processMessage(message);
            }
            catch (IOException e)
            {
                displayMessage("An error occurred while reading information from stream");
                server.getClientHandlerMap().remove(phoneNumber);
                try
                {
                    handler.close();
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
                return;
            }

        } while (message == null || !message.equals(DISCONNECT));
    }

    private void processMessage(String message)
    {
        String[] data = message.split(Pattern.quote(SEPARATOR));
        displayMessage(data[0] + "\n");
        switch (data[0])
        {
            case CALL:
                call(data[1]);
                break;
            case ANSWERED:
                Client caller = server.getClients().getClient(data[1]);
                ClientHandler callerHandler = server.getClientHandlerMap().get(data[1]);
                Call call = (caller instanceof PrePayedClient) ? new PrePayedCall(callerHandler, this) : new PostPayedCall(callerHandler, this);
                server.getActiveCalls().add(call);
                callerHandler.sendMessage(ANSWERED);
                call.start();
                break;
            case STOP:
                Call c = server.getCall( phoneNumber );
                c.stop();
                server.getActiveCalls().remove(c);

                BigDecimal amount = BigDecimal.ZERO;
                if ( c instanceof PostPayedCall)
                    amount = ((PostPayedClient) server.getClients().getClient(c.getCaller().phoneNumber)).getDebt();
                else if ( c instanceof PrePayedCall )
                        amount = ((PrePayedClient) server.getClients().getClient(c.getCaller().phoneNumber)).getAmount();

                c.getCaller().sendBalance(amount);
                ClientHandler other = server.getClientHandlerMap().get( data[1] );
                other.sendMessage(STOP);
                break;
            case DISCONNECT:
                server.getClientHandlerMap().remove(phoneNumber);
                break;
            case BALANCE:
                Client cl = server.getClients().getClient(phoneNumber);
                if ( cl != null)
                {
                    if ( cl instanceof PrePayedClient )
                        sendBalance( ((PrePayedClient) cl).getAmount() );
                    else if ( cl instanceof PostPayedClient )
                        sendBalance(((PostPayedClient)cl).getDebt());
                }
                break;
        }
    }

    private void call( String client )
    {
        // no money
        Client sender = server.getClients().getClient(phoneNumber);
        if ( sender instanceof PrePayedClient && ((PrePayedClient) sender).getAmount().equals(BigDecimal.ZERO))
        {
            sendMessage(NO_RESOURCES);
            return;
        }

        Client receiver = server.getClients().getClient(client);
        if ( receiver != null )
        {
            ClientHandler receiverHandler = server.getClientHandlerMap().get(client);
            if (receiverHandler != null  )
            {
                if ( !server.isBusy( client ) )
                {
                    handler.send(WAIT_RESPONSE + SEPARATOR + client);
                    server.sendMessageToClient( client, CALL + SEPARATOR + phoneNumber );
                }
                else
                    sendMessage( BUSY + SEPARATOR + client );
            }
            else
                handler.send( OFFLINE );
        }
        else
            handler.send(INVALID_PHONE_NUMBER_CALL);
    }

    private void displayMessage( String message )
    {
        System.out.print(message);
    }

    public void sendMessage(String message)
    {
        handler.send(message);
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    private void sendBalance( BigDecimal balance )
    {
        sendMessage( BALANCE + SEPARATOR + balance );
    }
}

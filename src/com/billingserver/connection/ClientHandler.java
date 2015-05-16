package com.billingserver.connection;
import com.billingserver.BillingServer;

import java.io.IOException;
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
            phoneNumber = data[1];
            return true;
        }

        return false;
    }

    @Override
    public void run()
    {
        try
        {
            if (validate())
                enabled = true;
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
            }

        } while (message == null || !message.equals(DISCONNECT));
    }

    private void processMessage(String message)
    {
        String[] data = message.split( Pattern.quote(SEPARATOR) );

        switch (data[0])
        {

        }
    }

    private void displayMessage( String message )
    {
        System.out.print(message);
    }
}

package com.billingserver;

import com.billingserver.calls.Call;
import com.billingserver.connection.ClientHandler;
import com.billingserver.connection.SenderReceiverTCP;
import com.billingserver.data.ClientsManager;
import com.billingserver.data.clients.Repository;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.billingserver.connection.ServerConnectionConstants.*;

/**
 * Main Server Class
 */
public class BillingServer
{
    private ClientsManager clients;
    private ServerSocket serverSocket;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private Map< String, ClientHandler > clientHandlerMap;
    private LinkedList<Call> activeCalls;

    public void init() throws IOException
    {
        serverSocket = new ServerSocket(SERVER_PORT);
        clients = Repository.getInstance().getClientsManager();
        clientHandlerMap = new TreeMap<>();
        activeCalls = new LinkedList<>();
    }

    public void runServer()
    {
        while (true)
        {
            try
            {
                Socket clientSocket = serverSocket.accept();
                SenderReceiverTCP handler = new SenderReceiverTCP(clientSocket);
                executorService.execute(new ClientHandler(handler, this));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public Map<String, ClientHandler> getClientHandlerMap()
    {
        return clientHandlerMap;
    }

    public ClientsManager getClients()
    {
        return clients;
    }

    public void sendMessageToClient( String phoneNumber, String message )
    {
        clientHandlerMap.get(phoneNumber).sendMessage(message);
    }

    public boolean isBusy( String phoneNumber )
    {
        for ( Call c : activeCalls )
        {
            if ( c.getCaller().getPhoneNumber().equals(phoneNumber) || c.getReceiver().getPhoneNumber().equals(phoneNumber))
                return true;
        }

        return false;
    }

    public LinkedList<Call> getActiveCalls()
    {
        return activeCalls;
    }

    public Call getCall( String number )
    {
        for (Call call : activeCalls)
        {
            if ( call.getCaller().getPhoneNumber().equals(number) || call.getReceiver().getPhoneNumber().equals(number))
                return call;
        }

        return null;
    }
}

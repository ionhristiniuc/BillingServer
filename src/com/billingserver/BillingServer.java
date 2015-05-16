package com.billingserver;

import com.billingserver.connection.ClientHandler;
import com.billingserver.connection.SenderReceiverTCP;
import com.billingserver.data.ClientsManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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

    public void init() throws IOException
    {
        serverSocket = new ServerSocket(SERVER_PORT);
        clients = new ClientsManager();
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
}

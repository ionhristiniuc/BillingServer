package com.billingserver.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class SenderReceiverTCP implements Sender, Receiver, AutoCloseable
{
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public SenderReceiverTCP(Socket clientSocket) throws SocketException
    {
        socket = clientSocket;

        try
        {
            getStreams();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void getStreams() throws IOException
    {
        output = new ObjectOutputStream(socket.getOutputStream());
        output.flush();
        input = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public String receive() throws IOException
    {
        try
        {
            return (String) input.readObject();
        }
        catch (ClassNotFoundException e)
        {
            return null;
        }
    }

    @Override
    public void send(String message)
    {
        try
        {
            output.writeObject( message );
            output.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception
    {
        output.close();
        input.close();
        socket.close();
    }
}

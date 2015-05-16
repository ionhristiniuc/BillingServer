package com.billingserver.data;

import com.billingserver.data.clients.Client;
import com.billingserver.data.clients.PostPayedClient;
import com.billingserver.data.clients.PrePayedClient;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * Keeps all data about clients
 */
public class ClientsManager
{
    private LinkedList<Client> clients;

    public ClientsManager()
    {
        clients = new LinkedList<>();
        clients.add( new PostPayedClient( "060971255" ));
        clients.add( new PrePayedClient( "068321605", BigDecimal.valueOf(100.00)));
        clients.add( new PrePayedClient( "069526844", BigDecimal.valueOf(50.00)));
    }

    public Client getClient(String phoneNumber)
    {
        for (Client c : clients)
            if (c.getPhoneNumber().equals( phoneNumber ))
                return c;

        return null;
    }
}

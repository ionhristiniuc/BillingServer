package com.billingserver.data.clients;

import com.billingserver.data.ClientsManager;

/**
 * DB
 */
public class Repository
{
    private static Repository ourInstance = new Repository();
    private ClientsManager clientsManager = new ClientsManager();

    public static Repository getInstance()
    {
        return ourInstance;
    }
    public ClientsManager getClientsManager()
    {
        return clientsManager;
    }

    private Repository()
    {
    }
}

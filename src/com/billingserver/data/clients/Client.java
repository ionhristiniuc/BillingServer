package com.billingserver.data.clients;

/**
 * Abstract client
 */
public abstract class Client
{
    private String phoneNumber;

    public Client(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }
}

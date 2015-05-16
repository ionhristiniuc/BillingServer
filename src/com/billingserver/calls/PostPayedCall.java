package com.billingserver.calls;

import com.billingserver.connection.ClientHandler;
import com.billingserver.data.clients.Client;
import com.billingserver.data.clients.PostPayedClient;
import com.billingserver.data.clients.Repository;

import java.math.BigDecimal;

/**
 * Post Payed
 */
public class PostPayedCall extends Call
{
    public PostPayedCall(ClientHandler caller, ClientHandler receiver)
    {
        super(caller, receiver);
    }

    @Override
    public void stop()
    {
        super.stop();
        long sec = getDuration();
        PostPayedClient c = (PostPayedClient) Repository.getInstance().getClientsManager().getClient(getCaller().getPhoneNumber());
        c.setDebt( c.getDebt().add(standardCharge.multiply( (BigDecimal.valueOf(sec) ) ) ) );
    }
}

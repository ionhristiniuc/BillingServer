package com.billingserver.calls;

import com.billingserver.connection.ClientHandler;
import com.billingserver.data.clients.PostPayedClient;
import com.billingserver.data.clients.Repository;

import java.math.BigDecimal;

/**
 * Post Payed
 */
public class PostPayedCall extends Call
{
    public PostPayedCall(ClientHandler caller, ClientHandler receiver, CommunicationType communicationType, CallType callType)
    {
        super(caller, receiver, communicationType, callType);
    }

    @Override
    public void stop()
    {
        super.stop();
        long sec = getDuration() - getPauseDuration();
        PostPayedClient c = (PostPayedClient) Repository.getInstance().getClientsManager().getClient(getCaller().getPhoneNumber());
        c.setDebt( c.getDebt().add(getCharge().multiply((BigDecimal.valueOf(sec))) ) );

//        PostPayedClient caller = (PostPayedClient) Repository.getInstance().getClientsManager().getClient(getCaller().getPhoneNumber());
//        writeRecord(caller, (int) getDuration(), getReceiver().getPhoneNumber(), CallType.Voice, CommunicationType.Standard, caller.getDebt() );
    }
}

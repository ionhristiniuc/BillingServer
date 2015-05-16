package com.billingserver.calls;

import com.billingserver.connection.ClientHandler;
import com.billingserver.data.clients.PrePayedClient;
import com.billingserver.data.clients.Repository;
import org.omg.CORBA.NO_RESOURCES;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;
import static com.billingserver.connection.ServerConnectionConstants.*;

/**
 * Pre payed
 */
public class PrePayedCall extends Call
{
    private Timer timer = new Timer();

    public PrePayedCall(ClientHandler caller, ClientHandler receiver)
    {
        super(caller, receiver);
    }

    @Override
    public void start()
    {
        super.start();

        PrePayedClient client = (PrePayedClient) Repository.getInstance().getClientsManager().getClient(getCaller().getPhoneNumber());
        long availableSeconds = (long) client.getAmount().divide( standardCharge ).longValue();

        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                getCaller().sendMessage(NO_RESOURCES);
                getReceiver().sendMessage(STOP);
                stop();
            }
        }, availableSeconds * 1000);
    }

    @Override
    public void stop()
    {
        super.stop();
        long sec = getDuration();
        PrePayedClient c = (PrePayedClient) Repository.getInstance().getClientsManager().getClient(getCaller().getPhoneNumber());
        c.setAmount(c.getAmount().subtract(standardCharge.multiply((BigDecimal.valueOf(sec)))));
        timer.cancel();
    }
}

package com.billingserver.calls;

import com.billingserver.connection.ClientHandler;
import com.billingserver.data.clients.PrePayedClient;
import com.billingserver.data.clients.Repository;

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

    public PrePayedCall(ClientHandler caller, ClientHandler receiver, CommunicationType communicationType, CallType callType)
    {
        super(caller, receiver, communicationType, callType);
    }

    @Override
    public void start()
    {
        super.start();

        PrePayedClient client = (PrePayedClient) Repository.getInstance().getClientsManager().getClient(getCaller().getPhoneNumber());
        long availableSeconds = (long) client.getAmount().divide( getCharge() ).longValue();

        timer.schedule(new MyTimerTask(availableSeconds), 0, 1000 );
    }

    class MyTimerTask extends TimerTask
    {
        private long value;

        public MyTimerTask(long value)
        {
            this.value = value;
        }

        @Override
        public void run()
        {
            if (value == 0)
            {
                getCaller().sendMessage(NO_RESOURCES + SEPARATOR + (getDuration() - getPauseDuration()));
                getReceiver().sendMessage(STOP + SEPARATOR + (getDuration() - getPauseDuration()));
                stop();
            }
            if (!isPaused())
                --value;
        }
    }

    @Override
    public void stop()
    {
        super.stop();
        long sec = getDuration() - getPauseDuration();
        PrePayedClient c = (PrePayedClient) Repository.getInstance().getClientsManager().getClient(getCaller().getPhoneNumber());
        c.setAmount(c.getAmount().subtract(getCharge().multiply((BigDecimal.valueOf(sec)))));
        timer.cancel();
    }

}

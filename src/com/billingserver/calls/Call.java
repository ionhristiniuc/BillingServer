package com.billingserver.calls;

import com.billingserver.connection.ClientHandler;
import com.billingserver.data.clients.Client;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

/**
 * An abstract call
 */
public abstract class Call
{
    private ClientHandler caller;
    private ClientHandler receiver;
    private Instant callStart;
    private Instant callStop;
    private long duration;
    protected static BigDecimal standardCharge = CallConstants.STD_CHARGE;
    protected static BigDecimal roamingCharge = CallConstants.ROAMING_CHARGE;

    public Call(ClientHandler caller, ClientHandler receiver)
    {
        this.caller = caller;
        this.receiver = receiver;
    }

    public ClientHandler getCaller()
    {
        return caller;
    }

    public ClientHandler getReceiver()
    {
        return receiver;
    }

    public void start()
    {
        callStart = Instant.now();
    }

    public void stop()
    {
        callStop = Instant.now();
        duration = Duration.between( callStart, callStop ).getSeconds();
    }

    public long getDuration()
    {
        return duration;
    }
}

package com.billingserver.calls;

import com.billingserver.connection.ClientHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
    private BigDecimal charge;
    private CommunicationType communicationType;
    private CallType callType;
    private long pauseDuration;
    private Instant pauseStart;
    private Instant pauseStop;
    private boolean paused = false;

    public Call(ClientHandler caller, ClientHandler receiver, CommunicationType communicationType, CallType callType)
    {
        this.caller = caller;
        this.receiver = receiver;
        this.communicationType = communicationType;
        this.callType = callType;
        charge = this.communicationType == CommunicationType.Roaming ? CallConstants.ROAMING_CHARGE : CallConstants.STD_CHARGE;
        charge = this.callType == CallType.Video ? charge.multiply( BigDecimal.valueOf(2) ) : charge;
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

    public void pause()
    {
        pauseStart = Instant.now();
        paused = true;
    }

    public void reload()
    {
        pauseStop = Instant.now();
        pauseDuration += Duration.between(pauseStart, pauseStop).getSeconds();
        pauseStart = null;
        pauseStop = null;
        paused = false;
    }

    public void stop()
    {
        callStop = Instant.now();
        duration = Duration.between( callStart, callStop ).getSeconds();
    }

    protected static void writeRecord(String phone, long duration,
                                      CallType callType, CommunicationType commType, BigDecimal ammount)
    {
        try
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("Client");
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
    }

    public long getDuration()
    {
        return duration;
    }

    public BigDecimal getCharge()
    {
        return charge;
    }

    public boolean isPaused()
    {
        return paused;
    }

    public long getPauseDuration()
    {
        return pauseDuration;
    }
}

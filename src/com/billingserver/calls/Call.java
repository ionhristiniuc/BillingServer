package com.billingserver.calls;

import com.billingserver.connection.ClientHandler;
import com.billingserver.data.clients.Client;
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
}

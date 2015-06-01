package com.billingserver.calls;

import com.billingserver.connection.ClientHandler;
import com.billingserver.data.clients.Client;
import com.billingserver.data.clients.PostPayedClient;
import com.billingserver.data.clients.PrePayedClient;
import jdk.internal.org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    protected static void writeRecord(Client client, int duration, String receiver,
                                      CallType callType, CommunicationType commType, BigDecimal ammount)
    {
        try
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            String fileName = "Clients//" + client.getPhoneNumber() + ".xml";
            Document doc = null;
            Element rootElement = null;

            File f = new File(fileName);

            if (!Files.exists(Paths.get("Clients"))) {
                new File("Clients").mkdir();
            }

            if(!(f.exists() && !f.isDirectory()))
            {
//                doc = docBuilder.newDocument();
//                rootElement = doc.createElement("Client");
//                rootElement.setAttribute("Phone", client.getPhoneNumber());
//
//                if ( client instanceof PrePayedClient)
//                    rootElement.setAttribute("AccountType", "Pre");
//                else if ( client instanceof PostPayedClient)
//                    rootElement.setAttribute("AccountType", "Post");
//
//                doc.appendChild(rootElement);

            }
            else
            {
                doc = docBuilder.parse(fileName);
                rootElement = doc.getDocumentElement();
            }

            Element history = doc.createElement("History");

            history.setAttribute("Duration", Integer.toString(duration));
            history.setAttribute("CallType", callType.toString());
            history.setAttribute("CommunicationType", commType.toString());
            history.setAttribute("Amount", ammount.toString());
            history.setAttribute("Receiver", receiver);
            rootElement.appendChild(history);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(new File(fileName));
            transformer.transform(source, result);

        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        catch (org.xml.sax.SAXException e)
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

package com.billingserver.data;

import com.billingserver.data.clients.Client;
import com.billingserver.data.clients.PostPayedClient;
import com.billingserver.data.clients.PrePayedClient;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;

/**
 * Keeps all data about clients
 */
public class ClientsManager
{
    private LinkedList<Client> clients;

    public ClientsManager()
    {
        clients = new LinkedList<>();
        clients.add( new PostPayedClient( "060971255", new BigDecimal(0)));
        clients.add( new PrePayedClient( "068321605", BigDecimal.valueOf(100.00)));
        clients.add( new PrePayedClient( "069526844", BigDecimal.valueOf(50.00)));
        clients.add( new PrePayedClient( "069706448", BigDecimal.valueOf(02.00)));
    }

    public Client getClient(String phoneNumber)
    {
        for (Client c : clients)
            if (c.getPhoneNumber().equals( phoneNumber ))
                return c;

        return null;
    }

//    public Client getClient(String phone)
//    {
//        try {
//            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//
//
//            String fileName = "src/Clients/" + phone + ".xml";
//            org.w3c.dom.Document doc = null;
//            org.w3c.dom.Element rootElement = null;
//
//            File f = new File(fileName);
//            if (!(f.exists()))
//                return null;
//
//            doc = docBuilder.parse(fileName);
//            rootElement = doc.getDocumentElement();
//
//            String type = rootElement.getAttribute("Type");
//            BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(rootElement.getAttribute("Amount")));
//
//            if (type.equals("Pre"))
//                return new PrePayedClient(phone, amount);
//            else if (type.equals("Post"))
//                return new PostPayedClient(phone, amount);
//
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        catch (ParserConfigurationException e)
////        {
////            e.printStackTrace();
////        }
////        catch (SAXException e)
////        {
////            e.printStackTrace();
////        }
////        catch (IOException e)
////        {
////            e.printStackTrace();
////        }
//
//        return null;
//    }
}

import com.billingserver.BillingServer;

import java.io.IOException;

/**
 * Entry point
 */
public class Main
{
    public static void main(String[] args)
    {
        try
        {
            BillingServer server = new BillingServer();
            server.init();
            server.runServer();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

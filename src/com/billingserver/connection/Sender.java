package com.billingserver.connection;

import java.io.IOException;

public interface Sender
{
    void send(String message) throws IOException;
}

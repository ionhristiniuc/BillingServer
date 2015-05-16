package com.billingserver.connection;

public interface ServerConnectionConstants
{
    int SERVER_PORT = 12345;
    int MAX_PACKET_LENGTH = 100;
    String SEPARATOR = "|";
    String CONNECT = "CONN";
    String DISCONNECT = "DISCONN";

    // operations
    String CALL = "CALL";
    String ANSWERED = "ANS";
    String BALANCE = "BAL";
    String STOP = "STOP";
    // error
    String INVALID_PHONE_NUMBER = "INV_P_NR";
    String INVALID_PHONE_NUMBER_CALL = "INV_P_NR_C";
    String OFFLINE = "OFFLINE";
    String WAIT_RESPONSE = "WAIT_RESP";
    String BUSY = "BUSY";
    String NO_RESOURCES = "NO_RES";
}

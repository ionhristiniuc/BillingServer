package com.billingserver.connection;

public interface ServerConnectionConstants
{
    int SERVER_PORT = 12345;
    String SEPARATOR = "|";
    String CONNECT = "CONN";
    String DISCONNECT = "DISCONN";

    // call
    String WAIT_RESPONSE = "WAIT_RESP";
    String OFFLINE = "OFFLINE";
    String BUSY = "BUSY";

    // operations
    String CALL = "CALL";
    String ANSWERED = "ANS";
    String BALANCE = "BAL";
    String STOP = "STOP";
    String PAUSE = "PAUSE";
    String RELOAD = "RELOAD";
    String DURATION = "DUR";

    // video call
    String CALL_VIDEO = "CALL_V";
    String ANSWERED_VIDEO = "ANS_V";

    // sms
    String SMS = "SMS";
    // error
    String INVALID_PHONE_NUMBER = "INV_P_NR";
    String INVALID_PHONE_NUMBER_CALL = "INV_P_NR_C";
    String NO_RESOURCES = "NO_RES";
}

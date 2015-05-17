package com.billingserver.operators;

/**
 * Moldcell operator
 */
public class Moldcell
{
    public static boolean isClient( String number )
    {
        return number.length() == 9 && ( number.startsWith("069") || number.startsWith("068") || number.startsWith("060") || number.startsWith("061"))
                || number.length() == 12 && ( number.startsWith( "+37369") || number.startsWith( "+37368") || number.startsWith( "+37360")
                || (number.startsWith( "+37361") ) );
    }
}

package com.billingserver.data.clients;

import java.math.BigDecimal;

/**
 * Created by Ion on 16.05.2015.
 */
public class PrePayedClient extends Client
{
    private BigDecimal amount;

    public PrePayedClient(String phoneNumber, BigDecimal amount)
    {
        super(phoneNumber);
        this.amount = amount;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }
}

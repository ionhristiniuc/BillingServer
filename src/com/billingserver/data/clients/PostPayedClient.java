package com.billingserver.data.clients;

import java.math.BigDecimal;

/**
 * Post-paid client
 */
public class PostPayedClient extends Client
{
    private BigDecimal debt = BigDecimal.valueOf(0.0);

    public PostPayedClient(String phoneNumber, BigDecimal amount)
    {
        super(phoneNumber);
        debt = amount;
    }

    public BigDecimal getDebt()
    {
        return debt;
    }

    public void setDebt(BigDecimal debt)
    {
        this.debt = debt;
    }
}

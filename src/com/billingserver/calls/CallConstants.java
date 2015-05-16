package com.billingserver.calls;

import java.math.BigDecimal;

/**
 * Call Constants
 */
public interface CallConstants
{
    BigDecimal STD_CHARGE = BigDecimal.valueOf(0.01);
    BigDecimal ROAMING_CHARGE = BigDecimal.valueOf(0.03);
}

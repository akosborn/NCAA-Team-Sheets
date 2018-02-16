package me.andrewosborn.util;

import java.math.BigDecimal;

public class RoundingUtil
{
    /**
     * Round to certain number of decimals
     *
     * @param d
     * @param decimalPlace
     * @return
     */
    public static float round(float d, int decimalPlace)
    {
        BigDecimal bd = null;
        try
        {
            bd = new BigDecimal(Float.toString(d));
            bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }

        return bd.floatValue();
    }
}

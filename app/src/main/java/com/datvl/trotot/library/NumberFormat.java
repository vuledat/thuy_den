package com.datvl.trotot.library;

import java.util.Locale;

public class NumberFormat {
    public static String getFormatedNum(int amount){
        return java.text.NumberFormat.getNumberInstance(Locale.US).format(amount);
    }
}

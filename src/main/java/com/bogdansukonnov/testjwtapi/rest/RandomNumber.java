package com.bogdansukonnov.testjwtapi.rest;

import java.io.Serializable;
import java.util.Random;

public class RandomNumber implements Serializable {

    private static final long serialVersionUID = -8335943548965154778L;
    private final int number;

    public RandomNumber() {
        number = new Random().nextInt();
    }

    public int getNumber() {
        return number;
    }
}

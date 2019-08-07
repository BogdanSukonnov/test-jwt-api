package com.bogdansukonnov.testjwtapi.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RandomNumberService {

    @RequestMapping(path = "/randomNumber", method = RequestMethod.POST)
    public static RandomNumber getServerTime() {
        return new RandomNumber();
    }

}

package com.test.trade.exception;

import java.util.concurrent.ExecutorService;

public class ExpiredTradeException extends Exception {

    public ExpiredTradeException(String message) {
        super(message);
    }
}

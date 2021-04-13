package com.test.trade;


import java.time.LocalDate;

public interface DateChangeObserver {
    void handleDateChange(LocalDate newDate, LocalDate oldDate);
}

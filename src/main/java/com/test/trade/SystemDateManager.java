package com.test.trade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.ZoneId;

@Component
public class SystemDateManager{

    private volatile LocalDate currentDate;

    @Value("${date.zone}")
    private ZoneId zoneId;

    @Autowired
    private DateChangeObserver observer;

    @PostConstruct
    public void init() {
        currentDate = LocalDate.now(zoneId);
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "${date.zone}")
    public void changeDate(){
        LocalDate oldDate = currentDate;
        currentDate = LocalDate.now(zoneId);
        if(!oldDate.equals(currentDate)){
            observer.handleDateChange(currentDate, oldDate);
        }
    }
}

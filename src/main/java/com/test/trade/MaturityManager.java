package com.test.trade;

import com.test.trade.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
public class MaturityManager implements DateChangeObserver {

    @Autowired
    TradeRepository tradeRepository;

    @Transactional
    @Override
    public void handleDateChange(LocalDate newDate, LocalDate oldDate) {
        tradeRepository.updateMaturity(newDate);
    }
}

package com.test.trade.repository;

import com.test.trade.model.Trade;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TradeRepository extends CrudRepository<Trade, Integer> {

    List<Trade> findByTradeId(String tradeId);

    @Modifying
    @Query( "update Trade t set t.expired = true where t.maturityDate < :newDate ")
    void updateMaturity(@Param("newDate") LocalDate newDate);
}

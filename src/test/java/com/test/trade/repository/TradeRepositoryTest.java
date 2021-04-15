package com.test.trade.repository;

import com.test.trade.Application;
import com.test.trade.model.Book;
import com.test.trade.model.CounterParty;
import com.test.trade.model.Trade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TradeRepositoryTest {

    public static final String T_1 = "t-1";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CounterPartyRepository counterPartyRepository;

    @Autowired
    private TradeRepository tradeRepository;

    private CounterParty counterParty;
    private Book book;

    @Before
    public void init(){
        tradeRepository.deleteAll();
        counterParty = counterPartyRepository.save(new CounterParty("c-1"));
        book = bookRepository.save(new Book("b-1"));
    }

    @Test
    public void test_find_by_trade_id(){
        Trade trade = tradeRepository.save(Trade.builder().version(1).tradeId(T_1).counterParty(counterParty).
                book(book).maturityDate(LocalDate.now()).creationDate(LocalDate.now()).build());

        Trade trade1 = tradeRepository.save(Trade.builder().version(2).tradeId(T_1).counterParty(counterParty).
                book(book).maturityDate(LocalDate.now()).creationDate(LocalDate.now()).build());

        List<Trade> tradeList = tradeRepository.findByTradeId(T_1);
        assertEquals(2, tradeList.size());
    }
}

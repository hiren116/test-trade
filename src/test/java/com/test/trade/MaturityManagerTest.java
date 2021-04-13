package com.test.trade;

import com.test.trade.model.Book;
import com.test.trade.model.CounterParty;
import com.test.trade.model.Trade;
import com.test.trade.repository.BookRepository;
import com.test.trade.repository.CounterPartyRepository;
import com.test.trade.repository.TradeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MaturityManagerTest {

    public static final String T_1 = "t-1";
    public static final String T_2 = "t-2";
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CounterPartyRepository counterPartyRepository;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private MaturityManager maturityManager;

    private CounterParty counterParty;
    private Book book;
    private Trade trade;

    @Before
    public void init(){
        counterParty = counterPartyRepository.save(new CounterParty("c-1"));
        book = bookRepository.save(new Book("b-1"));
        trade = tradeRepository.save(Trade.builder().version(1).tradeId(T_1).counterParty(counterParty).
                book(book).maturityDate(LocalDate.of(2021, 4, 15 ))
                .creationDate(LocalDate.now()).build());

        tradeRepository.save(Trade.builder().version(2).tradeId(T_2).counterParty(counterParty).
                book(book).maturityDate(LocalDate.of(2021, 4, 16 ))
                .creationDate(LocalDate.now()).build());
    }

    @Test
    public void test_update_expiry_flag(){
        assertFalse(trade.isExpired());
        maturityManager.handleDateChange(LocalDate.of(2021, 4, 16 ),
                LocalDate.of(2021, 4, 15));
        List<Trade> tradeList = tradeRepository.findByTradeId(T_1);
        assertEquals(1, tradeList.size());
        assertEquals(trade.getId(), tradeList.get(0).getId());
        assertTrue(tradeList.get(0).isExpired());
    }

}

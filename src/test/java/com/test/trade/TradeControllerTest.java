package com.test.trade;

import com.test.trade.exception.ExpiredTradeException;
import com.test.trade.exception.StaleTradeException;
import com.test.trade.exception.UnknownBookException;
import com.test.trade.exception.UnknownCounterPartyException;
import com.test.trade.model.Book;
import com.test.trade.model.CounterParty;
import com.test.trade.model.Trade;
import com.test.trade.repository.BookRepository;
import com.test.trade.repository.CounterPartyRepository;
import com.test.trade.repository.TradeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TradeControllerTest {

    public static final String COUNTER_PARTY_ID = "c-1";
    public static final String B_1 = "b-1";
    public static final String T_1 = "t-1";
    private static final int v_1 = 1;
    @Mock
    SystemDateManager systemDateManager;

    @Mock
    BookRepository bookRepository;

    @Mock
    TradeRepository tradeRepository;

    @Mock
    CounterPartyRepository counterPartyRepository;

    @InjectMocks
    TradeController tradeController;

    @Test(expected = DateTimeParseException.class)
    public void test_invalid_date_format() throws ExpiredTradeException, UnknownCounterPartyException, StaleTradeException, UnknownBookException {
        TradeInput tradeInput = TradeInput.builder()
                .setBookId(B_1)
                .setTradeId(T_1)
                .setCounterPartyId(COUNTER_PARTY_ID)
                .setMaturityDate("14/03/2010")
                .setVersion(v_1)
                .build();

        tradeController.saveTrade(tradeInput);
    }

    @Test(expected = ExpiredTradeException.class)
    public void test_when_maturity_date_less_than_current_date_expect_exception() throws ExpiredTradeException, UnknownCounterPartyException, StaleTradeException, UnknownBookException {
        when(systemDateManager.getCurrentDate()).thenReturn(LocalDate.of(2010, 11, 02));
        when(counterPartyRepository.findById(eq(COUNTER_PARTY_ID))).thenReturn(Optional.empty());
        TradeInput tradeInput = TradeInput.builder()
                .setBookId(B_1)
                .setTradeId(T_1)
                .setCounterPartyId(COUNTER_PARTY_ID)
                .setMaturityDate("2010-11-01")
                .setVersion(v_1)
                .build();

        tradeController.saveTrade(tradeInput);
    }

    @Test(expected = UnknownCounterPartyException.class)
    public void test_when_counter_party_not_found_expect_exception() throws ExpiredTradeException, UnknownCounterPartyException, StaleTradeException, UnknownBookException {
        when(systemDateManager.getCurrentDate()).thenReturn(LocalDate.of(2010, 11, 02));
        when(counterPartyRepository.findById(eq(COUNTER_PARTY_ID))).thenReturn(Optional.empty());
        TradeInput tradeInput = TradeInput.builder()
                .setBookId(B_1)
                .setTradeId(T_1)
                .setCounterPartyId(COUNTER_PARTY_ID)
                .setMaturityDate("2010-12-03")
                .setVersion(v_1)
                .build();

        tradeController.saveTrade(tradeInput);
    }

    @Test(expected = UnknownBookException.class)
    public void test_when_book_party_not_found_expect_exception() throws ExpiredTradeException, UnknownCounterPartyException, StaleTradeException, UnknownBookException {
        when(systemDateManager.getCurrentDate()).thenReturn(LocalDate.of(2010, 11, 02));
        when(counterPartyRepository.findById(eq(COUNTER_PARTY_ID)))
                .thenReturn(Optional.of(new CounterParty(COUNTER_PARTY_ID)));
        when(bookRepository.findById(eq(B_1))).thenReturn(Optional.empty());
        TradeInput tradeInput = TradeInput.builder()
                .setBookId(B_1)
                .setTradeId(T_1)
                .setCounterPartyId(COUNTER_PARTY_ID)
                .setMaturityDate("2010-12-03")
                .setVersion(v_1)
                .build();

        tradeController.saveTrade(tradeInput);
    }

    @Test
    public void test_when_book_and_party_found_and_no_existing_trade_persist_trade() throws ExpiredTradeException, UnknownCounterPartyException, StaleTradeException, UnknownBookException {
        when(systemDateManager.getCurrentDate()).thenReturn(LocalDate.of(2010, 11, 02));
        when(counterPartyRepository.findById(eq(COUNTER_PARTY_ID)))
                .thenReturn(Optional.of(new CounterParty(COUNTER_PARTY_ID)));
        when(bookRepository.findById(eq(B_1))).thenReturn(Optional.of(new Book(B_1)));
        Trade trade = mock(Trade.class);
        when(tradeRepository.save(any())).thenReturn(trade);

        TradeInput tradeInput = TradeInput.builder()
                .setBookId(B_1)
                .setTradeId(T_1)
                .setCounterPartyId(COUNTER_PARTY_ID)
                .setMaturityDate("2010-12-03")
                .setVersion(v_1)
                .build();
        Trade trade1 = tradeController.saveTrade(tradeInput);
        assertSame(trade, trade1);
    }

    @Test(expected = StaleTradeException.class)
    public void test_when_existing_trade_with_greater_version_expect_exception() throws ExpiredTradeException, UnknownCounterPartyException, StaleTradeException, UnknownBookException {
        when(systemDateManager.getCurrentDate()).thenReturn(LocalDate.of(2010, 11, 02));
        when(counterPartyRepository.findById(eq(COUNTER_PARTY_ID)))
                .thenReturn(Optional.of(new CounterParty(COUNTER_PARTY_ID)));
        when(bookRepository.findById(eq(B_1))).thenReturn(Optional.of(new Book(B_1)));
        Trade trade = mock(Trade.class);
        when(tradeRepository.findByTradeId(eq(T_1))).thenReturn(
                Collections.singletonList(Trade.builder().tradeId(T_1).version(2).build()));

        TradeInput tradeInput = TradeInput.builder()
                .setBookId(B_1)
                .setTradeId(T_1)
                .setCounterPartyId(COUNTER_PARTY_ID)
                .setMaturityDate("2010-12-03")
                .setVersion(v_1)
                .build();
        Trade trade1 = tradeController.saveTrade(tradeInput);
        assertSame(trade, trade1);
    }

    @Test
    public void test_when_existing_trade_with_same_version_expect_merge() throws ExpiredTradeException, UnknownCounterPartyException, StaleTradeException, UnknownBookException {
        when(systemDateManager.getCurrentDate()).thenReturn(LocalDate.of(2010, 11, 02));
        when(counterPartyRepository.findById(eq(COUNTER_PARTY_ID)))
                .thenReturn(Optional.of(new CounterParty(COUNTER_PARTY_ID)));
        when(bookRepository.findById(eq(B_1))).thenReturn(Optional.of(new Book(B_1)));

        when(tradeRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());

        when(tradeRepository.findByTradeId(eq(T_1))).thenReturn(
                Collections.singletonList(Trade.builder().tradeId(T_1).version(v_1).build()));

        TradeInput tradeInput = TradeInput.builder()
                .setBookId(B_1)
                .setTradeId(T_1)
                .setCounterPartyId(COUNTER_PARTY_ID)
                .setMaturityDate("2010-12-03")
                .setVersion(v_1)
                .build();
        Trade trade = tradeController.saveTrade(tradeInput);
        assertEquals(LocalDate.of(2010, 12, 03), trade.getMaturityDate());

    }

    @Test
    public void test_when_existing_trade_with_lesser_version_expect_persist() throws ExpiredTradeException, UnknownCounterPartyException, StaleTradeException, UnknownBookException {
        when(systemDateManager.getCurrentDate()).thenReturn(LocalDate.of(2010, 11, 02));
        when(counterPartyRepository.findById(eq(COUNTER_PARTY_ID)))
                .thenReturn(Optional.of(new CounterParty(COUNTER_PARTY_ID)));
        when(bookRepository.findById(eq(B_1))).thenReturn(Optional.of(new Book(B_1)));

        when(tradeRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());
        when(tradeRepository.findByTradeId(eq(T_1))).thenReturn(
                Collections.singletonList(Trade.builder().tradeId(T_1).version(v_1).build()));

        TradeInput tradeInput = TradeInput.builder()
                .setBookId(B_1)
                .setTradeId(T_1)
                .setCounterPartyId(COUNTER_PARTY_ID)
                .setMaturityDate("2010-12-03")
                .setVersion(2)
                .build();
        Trade trade = tradeController.saveTrade(tradeInput);
        assertEquals(COUNTER_PARTY_ID, trade.getCounterParty().getId());
        assertEquals(LocalDate.of(2010, 12, 03), trade.getMaturityDate());
    }

}

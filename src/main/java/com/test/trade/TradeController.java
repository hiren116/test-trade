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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class TradeController {

    @Autowired
    private SystemDateManager systemDateManager;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private CounterPartyRepository counterPartyRepository;

    @PostMapping("/trades")
    public Trade saveTrade(@RequestBody TradeInput tradeInput) throws ExpiredTradeException,
            UnknownCounterPartyException, UnknownBookException, StaleTradeException {

        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE;
        final LocalDate maturityDate = LocalDate.parse(tradeInput.getMaturityDate(), dateTimeFormatter);

        if(maturityDate.compareTo(systemDateManager.getCurrentDate()) < 0){
            throw new ExpiredTradeException(String.format("Unable to add trade with maturity date %s " +
                            "earlier than current date %s",
                    maturityDate, systemDateManager.getCurrentDate()));
        }

        Optional<CounterParty> counterPartyOptional = counterPartyRepository.findById(tradeInput.getCounterPartyId());
        final CounterParty counterParty = counterPartyOptional.orElseThrow(
                ()-> new UnknownCounterPartyException(String.format("Trade Input has unknown counter party %s ",
                tradeInput.getCounterPartyId())));

        Optional<Book> bookOptional = bookRepository.findById(tradeInput.getBookId());
        final Book book = bookOptional.orElseThrow(
                ()-> new UnknownBookException(String.format("Trade Input has unknown book %s ",
                        tradeInput.getCounterPartyId())));

        final List<Trade> trades = tradeRepository.findByTradeId(tradeInput.getTradeId());
        if(trades.isEmpty()){
            return saveAndGetTrade(tradeInput, maturityDate, counterParty, book);
        }

        final int existingVersion = trades.stream().mapToInt(Trade::getVersion).max().orElse(-1);
        if(tradeInput.getVersion() < existingVersion){
            throw new StaleTradeException("Trade Input has version smaller than existing trade");
        }

        if(tradeInput.getVersion() > existingVersion){
            return saveAndGetTrade(tradeInput, maturityDate, counterParty, book);
        }

        final Trade existingTrade = trades.stream().filter(t-> t.getVersion() == existingVersion).findFirst().get();
        mergeInputToExistingTrade(tradeInput, maturityDate, book, counterParty, existingTrade);
        return tradeRepository.save(existingTrade);

    }

    private Trade saveAndGetTrade(@RequestBody TradeInput tradeInput, LocalDate maturityDate, CounterParty counterParty, Book book) {
        Trade trade = Trade.builder().counterParty(counterParty).book(book).version(tradeInput.getVersion())
                .maturityDate(maturityDate).creationDate(systemDateManager.getCurrentDate()).tradeId(tradeInput.getTradeId())
                .build();
        return tradeRepository.save(trade);
    }

    private void mergeInputToExistingTrade(TradeInput tradeInput, LocalDate maturityDate,
                                           Book book, CounterParty counterParty, Trade trade) {
        trade.setBook(book);
        trade.setMaturityDate(maturityDate);
        trade.setCounterParty(counterParty);
        trade.setVersion(tradeInput.getVersion());
    }

}

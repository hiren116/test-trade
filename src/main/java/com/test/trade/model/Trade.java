package com.test.trade.model;

import org.springframework.data.annotation.Version;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    private String tradeId;
    private int version;

    private LocalDate maturityDate;
    private boolean expired = false;
    private LocalDate creationDate;

    @ManyToOne
    private CounterParty counterParty;

    @ManyToOne
    private Book book;

    @Version
    private int entityVersion;

    public Trade() {
    }

    private Trade(String tradeId, CounterParty counterParty, Book book, int version, LocalDate maturityDate, LocalDate creationDate) {
        this.tradeId = tradeId;
        this.counterParty = counterParty;
        this.book = book;
        this.version = version;
        this.maturityDate = maturityDate;
        this.creationDate = creationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTradeId() {
        return tradeId;
    }

    public CounterParty getCounterParty() {
        return counterParty;
    }

    public Book getBook() {
        return book;
    }

    public int getVersion() {
        return version;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public boolean isExpired() {
        return expired;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public void setCounterParty(CounterParty counterParty) {
        this.counterParty = counterParty;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "tradeId='" + tradeId + '\'' +
                ", version=" + version +
                ", maturityDate=" + maturityDate +
                ", isMature=" + expired +
                ", counterParty=" + counterParty +
                ", book=" + book +
                ", creationDate=" + creationDate +
                ", entityVersion=" + entityVersion +
                '}';
    }

    public static TradeBuilder builder(){
        return new TradeBuilder();
    }

    public static class TradeBuilder {
        private String tradeId;
        private CounterParty counterParty;
        private Book book;
        private int version;
        private LocalDate maturityDate;
        private LocalDate creationDate;

        public TradeBuilder tradeId(String tradeId) {
            this.tradeId = tradeId;
            return this;
        }

        public TradeBuilder counterParty(CounterParty counterParty) {
            this.counterParty = counterParty;
            return this;
        }

        public TradeBuilder book(Book book) {
            this.book = book;
            return this;
        }

        public TradeBuilder version(int version) {
            this.version = version;
            return this;
        }

        public TradeBuilder maturityDate(LocalDate maturityDate) {
            this.maturityDate = maturityDate;
            return this;
        }

        public TradeBuilder creationDate(LocalDate creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public Trade build() {
            return new Trade(tradeId, counterParty, book, version, maturityDate, creationDate);
        }
    }

}

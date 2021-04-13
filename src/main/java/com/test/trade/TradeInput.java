package com.test.trade;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class TradeInput {
    @NotEmpty(message = "'tradeId' should not be empty")
    private String tradeId;

    @NotEmpty(message = "'counterPartyId' should not be empty")
    private String counterPartyId;

    @NotEmpty(message = "'bookId' should not be empty")
    private String bookId;

    @Min(value=1, message = "'version' should not be greater than 1")
    private int version;

    @NotEmpty(message = "'maturityDate' should not be empty, expected format 'YYYY-MM-DD'")
    private String maturityDate;

    public TradeInput() {
    }

    public TradeInput(@NotEmpty(message = "'tradeId' should not be empty") String tradeId,
                      @NotEmpty(message = "'counterPartyId' should not be empty") String counterPartyId,
                      @NotEmpty(message = "'bookId' should not be empty") String bookId,
                      @NotEmpty(message = "'version' should not be empty") int version,
                      @NotEmpty(message = "'maturityDate' should not be empty, expected format 'YYYY-MM-DD'") String maturityDate) {
        this.tradeId = tradeId;
        this.counterPartyId = counterPartyId;
        this.bookId = bookId;
        this.version = version;
        this.maturityDate = maturityDate;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getCounterPartyId() {
        return counterPartyId;
    }

    public void setCounterPartyId(String counterPartyId) {
        this.counterPartyId = counterPartyId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    @Override
    public String toString() {
        return "TradeInput{" +
                "tradeId='" + tradeId + '\'' +
                ", counterPartyId=" + counterPartyId +
                ", bookId=" + bookId +
                ", version=" + version +
                ", maturityDate='" + maturityDate + '\'' +
                '}';
    }

    public static TradeInputBuilder builder(){
        return new TradeInputBuilder();
    }

    public static class TradeInputBuilder {
        private String tradeId;
        private String counterPartyId;
        private String bookId;
        private int version;
        private String maturityDate;

        public TradeInputBuilder setTradeId(@NotEmpty(message = "'tradeId' should not be empty") String tradeId) {
            this.tradeId = tradeId;
            return this;
        }

        public TradeInputBuilder setCounterPartyId(@NotEmpty(message = "'counterPartyId' should not be empty") String counterPartyId) {
            this.counterPartyId = counterPartyId;
            return this;
        }

        public TradeInputBuilder setBookId(@NotEmpty(message = "'bookId' should not be empty") String bookId) {
            this.bookId = bookId;
            return this;
        }

        public TradeInputBuilder setVersion(@NotEmpty(message = "'version' should not be empty") int version) {
            this.version = version;
            return this;
        }

        public TradeInputBuilder setMaturityDate(@NotEmpty(message = "'maturityDate' should not be empty, expected format 'YYYY-MM-DD'") String maturityDate) {
            this.maturityDate = maturityDate;
            return this;
        }

        public TradeInput build() {
            return new TradeInput(tradeId, counterPartyId, bookId, version, maturityDate);
        }
    }

}

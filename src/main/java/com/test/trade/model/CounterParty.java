package com.test.trade.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CounterParty {

    @Id
    private String id;
    private String counterPartyName;

    public CounterParty() {
    }

    public CounterParty(String counterPartyId) {
        this.id = counterPartyId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCounterPartyName() {
        return counterPartyName;
    }

    public void setCounterPartyName(String counterPartyName) {
        this.counterPartyName = counterPartyName;
    }
}

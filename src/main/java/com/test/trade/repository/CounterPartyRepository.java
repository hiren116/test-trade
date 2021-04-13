package com.test.trade.repository;

import com.test.trade.model.CounterParty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounterPartyRepository extends CrudRepository<CounterParty, String> {
}

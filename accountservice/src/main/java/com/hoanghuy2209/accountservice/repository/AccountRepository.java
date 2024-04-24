package com.hoanghuy2209.accountservice.repository;

import com.hoanghuy2209.accountservice.data.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account,String> {
}
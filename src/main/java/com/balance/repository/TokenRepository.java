package com.balance.repository;

import com.balance.model.Token;
import com.balance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by da_20 on 29/5/2017.
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    Token findByToken(String token);
}

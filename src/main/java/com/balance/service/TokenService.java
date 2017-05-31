package com.balance.service;

import com.balance.model.Token;

/**
 * Created by da_20 on 29/5/2017.
 */
public interface TokenService {
    void saveToken(Token token);
    Iterable<Token> listAllTokens();
    Token getTokenById(Integer id);
    void deleteToken(Integer id);
    Token findByToken(String token);


}

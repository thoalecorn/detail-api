package com.nelumbo.dental_api.security;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlacklist {

    private final Map<String, Long> blacklistedTokens = new ConcurrentHashMap<>();

    public void add(String token, long expirationMillis) {
        blacklistedTokens.put(token, expirationMillis);
    }

    public boolean isBlacklisted(String token) {
        Long expiration = blacklistedTokens.get(token);
        if (expiration == null) return false;
        if (System.currentTimeMillis() > expiration) {
            blacklistedTokens.remove(token);
            return false;
        }
        return true;
    }
}
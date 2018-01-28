package co.leanjava.filters;

import com.google.common.io.BaseEncoding;

import java.security.SecureRandom;

/**
 * @author ameya
 */
public class TokenCredentials {

    private static final SecureRandom PRNG = new SecureRandom();
    private static final int TOKEN_SIZE = 20;

    private final String authToken;
    private final String csrfToken;

    public TokenCredentials(String authToken, String csrfToken) {
        this.authToken = authToken;
        this.csrfToken = csrfToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public static TokenCredentials generateRandomCredentials() {
        return new TokenCredentials(generateRandomToken(), generateRandomToken());
    }

    private static String generateRandomToken() {
        final byte[] tokenBytes = new byte[TOKEN_SIZE];
        PRNG.nextBytes(tokenBytes);
        return BaseEncoding.base16().lowerCase().encode(tokenBytes);
    }
}

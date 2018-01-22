package co.leanjava.filters;

import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;

/**
 * @author ameya
 */
public class TokenCredentials {

    private static final SecureRandom PRNG = new SecureRandom();
    private static final int TOKEN_SIZE = 20;

    private final String token;

    public TokenCredentials(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public static TokenCredentials generateRandomToken() {
        final byte[] tokenBytes = new byte[TOKEN_SIZE];
        PRNG.nextBytes(tokenBytes);
        return new TokenCredentials(DatatypeConverter.printHexBinary(tokenBytes));
    }
}

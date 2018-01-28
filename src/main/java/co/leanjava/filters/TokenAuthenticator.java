package co.leanjava.filters;

import co.leanjava.db.AuthTokenDao;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

import java.util.Optional;

/**
 * @author ameya
 */
public class TokenAuthenticator implements Authenticator<TokenCredentials, User> {

    private final AuthTokenDao dao;

    public TokenAuthenticator(AuthTokenDao dao) {
        this.dao = dao;
    }

    @Override
    public Optional<User> authenticate(TokenCredentials tokenCredentials) throws AuthenticationException {
        return Optional.ofNullable(this.dao.getUserForToken(tokenCredentials)).map(User::new);
    }
}

package co.leanjava.filters;

import com.google.common.base.Strings;
import io.dropwizard.auth.AuthFilter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;
import java.io.IOException;
import java.util.Map;

/**
 * @author ameya
 */
public class TokenAuthFilter extends AuthFilter<TokenCredentials, User> {

    public static final String TOKEN_COOKIE_NAME = "token";

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        final Map<String, Cookie> cookies = requestContext.getCookies();
        final Cookie tokenCookie = cookies.get(TOKEN_COOKIE_NAME);
        if (tokenCookie == null ||
                Strings.isNullOrEmpty(tokenCookie.getValue()) ||
                !authenticate(requestContext, new TokenCredentials(tokenCookie.getValue()), "COOKIE_TOKEN_AUTH")) {
            throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
        }
    }

    public static class TokenAuthFilterBuilder extends AuthFilter.AuthFilterBuilder<TokenCredentials, User, TokenAuthFilter> {

        @Override
        protected TokenAuthFilter newInstance() {
            return new TokenAuthFilter();
        }
    }
}

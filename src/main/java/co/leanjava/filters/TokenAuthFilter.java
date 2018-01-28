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

    public static final String AUTH_TOKEN_COOKIE_NAME = "AUTH-TOKEN";

    // This is the default cookie name that Angular JS looks for
    public static final String CSRF_TOKEN_COOKIE_NAME = "XSRF-TOKEN";

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        final Map<String, Cookie> cookies = requestContext.getCookies();
        final Cookie tokenCookie = cookies.get(AUTH_TOKEN_COOKIE_NAME);
        final Cookie csrfCookie = cookies.get(CSRF_TOKEN_COOKIE_NAME);
        if (!isCookieValid(tokenCookie)
                || !isCookieValid(csrfCookie)
                || !authenticate(requestContext, new TokenCredentials(tokenCookie.getValue(), csrfCookie.getValue()), "COOKIE_TOKEN_AUTH")) {
            throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
        }
    }

    private boolean isCookieValid(final Cookie cookie) {
        return cookie != null && !Strings.isNullOrEmpty(cookie.getValue());
    }


    public static class TokenAuthFilterBuilder extends AuthFilter.AuthFilterBuilder<TokenCredentials, User, TokenAuthFilter> {

        @Override
        protected TokenAuthFilter newInstance() {
            return new TokenAuthFilter();
        }
    }
}

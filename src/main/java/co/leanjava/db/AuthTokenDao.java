package co.leanjava.db;

import co.leanjava.filters.TokenCredentials;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author ameya
 */
public interface AuthTokenDao {

    @SqlUpdate("INSERT INTO auth_tokens (user_id, auth_token, csrf_token, expires)" +
            " VALUES (:userId, :creds.authToken, :creds.csrfToken, TIMESTAMPADD(SECOND, :expires, NOW()))")
    int insertToken(@Bind("userId") int userId, @Bind("creds") TokenCredentials creds, @Bind("expires") long expires);

    @SqlUpdate("DELETE FROM auth_tokens where user_id = :userId")
    void deleteTokenForUser(@Bind("userId") int userId);

    @SqlQuery("SELECT user_id from auth_tokens where auth_token = :creds.authToken" +
            " AND csrf_token = :creds.csrfToken" +
            " AND TIMESTAMPDIFF(SECOND, NOW(), expires) > 0")
    Integer getUserForToken(@Bind("creds") TokenCredentials creds);
}

package co.leanjava.db;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author ameya
 */
public interface AuthTokenDao {

    @SqlUpdate("INSERT INTO auth_tokens (user_id, token, expires) VALUES (:userId, :token, TIMESTAMPADD(SECOND, :expires, NOW()))")
    int insertToken(@Bind("userId") int userId, @Bind("token") String token, @Bind("expires") long expires);

    @SqlUpdate("DELETE FROM auth_tokens where user_id = :userId")
    void deleteTokenForUser(@Bind("userId") int userId);

    @SqlQuery("SELECT user_id from auth_tokens where token = :token AND TIMESTAMPDIFF(SECOND, NOW(), expires) > 0")
    Integer getUserForToken(@Bind("token") String token);
}

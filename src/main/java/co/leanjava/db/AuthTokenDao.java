package co.leanjava.db;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author ameya
 */
public interface AuthTokenDao {

    @SqlUpdate("INSERT INTO auth_tokens (user_id, token, expires) VALUES (:userId, :token, :expires)")
    void insertToken(@Bind("userId") String userId, @Bind("token") String token, @Bind("expires") long expires);
}

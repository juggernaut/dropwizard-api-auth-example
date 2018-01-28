package co.leanjava.security;

import java.security.Principal;

/**
 * @author ameya
 */
public class User implements Principal {

    private final int id;

    public User(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}

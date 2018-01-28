CREATE TABLE auth_tokens(
    id int(11) unsigned NOT NULL AUTO_INCREMENT,
    auth_token varchar(40),
    csrf_token varchar(40),
    user_id int(11) unsigned NOT NULL,
    expires timestamp NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (user_id)
);
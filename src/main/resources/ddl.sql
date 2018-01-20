CREATE TABLE auth_tokens(
    id int(11) unsigned NOT NULL AUTO_INCREMENT,
    token varchar(40),
    userid int(11) unsigned NOT NULL,
    expires timestamp NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (userid, token)
);
CREATE TABLE IF NOT EXISTS users
(
    id       LONG AUTO_INCREMENT NOT NULL,
    username VARCHAR(200)        NOT NULL UNIQUE,
    password VARCHAR(500)        NOT NULL,
    enabled  BOOLEAN DEFAULT TRUE,
    CONSTRAINT user_pk PRIMARY KEY (id),
    CONSTRAINT user_name_length CHECK (length(username) > 2)
);

CREATE TABLE IF NOT EXISTS notes
(
    id      UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    title   VARCHAR(200)  NOT NULL CHECK (length(title) > 1),
    content VARCHAR(2000) NOT NULL CHECK (length(title) > 1),
    user_id LONG          NOT NULL,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS roles
(
    id   LONG AUTO_INCREMENT NOT NULL,
    name VARCHAR(50)         NOT NULL,
    CONSTRAINT role_pk PRIMARY KEY (id)
);


CREATE TABLE user_roles
(
    id      LONG AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id LONG NOT NULL,
    role_id LONG NOT NULL,
    CONSTRAINT role_fk FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE,
    CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

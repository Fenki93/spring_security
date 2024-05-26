INSERT INTO users (username, password)
VALUES ('admin', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW'),
       ('user', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW');

INSERT INTO notes (id, title, content, user_id)
VALUES ('ec4fe450-6511-4689-87f0-295b224c4200', 'Admin Title1', 'Content1', 1),
       ('77415d89-c15d-48fd-9c8c-181cd9c10d19', 'User Title2', 'Content2', 2),
       ('6848fd87-d2ad-427f-93a7-208630afe9b1', 'Admin Title3', 'Content3', 1);

INSERT INTO roles (name) VALUES ('ADMIN'),('USER');

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1), (1, 2), (2, 2);


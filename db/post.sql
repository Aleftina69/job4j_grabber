CREATE TABLE post (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      text TEXT NOT NULL,
                      link VARCHAR(255) NOT NULL,
                      created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      UNIQUE (name, link)
);
CREATE TABLE IF NOT EXISTS Users (
  id         BIGINT PRIMARY KEY AUTO_INCREMENT,
  email      VARCHAR(255) NOT NULL UNIQUE,
  password   VARCHAR(255) NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  last_name  VARCHAR(255) NOT NULL,
  gender     INT          NOT NULL,
  role       INT          NOT NULL,
);

CREATE TABLE IF NOT EXISTS Teams (
  id   BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(30) UNIQUE,
);

CREATE TABLE IF NOT EXISTS Profiles (
  id         BIGINT PRIMARY KEY,
  telephone  VARCHAR(20),
  birthday   DATE,
  country    VARCHAR(25),
  city       VARCHAR(25),
  university VARCHAR(50),
  team       INT REFERENCES Teams (id),
  position   VARCHAR(25),
  about      VARCHAR(255),
  FOREIGN KEY (id) REFERENCES Users (id),
);

CREATE TABLE IF NOT EXISTS Relations (
  id        BIGINT PRIMARY KEY AUTO_INCREMENT,
  sender    BIGINT NOT NULL,
  recipient BIGINT NOT NULL,
  type  INT,
  FOREIGN KEY (sender) REFERENCES Users(id),
  FOREIGN KEY (recipient) REFERENCES Users(id)
);


CREATE INDEX INDEX_EMAIL
  ON Users (email);
CREATE INDEX INDEX_F_NAME
  ON Users (first_name);
CREATE INDEX INDEX_L_NAME
  ON Users (last_name);
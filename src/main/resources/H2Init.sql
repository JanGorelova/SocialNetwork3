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
  team       VARCHAR(30),
  position   VARCHAR(25),
  about      VARCHAR(255),
  FOREIGN KEY (id) REFERENCES Users (id),
);

CREATE TABLE IF NOT EXISTS Photos (
  id     BIGINT PRIMARY KEY AUTO_INCREMENT,
  sender BIGINT,
  time   DATETIME,
  avatar BOOLEAN            DEFAULT FALSE,
  link   VARCHAR(255) NOT NULL,
  FOREIGN KEY (sender) REFERENCES Users (id),
);

CREATE TABLE IF NOT EXISTS Posts (
  id        BIGINT PRIMARY KEY AUTO_INCREMENT,
  sender    BIGINT,
  recipient BIGINT,
  time      DATETIME,
  message   VARCHAR(1000),
  FOREIGN KEY (sender) REFERENCES Users (id),
  FOREIGN KEY (recipient) REFERENCES Users (id),
);

CREATE TABLE IF NOT EXISTS Relations (
  id        BIGINT PRIMARY KEY AUTO_INCREMENT,
  sender    BIGINT NOT NULL,
  recipient BIGINT NOT NULL,
  type      INT,
  FOREIGN KEY (sender) REFERENCES Users (id),
  FOREIGN KEY (recipient) REFERENCES Users (id)
);

CREATE TABLE IF NOT EXISTS Chats (
  id          BIGINT PRIMARY KEY AUTO_INCREMENT,
  creator_id  BIGINT    NOT NULL,
  last_update TIMESTAMP NOT NULL,
  start_time  TIMESTAMP NOT NULL,
  name        VARCHAR(20)        DEFAULT 'private',
  description VARCHAR(30),
  FOREIGN KEY (creator_id) REFERENCES Users (id)
);

CREATE TABLE IF NOT EXISTS Messages (
  id           BIGINT PRIMARY KEY AUTO_INCREMENT,
  sender_id    BIGINT    NOT NULL,
  chat_id      BIGINT    NOT NULL,
  text         VARCHAR(1000),
  sending_time TIMESTAMP NOT NULL,
  FOREIGN KEY (sender_id) REFERENCES Users (id),
  FOREIGN KEY (chat_id) REFERENCES Chats (id)
);

CREATE TABLE IF NOT EXISTS Chat_Participants (
  id             BIGINT PRIMARY KEY AUTO_INCREMENT,
  participant_id BIGINT NOT NULL,
  chat_id        BIGINT NOT NULL,
  last_read      TIMESTAMP,
  FOREIGN KEY (participant_id) REFERENCES Users (id),
  FOREIGN KEY (chat_id) REFERENCES Chats (id)
);

CREATE INDEX INDEX_EMAIL
  ON Users (email);
CREATE INDEX INDEX_F_NAME
  ON Users (first_name);
CREATE INDEX INDEX_L_NAME
  ON Users (last_name);
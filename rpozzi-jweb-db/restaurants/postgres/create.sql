--
-- Table structure for table `RESTAURANTS`
--
DROP TABLE IF EXISTS RESTAURANTS;
CREATE TABLE RESTAURANTS (
  ID INT NOT NULL,
  RESTNAME VARCHAR(150) DEFAULT NULL,
  PRIMARY KEY (ID)
);
INSERT INTO RESTAURANTS (ID, RESTNAME) VALUES (1, 'I Fontanili');
INSERT INTO RESTAURANTS (ID, RESTNAME) VALUES (2, 'Il Ragazzo di Campagna');
INSERT INTO RESTAURANTS (ID, RESTNAME) VALUES (3, 'Hostaria del Porto');
INSERT INTO RESTAURANTS (ID, RESTNAME) VALUES (4, 'La Nautica');

--
-- Table structure for table `SEQUENCE`
--
DROP TABLE IF EXISTS SEQUENCE;
CREATE TABLE SEQUENCE (
  SEQ_NAME VARCHAR(50) NOT NULL,
  SEQ_COUNT DECIMAL(38,0) DEFAULT NULL,
  PRIMARY KEY (SEQ_NAME)
);
INSERT INTO SEQUENCE VALUES ('SEQ_GEN',4);

--
-- Table structure for table `TODOLIST`
--
DROP TABLE IF EXISTS TODOLIST;
CREATE TABLE TODOLIST (
  L_ID BIGINT NOT NULL,
  C_NAME VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (L_ID)
);
INSERT INTO TODOLIST VALUES (1,'TODO');
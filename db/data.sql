# The Entity Beans create these tables themselves so this SQL is not needed, don't delete this though
# Police primary key is now Person_ID, this won't affect searching during login- BadgeNumber column still exists and I can make it UNIQUE and NOT NULL
# CREATE TABLE Person (
#   Person_ID int(11) NOT NULL AUTO_INCREMENT,
#   First_name varchar(50) NOT NULL,
#   Middle_name varchar(50) DEFAULT NULL,
#   Last_name varchar(50) NOT NULL,
#   Address_person varchar(150) DEFAULT NULL,
#   Alias varchar(50) DEFAULT NULL,
#   Date_Of_Birth date NOT NULL,
#   Sex varchar(50) NOT NULL,
#   Ethnic_Origin varchar(20) DEFAULT NULL,
#   Height int(11) DEFAULT NULL,
#   Weight int(11) DEFAULT NULL,
#   PRIMARY KEY (Person_ID)
# );
#
# CREATE TABLE Police (
#   Badge_Number int(11) NOT NULL AUTO_INCREMENT,
#   Password_Hash varchar(256) NOT NULL,
#   Salt binary(16) NOT NULL,
#   Person_ID int(11) NOT NULL,
#   Rank_l varchar(50) NOT NULL,
#   Role varchar(50) NOT NULL,
#   Security_Access_Level varchar(50) NOT NULL,
#   PRIMARY KEY (Badge_Number),
#   KEY Police_FK (Person_ID),
#   CONSTRAINT Police_FK FOREIGN KEY (Person_ID) REFERENCES Person (Person_ID) ON DELETE CASCADE ON UPDATE CASCADE
# );
#
# CREATE TABLE Incident_Report (
#   Incident_URN int(11) NOT NULL AUTO_INCREMENT,
#   Badge_Number int(11) NOT NULL,
#   Date_and_Time datetime DEFAULT NULL,
#   Location varchar(100) DEFAULT NULL,
#   Crime_Type varchar(50) DEFAULT NULL,
#   PRIMARY KEY (Incident_URN),
#   KEY Incident_Report_FK (Badge_Number),
#   CONSTRAINT Incident_Report_FK FOREIGN KEY (Badge_Number) REFERENCES Police (Badge_Number)
# );
#
# CREATE TABLE Evidence (
#   Evidence_ID int(50) NOT NULL AUTO_INCREMENT,
#   Incident_URN int(11) NOT NULL,
#   Name varchar(100) NOT NULL,
#   Evidence_file longblob NOT NULL,
#   Security_Access_Level varchar(50) NOT NULL,
#   Description longtext NOT NULL,
#   PRIMARY KEY (Evidence_ID),
#   KEY Evidence_FK (Incident_URN),
#   CONSTRAINT Evidence_FK FOREIGN KEY (Incident_URN) REFERENCES Incident_Report (Incident_URN)
# );
#
# CREATE TABLE SuspectLink (
#   Person_ID int(11) NOT NULL,
#   Incident_URN int(11) NOT NULL,
#   KEY SuspectLink_FK (Incident_URN),
#   KEY SuspectLink_FK_1 (Person_ID),
#   CONSTRAINT SuspectLink_FK FOREIGN KEY (Incident_URN) REFERENCES Incident_Report (Incident_URN) ON DELETE CASCADE ON UPDATE CASCADE,
#   CONSTRAINT SuspectLink_FK_1 FOREIGN KEY (Person_ID) REFERENCES Person (Person_ID) ON DELETE CASCADE ON UPDATE CASCADE
# );
#
# CREATE TABLE NRAC_Review (
#   NRAC_ID int(11) NOT NULL AUTO_INCREMENT,
#   Badge_Number int(11) NOT NULL,
#   Action varchar(50) NOT NULL,
#   Description longtext NOT NULL,
#   Security_Access_Level varchar(50) NOT NULL,
#   PRIMARY KEY (NRAC_ID),
#   KEY NRAC_Review_FK (Badge_Number),
#   CONSTRAINT NRAC_Review_FK FOREIGN KEY (Badge_Number) REFERENCES Police (Badge_Number) ON DELETE CASCADE ON UPDATE CASCADE
# );
#
# CREATE TABLE Licenses (
#   License_ID int(11) NOT NULL AUTO_INCREMENT,
#   License_Type varchar(50) NOT NULL,
#   PRIMARY KEY (License_ID)
# );
#
# CREATE TABLE License_Link_Table (
#   Person_ID int(11) NOT NULL,
#   License_ID int(11) NOT NULL,
#   Justification varchar(100) DEFAULT NULL,
#   KEY License_Link_Table_FK (Person_ID),
#   KEY License_Link_Table_FK_1 (License_ID),
#   CONSTRAINT License_Link_Table_FK FOREIGN KEY (Person_ID) REFERENCES Person (Person_ID),
#   CONSTRAINT License_Link_Table_FK_1 FOREIGN KEY (License_ID) REFERENCES Licenses (License_ID)
# )
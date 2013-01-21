ALTER TABLE PL_COMPETITION DROP COLUMN DEFAULTTOURNAMENTDEADLINE
ALTER TABLE PL_COMPETITION ADD COLUMN (DEFAULTTOURNAMENTANNOUNCEMENTLEAD INTEGER)
UPDATE PL_COMPETITION SET DEFAULTTOURNAMENTANNOUNCEMENTLEAD = 48
ALTER TABLE PL_TOURNAMENT DROP COLUMN DEADLINE
ALTER TABLE PL_TOURNAMENT ADD COLUMN (TOURNAMENTANNOUNCEMENTLEAD INTEGER)
UPDATE PL_TOURNAMENT SET TOURNAMENTANNOUNCEMENTLEAD = 48
CREATE TABLE PL_INVITATIONEVENT (ID INTEGER NOT NULL, EVENTTIME DATETIME, EVENTTYPE VARCHAR(255), OBSOLETE TINYINT(1) default 0, SENT TINYINT(1) default 0, INVITATION_ID INTEGER, PRIMARY KEY (ID))
ALTER TABLE PL_INVITATIONEVENT ADD CONSTRAINT FK_PL_INVITATIONEVENT_INVITATION_ID FOREIGN KEY (INVITATION_ID) REFERENCES PL_INVITATION (ID)

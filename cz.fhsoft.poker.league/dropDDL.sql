ALTER TABLE PL_PRIZEMONEYRULE DROP FOREIGN KEY FK_PL_PRIZEMONEYRULE_PRIZEMONEYRULESET_ID
ALTER TABLE PL_PRIZEMONEYFORMULA DROP FOREIGN KEY FK_PL_PRIZEMONEYFORMULA_PRIZEMONEYRULE_ID
ALTER TABLE PL_COMPETITION DROP FOREIGN KEY FK_PL_COMPETITION_DEFAULTPRIZEMONEYRULESET_ID
ALTER TABLE PL_TOURNAMENT DROP FOREIGN KEY FK_PL_TOURNAMENT_DEFAULTPRIZEMONEYRULESET_ID
ALTER TABLE PL_TOURNAMENT DROP FOREIGN KEY FK_PL_TOURNAMENT_COMPETITION_ID
ALTER TABLE PL_INVITATION DROP FOREIGN KEY FK_PL_INVITATION_TOURNAMENT_ID
ALTER TABLE PL_INVITATION DROP FOREIGN KEY FK_PL_INVITATION_PLAYER_ID
ALTER TABLE PL_INVITATIONEVENT DROP FOREIGN KEY FK_PL_INVITATIONEVENT_INVITATION_ID
ALTER TABLE PL_GAME DROP FOREIGN KEY FK_PL_GAME_TOURNAMENT_ID
ALTER TABLE PL_GAME DROP FOREIGN KEY FK_PL_GAME_PRIZEMONEYRULESET_ID
ALTER TABLE PL_PLAYERINGAME DROP FOREIGN KEY FK_PL_PLAYERINGAME_PLAYER_ID
ALTER TABLE PL_PLAYERINGAME DROP FOREIGN KEY FK_PL_PLAYERINGAME_GAME_ID
ALTER TABLE PL_COMPETITION_PL_PLAYER DROP FOREIGN KEY FK_PL_COMPETITION_PL_PLAYER_pl_Competition_ID
ALTER TABLE PL_COMPETITION_PL_PLAYER DROP FOREIGN KEY FK_PL_COMPETITION_PL_PLAYER_players_ID
DROP TABLE PL_DATASTRUCTUREVERSION
DROP TABLE PL_DATAVERSION
DROP TABLE PL_PRIZEMONEYRULESET
DROP TABLE PL_PRIZEMONEYRULE
DROP TABLE PL_PRIZEMONEYFORMULA
DROP TABLE PL_PLAYER
DROP TABLE PL_COMPETITION
DROP TABLE PL_TOURNAMENT
DROP TABLE PL_INVITATION
DROP TABLE PL_INVITATIONEVENT
DROP TABLE PL_GAME
DROP TABLE PL_PLAYERINGAME
DROP TABLE PL_COMPETITION_PL_PLAYER
DELETE FROM SEQUENCE WHERE SEQ_NAME = 'SEQ_GEN'

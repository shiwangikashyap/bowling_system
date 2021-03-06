# Bowling System
This project contains implementations of Rest Web Services of a bowling system

## WORKING OF SYSTEM

1. Create game by submitting names of players participating in the game. Configurations and initialisation required by the system for starting a game are executed.
2. Configurations include storing game details ,player details and checking whether enough lanes are vacant to accomodate all players of a game at a time.
3. Initialisation include initiating first set and first trial of each new game players.
4. Game Rules : 
     * MAX_SET_TRIAL_COUNT = 2 i.e. Each set will have atmost of 2 trials if all pins are not knocked in the first trial.
     * MAX_SET_COUNT = 10 i.e. Each player in a game is allowed to  play 10 sets.
     * ADDITIONAL_SET_TRIAL_RULE = 1 i.e. A player that scores a spare or a strike in the last set will be allowed to play for 1 extra trial
     * SINGLE_PIN_SCORE = 1 i.e. Score per knocked pin.
     * SPARE_BONUS = 5 i.e. In case of a spare the player will be awarded a bonus of 5.
     * STRIKE_BONUS = 10 i.e. In case of a spare the player will be awarded a bonus of 10.
     * TOTAL_PINS_RULE = 10 i.e. Number of max pins in a play.
5. A game is said to be in
     * ONGOING status : If all players have been alocated lanes and any 1 player is currently not finished playing all sets.
     * WAITING status : If there are not enough vacant lanes to accomodate all players of the game
     * FINISHED status : If all players have completed the last set and the winner has been declared.
6. A player is said to be 
     * ACTIVE : If they are currently playing a game.
     * INACTIVE : If they are not playing a game (either the player has completed playing the last set or lanes were not allocated to the player)
7. Lane Allocation is done on the basis of FCFS i.e. the first vacant lane found in the system is allocated to the player.
8. The winner of the game is the player with highest number of scores, in case two players having same score ,winner declared is the on with more number of total strikes.
9. As the game ends the lanes occupied by players of this game are released.
10. The game determines the knocked pins in a set by random number generator.

## Tools and Technologies Required:
1. JUnit - 4
2. Java - 1.8
3. Spring Boot - 2.4.3
4. Mysql - 5.7.33
5. STS - 4.9 (other IDEs can also be used)

## SETUP
1. Create a database called bowling_system or update name of the database in main/src/main/resources/application.properties.
2. Enter username and password for accessing the db in main/src/main/resources/application.properties.
3. On running the system, tables will be created in th DB.
4. Insert all rules in the rules table :

```
INSERT INTO `bowling_system`.`rules` (`rule_name`, `rule_value`) VALUES ('MAX_SET_TRIAL_COUNT', '2');
INSERT INTO `bowling_system`.`rules` (`rule_name`, `rule_value`) VALUES ('MAX_SET_COUNT', '10');
INSERT INTO `bowling_system`.`rules` (`rule_name`, `rule_value`) VALUES ('ADDITIONAL_SET_TRIAL_RULE', '1');
INSERT INTO `bowling_system`.`rules` (`rule_name`, `rule_value`) VALUES ('SINGLE_PIN_SCORE', '1');
INSERT INTO `bowling_system`.`rules` (`rule_name`, `rule_value`) VALUES ('SPARE_BONUS', '5');
INSERT INTO `bowling_system`.`rules` (`rule_name`, `rule_value`) VALUES ('STRIKE_BONUS', '10');
INSERT INTO `bowling_system`.`rules` (`rule_name`, `rule_value`) VALUES ('TOTAL_PINS_RULE', '10');
```
5. Insert all available lanes and their vacancy in lane table

```
INSERT INTO `bowling_system`.`lane` (`lane`, `vacancy`) VALUES ('1', '4');
INSERT INTO `bowling_system`.`lane` (`lane`, `vacancy`) VALUES ('2', '4');
INSERT INTO `bowling_system`.`lane` (`lane`, `vacancy`) VALUES ('3', '4');
INSERT INTO `bowling_system`.`lane` (`lane`, `vacancy`) VALUES ('4', '4');
```

## TABLES
1. game                          -> (game_id, game_status, no_of_players, winner, winner_id, winner_score)
2. lane                          -> (lane, vacancy)
3. player                        -> (player_id, allocated_lane, current_player, current_set, current_set_id, game_id, is_active, missed_strikes, name, total_score, total_strikes)
4. player_set_history            -> (history_id, current_strikes, pins_knocked, player_id, score, set_id, trial)
5. rules                         -> (rule_name, rule_value)

## API
```json
1. Service to start game, takes input player names in key player_details and returns the gameId 
url :  http://localhost:9090/api/bowling/start
type : POST
Params : {
	"player_details":["Kavita","Sarita"]
	}
Content-type : application/json

Response 
2

2. Service to get details of a game on the basis of gameId 
url : http://localhost:9090/api/bowling/game/{gameId} =>  http://localhost:9090/api/bowling/game/1
type : GET

Response 
Content-type : application/json
Case 1 : when lane is not vacant (status 200) : {"gameId":1,"noOfPlayers":2,"winner":null,"winnerScore":0,"winnerId":0,"gameStatus":"WAITING"}
Case 2 : game has started (status 200) : {"gameId":1,"noOfPlayers":2,"winner":null,"winnerScore":0,"winnerId":0,"gameStatus":"ONGOING"}
Case 3 : game has finished (status 200): {"gameId":3,"noOfPlayers":2,"winner":"Sarita","winnerScore":96,"winnerId":3,"gameStatus":"FINISHED"}


3. Service to get players of a game
url : http://localhost:9090/api/bowling/game/{playerId}/players => http://localhost:9090/api/bowling/game/3/players
type : GET

Response
Content-type : application/json
Status : 200
[
    {
        "playerId": 3,
        "name": "Sarita",
        "gameId": 3,
        "allocatedLane": 1,
        "totalScore": 0,
        "totalStrikes": 0,
        "missedStrikes": 0,
        "currentPlayer": false,
        "currentSetId": 5,
        "currentSet": 1,
        "active": true
    },
    {
        "playerId": 4,
        "name": "Kavita",
        "gameId": 3,
        "allocatedLane": 1,
        "totalScore": 0,
        "totalStrikes": 0,
        "missedStrikes": 0,
        "currentPlayer": false,
        "currentSetId": 6,
        "currentSet": 1,
        "active": true
    }
]


4. Service for a player to play a set takes playerId as input and returns thw whole set details
url : http://localhost:9090/api/bowling/play
Content-type : application/json
Params : 3

Response
Case 1 : when player is active
Status : 200
[
    {
        "historyId": 5,
        "playerId": 3,
        "trial": "A",
        "setId": 1,
        "pinsKnocked": 9,
        "currentStrikes": 0,
        "currentScore": 9
    },
    {
        "historyId": 7,
        "playerId": 3,
        "trial": "B",
        "setId": 1,
        "pinsKnocked": 0,
        "currentStrikes": 0,
        "currentScore": 0
    }
]

Case 2 : if player id entered does not exist
Status : 404
{
    "errorMessage": "Player no. 3 does not exist",
    "errorCode": "NOT_FOUND"
}

Case : if player is in inactive state
Status : 409
{
    "errorMessage": "This action cannot be performed as player no. 3 is in invalid state",
    "errorCode": "CONFLICT"
}

5. Service to get score of a player at any time of the game w.r.t. playerId
url : http://localhost:9090/api/bowling/playerScore/{playerId} -> http://localhost:9090/api/bowling/playerScore/3
type : GET

Response
Content-type: application/json
Case 1 : player exists:
Status : 200
{
    "playerId": 3,
    "name": "Sarita",
    "gameId": 3,
    "allocatedLane": 1,
    "totalScore": 96,
    "totalStrikes": 1,
    "missedStrikes": 9,
    "currentPlayer": false,
    "currentSetId": 25,
    "currentSet": 10,
    "active": false
}
Case 2 : player does not exist
Status : 404
{
    "errorMessage": "Player no. 3 does not exist",
    "errorCode": "NOT_FOUND"
}

6. Service to finish the game and declare the winner . Input is the gameId
url : http://localhost:9090/api/bowling/finish
type : POST
Params : 3

Response
Content-type : application/json
Case 1 : game is in ongoing status and all players have finished playing
status : 200
{
    "gameId": 3,
    "noOfPlayers": 2,
    "winner": "Sarita",
    "winnerScore": 96,
    "winnerId": 3,
    "gameStatus": "FINISHED"
}
Case 2 : game is not in ongoing status 
status : 409
{
    "errorMessage": "Game is in invalid state",
    "errorCode": "CONFLICT"
}
```

## Extension
This system can further be extended to include following services
1. Replace FCFS allocation algorithm to accomodate other lane allocation algorithms,
2. Get lane wise turn of the players. (next player in the same lane)
3. Currently the system does nothing after putting the game in waiting status, this can be modified by activating game and players when enough lanes are available to be allocated 
4. Currently the system compares scores to declare winner and in case of same scores the player with higher no of strikes wins, this will fail for players with same max score and same no of strikes, thus this needs to be improved.

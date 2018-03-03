# Takenoko v8.0
This is a computerized version of Takenoko Game by Antoine Bauza, played by between 2 and 4 bots.

Basic version plays one game with logs.

At the end of the execution, statistics about the game are displayed on the standard output. 

Program arguments are :
* **--config** <u>CONFIG</u> : Set a configuration for one or several games, followed by arguments. (ex : --config -n 100 -nl -p panda panda --config -n 500 -p parcel parcel)

* **-n** <u>NUMBER</u>: Play the game <u>NUMBER</u> times.
* **--nologs** or **-nl** : Game without logs.
* **--visualize** or **-v** : Visualisation in real time for the game.
* **--interval=** <u>TIME</u> : Time in milliseconds between updates for the viewer. (By default, <u>TIME</u> = 100)
* **--size=** <u>SIZE</u> : Size in pixels of a side of Parcel. (By default, <u>SIZE</u> = 40)
* **--sherlock** or **-s** : Our little easter egg. (sound must be on)
* **--players** or **-p** <u>STRATEGY FOR PLAYER 1</u> <u>STRATEGY FOR PLAYER 2</u> [<u>STRATEGY FOR PLAYER 3</u> <u>STRATEGY FOR PLAYER 4</u>] : Determine the number of players and the strategy they will use, strategies can be :  
**panda**, **gardener**, **parcel**, **random** or **multiple** (not yet implemented) (By default, launch a game with 4 players, one with a panda strategy, one with a gardener strategy, one with a parcel strategy and one with a random strategy)

Currently, the game supports :
* 2 to 4 players.
* Drawing and validating parcel goals, panda goals and gardener goals.
* Drawing and placing irrigations.
* Drawing and placing parcels of all colors and with or without facilities.
* Drawing and validating goals.
* Drawing and placing facilities
* Ending the game with the right number of objectives. (11 minus the number of players).
* The emperor's visit for the first player to finish.
* Moving the panda and the gardener. (and their respective actions)
* Growing bamboos on parcels if the parcel is irrigated and on the neighbouring parcels of the same color.
* The weather dice

#### _**Coded with <3 by the BDSM Team (Bureau De Sebastien Mosser) :**_

* _**Jérémi Ferré**_

* _**Hugo Francois**_

* _**Laura Lopez**_

* _**Prune Pillone**_
# Asteroid Mini-Game

## Context

This is one of the two mini-games that allows students to learn vocabulary and pronunciation in preparation for the main game.

## Status

The mini-game can be started by tapping the associated button on the main menu. The current requirements are met except for including multiple levels in the mini-game. The code should only require a few small modifications to get this requirement running as individual parts of this requirement are functioning well.

## Structure

The following is the structure of `../core/src/asu/gunma/MiniGames`:

```bash
├── MiniGames
│   ├── Controllers
│   │   ├── AsteroidGameController.java
│   │   └── WordScrambleGameController.java
│   ├── Models
│   │   ├── AsteroidGameModel.java
│   │   ├── AsteroidModel.java
│   │   ├── AsteroidPlayerModel.java
│   │   └── WordScrambleGameModel.java
│   ├── Views
│   ├── ├── AsteroidGameView.java
└── └── └── WordScrambleGameView.java
```

The following files in the above structure are files necessary for the asteroid mini-game: AsteroidGameController.java, AsteroidGameModel.java, AsteroidModel.java, AsteroidPlayerModel.java, AsteroidGameView.java

The Model-View-Controller structure is used to allow for the separate organization of the front-end and back-end code. The models contain back-end code and logic, the view contains front-end code, and the controller is used to communicate between the models and view. The models and view NEVER directly rely on each other.


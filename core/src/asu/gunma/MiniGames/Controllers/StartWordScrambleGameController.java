package asu.gunma.MiniGames.Controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;

import java.util.ArrayList;

import asu.gunma.DbContainers.VocabWord;
import asu.gunma.MiniGames.Models.WordScrambleGameModel;
import asu.gunma.MiniGames.Views.WordScrambleGameView;
import asu.gunma.speech.ActionResolver;

public class StartWordScrambleGameController
{
    public WordScrambleGameModel wordScrambleModel;
    public WordScrambleGameController wordScrambleController;
    public WordScrambleGameView wordScrambleView;

    public StartWordScrambleGameController(Game game, ActionResolver speechGDX, Music gameMusic, Preferences prefs, ArrayList<VocabWord> activeVList)
    {
        this.wordScrambleModel = new WordScrambleGameModel(0, activeVList);
        this.wordScrambleController = new WordScrambleGameController(wordScrambleModel);
        wordScrambleView = new WordScrambleGameView(game, speechGDX, gameMusic, game.getScreen(), prefs, wordScrambleController);
        game.setScreen(wordScrambleView);
    }
}

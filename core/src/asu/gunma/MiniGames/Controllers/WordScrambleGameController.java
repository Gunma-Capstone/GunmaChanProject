package asu.gunma.MiniGames.Controllers;

import java.util.ArrayList;

import asu.gunma.DbContainers.VocabWord;
import asu.gunma.MiniGames.Models.WordScrambleGameModel;

public class WordScrambleGameController
{
    private WordScrambleGameModel model;

    public WordScrambleGameController(WordScrambleGameModel model)
    {
        if (model != null)
            this.model = model;
        else
            this.model = new WordScrambleGameModel(0, null);
    }

    // get methods
    public int getScore()
    {
        return model.getScore();
    }

    public ArrayList<VocabWord> getActiveVocabList()
    {
        return model.getActiveVocabList();
    }

    public ArrayList<String> getScrambledList()
    {
        return model.getScrambledList();
    }

    public VocabWord getCurrentVocabWord(int index)
    {
        return model.getActiveVocabList().get(index);
    }

    public String getCurrentScrambledWord(int index)
    {
        return model.getScrambledList().get(index);
    }

    // set methods
    public void setScore(int score)
    {
        model.setScore(score);
    }

    public void setActiveVocabList(ArrayList<VocabWord> activeVocabList)
    {
        model.setActiveVocabList(activeVocabList);
    }

    public void setScrambledList(ArrayList<String> scrambledList)
    {
        model.setScrambledList(scrambledList);
    }

    // other methods
    public int increaseScore()
    {
        return model.increaseScore();
    }
}

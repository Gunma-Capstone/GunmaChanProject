package asu.gunma.MiniGames.Controllers;

import java.util.ArrayList;
import asu.gunma.DbContainers.VocabWord;
import asu.gunma.MiniGames.Models.AsteroidModel;
import asu.gunma.MiniGames.Models.AsteroidGameModel;
import asu.gunma.MiniGames.Models.AsteroidPlayerModel;

public class AsteroidGameController
{
    // private fields
    private AsteroidGameModel model;

    // constructor
    public AsteroidGameController(AsteroidGameModel model)
    {
        if (model != null)
            this.model = model;
        else
            this.model = new AsteroidGameModel(1, 0, 5, null);
    }

    // get methods
    public AsteroidGameModel getModel()
    {
        return model;
    }

    public int getLevel()
    {
        return model.getLevel();
    }

    public int getScore()
    {
        return model.getScore();
    }

    public int getNumLives()
    {
        return model.getNumLives();
    }

    public int getNumAddWordsPerLevel() { return model.getNumAddWordsPerLevel(); }

    public int getMaxLevel() { return model.getMaxLevel(); }

    public ArrayList<VocabWord> getActiveVocabList()
    {
        return model.getActiveVocabList();
    }

    public ArrayList<AsteroidModel> getAsteroidList()
    {
        return model.getAsteroidList();
    }

    public AsteroidPlayerModel getPlayer()
    {
        return model.getPlayer();
    }

    // set methods
    public void setModel(AsteroidGameModel model)
    {
        this.model = model;
    }

    public void setLevel(int level)
    {
        model.setLevel(level);
    }

    public void setScore(int score)
    {
        model.setScore(score);
    }

    public void setNumLives(int numLives)
    {
        model.setNumLives(numLives);
    }

    public void setActiveVocabList(ArrayList<VocabWord> activeVocabList)
    {
        model.setActiveVocabList(activeVocabList);
    }

    public void setAsteroidList(ArrayList<AsteroidModel> asteroidList)
    {
        model.setAsteroidList();
    }

    public void setPlayer(AsteroidPlayerModel player)
    {
        model.setPlayer(player);
    }

    // other methods
    public void transformAsteroidPosition(float delta, int index)
    {
        model.getAsteroidList().get(index).transformPosition(delta);
    }

    public int increaseScore()
    {
        return model.increaseScore();
    }

    public int decreaseNumLives()
    {
        return model.decreaseNumLives();
    }

    public boolean destroyAsteroid(String vocabWord)
    {
        // successfully destroyed the asteroid
        if (model.destroyAsteroid(vocabWord))
            return true;

        //failed to destroy the asteroid
        return false;
    }

    public boolean nextLevel()
    {
        model.nextLevel();
        return true;
    }
}

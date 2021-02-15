package asu.gunma.MiniGames.Models;

import java.util.ArrayList;
import java.util.Collections;

import asu.gunma.DbContainers.VocabWord;

public class AsteroidGameModel
{
    // private fields
    private int level; // current level that the player is on (levels 1 - 5)
    private int score;
    private int numLives;
    private ArrayList<VocabWord> activeVocabList; // list of words that may be included in the mini-game
    private ArrayList<AsteroidModel> asteroidList;
    private AsteroidPlayerModel player;

    // constants
    public static final float MAX_X_POS= 1024f;
    public static final float MAX_Y_POS = 600f;
    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = 5;
    public static final int DEFAULT_LEVEL = 1;
    public static final int MIN_SCORE = 0;
    public static final int DEFAULT_SCORE = 0;
    public static final int MAX_LIVES = 3;
    public static final int MIN_LIVES = 1;
    public static final int DEFAULT_NUM_LIVES = 5;
    public static final int MIN_NUM_ASTEROIDS = 0;
    public static final float DEFAULT_VELOCITY = 1f;
    public static final float DEFAULT_ASTEROID_DIRECTION = 0f;
    public static final float DEFAULT_ASTEROID_X_POS = 0f;
    public static final float DEFAULT_ASTEROID_Y_POS = 0f;
    public static final float DEFAULT_PLAYER_X_POS = MAX_X_POS / 2 - 64;
    public static final float DEFAULT_PLAYER_Y_POS = 8f;
    public static final float DEFAULT_PLAYER_DIRECTION = 0f;
    public static final int SCORE_INCREASE = 1;

    // constructor
    public AsteroidGameModel(int level, int score, int numLives,
                             ArrayList<VocabWord> activeVocabList)
    {
        setLevel(level);
        setScore(score);
        setNumLives(numLives);
        setActiveVocabList(activeVocabList);
        setAsteroidList();
        setPlayer(null);
    }

    // get methods
    public int getScore()
    {
        return score;
    }

    public int getNumLives()
    {
        return numLives;
    }

    public int getLevel()
    {
        return level;
    }

    public ArrayList<VocabWord> getActiveVocabList()
    {
        return activeVocabList;
    }

    public ArrayList<AsteroidModel> getAsteroidList()
    {
        return asteroidList;
    }

    public AsteroidPlayerModel getPlayer()
    {
        return player;
    }

    // set methods
    public void setScore(int score)
    {
        if (score >= MIN_SCORE)
            this.score = score;
        else
            this.score = DEFAULT_SCORE;
    }

    public void setNumLives(int numLives)
    {
        if (numLives >= MIN_LIVES && numLives <= MAX_LIVES)
            this.numLives = numLives;
        else
            this.numLives = DEFAULT_NUM_LIVES;
    }

    public void setLevel(int level)
    {
        if (level >= MIN_LEVEL && level <= MAX_LEVEL)
            this.level = level;
        else
            this.level = DEFAULT_LEVEL;
    }

    public void setActiveVocabList(ArrayList<VocabWord> activeVocabList)
    {
        if (activeVocabList != null)
            this.activeVocabList = activeVocabList;
        else
            this.activeVocabList = new ArrayList<VocabWord>();

        // shuffle the vocab words so the asteroids travel towards the player in a different order
        // in every play
        Collections.shuffle(this.activeVocabList);
    }

    public void setAsteroidList()
    {
        if (asteroidList == null)
        {
            asteroidList = new ArrayList<AsteroidModel>();
        }

        // the number of asteroids sent towards the player must be equal to the level number
        for (int i = 0; i < level; i++)
            addAsteroid();
    }

    public void setPlayer(AsteroidPlayerModel player)
    {
        if (player != null)
            this.player = player;
        else
            this.player = new AsteroidPlayerModel(DEFAULT_PLAYER_X_POS, DEFAULT_PLAYER_Y_POS,
                    DEFAULT_PLAYER_DIRECTION);
    }

    // other methods
    public int increaseScore()
    {
        score += SCORE_INCREASE;
        return score;
    }

    public int decreaseNumLives()
    {
        if (numLives <= MIN_LIVES)
            numLives = 0;
        else
            numLives--;

        return numLives;
    }

    public boolean destroyAsteroid(String vocabWord)
    {
        int index = 0;

        for (index = 0; index < asteroidList.size(); index++)
        {
            // found the asteroid to be destroyed
            if (vocabWord.equals(asteroidList.get(index).getWord().getEngSpelling()))
                break;
            // the asteroid doesn't exist in the current asteroid list
            else if (index == asteroidList.size() - 1)
                return false;
        }

        // remove the asteroid
        asteroidList.remove(index);
        addAsteroid();
        return true;
    }

    private boolean addAsteroid()
    {
        // the asteroid list has space to add an asteroid
        if (asteroidList.size() < level)
        {
            // add first available vocab word from the vocab list
            AsteroidModel asteroid = new AsteroidModel(activeVocabList.get(0),
                    AsteroidModel.DEFAULT_VELOCITY, AsteroidModel.DEFAULT_DIRECTION,
                    AsteroidModel.DEFAULT_X_POS, AsteroidModel.DEFAULT_Y_POS);
            asteroidList.add(asteroid);
            activeVocabList.remove(0);
            return true;
        }

        // asteroid list is full
        return false;
    }
}
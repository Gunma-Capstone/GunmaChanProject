package asu.gunma.MiniGames.Models;

import com.badlogic.gdx.math.Vector2;
import asu.gunma.DbContainers.VocabWord;

public class AsteroidModel
{
    // private fields
    private VocabWord word;
    private float velocity;
    private float direction;
    private Vector2 position;
    private boolean isDisplayed;

    // constants
    public static final float MIN_VELOCITY = 0.01f;
    public static final float MAX_VELOCITY = 100f;
    public static final float DEFAULT_VELOCITY = 40f;
    public static final float MIN_DIRECTION = 0f;
    public static final float MAX_DIRECTION = 2f * (float)Math.PI;
    public static final float DEFAULT_DIRECTION = (float)Math.PI; // down the screen
    public static final float MAX_X_POS= 1024f;
    public static final float MAX_Y_POS = 600f;
    public static final float MIN_X_POS = 0f;
    public static final float MIN_Y_POS = 0f;
    public static final float DEFAULT_X_POS = MAX_X_POS / 2 - 64;
    public static final float DEFAULT_Y_POS = MAX_Y_POS;

    // constructor
    public AsteroidModel(VocabWord word, float velocity, float direction, float xPos, float yPos)
    {
        setWord(word);
        setVelocity(velocity);
        setDirection(direction);
        setPosition(xPos, yPos);
        setIsDisplayed(false);
    }

    // get methods
    public VocabWord getWord()
    {
        return word;
    }

    public float getVelocity()
    {
        return velocity;
    }

    public float getDirection()
    {
        return direction;
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public boolean getIsDisplayed() { return isDisplayed; }

    // set methods
    public void setWord(VocabWord word)
    {
        if (word != null)
            this.word = word;
        else
            this.word = new VocabWord();
    }

    public void setVelocity(float velocity)
    {
        if (velocity >= MIN_VELOCITY && velocity <= MAX_VELOCITY)
            this.velocity = velocity;
        else
            this.velocity = DEFAULT_VELOCITY;
    }

    public void setDirection(float direction)
    {
        if (direction >= MIN_DIRECTION && direction <= MAX_DIRECTION)
            this.direction = direction;
        else
            this.direction = DEFAULT_DIRECTION;
    }

    public void setPosition(float xPos, float yPos)
    {
        if (xPos >= MIN_X_POS && xPos <= MAX_X_POS && yPos >= MIN_Y_POS && yPos <= MAX_Y_POS)
            this.position = new Vector2(xPos, yPos);
        else
            this.position = new Vector2(DEFAULT_X_POS, DEFAULT_Y_POS);
    }

    public void setIsDisplayed(boolean isDisplayed)
    {
        this.isDisplayed = isDisplayed;
    }

    // other methods

    // transforms the position of the asteroid, adding x to the current x-position and y to the current y-position
    public void transformPosition(float deltaTime)
    {
        // movement in the x-direction is different from the y-direction
        this.position = new Vector2(position.x + velocity * (float)Math.sin(direction) * deltaTime,
                position.y + velocity * (float)Math.cos(direction) * deltaTime);
    }
}

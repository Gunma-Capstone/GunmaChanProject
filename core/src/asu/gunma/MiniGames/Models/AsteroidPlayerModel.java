package asu.gunma.MiniGames.Models;

import com.badlogic.gdx.math.Vector2;

public class AsteroidPlayerModel
{
    // private fields
    private Vector2 position;
    private float direction;

    // constants
    public static final float MAX_X_POS= 1024f;
    public static final float MAX_Y_POS = 600f;
    public static final float MIN_X_POS = 0f;
    public static final float MIN_Y_POS = 0f;
    public static final float DEFAULT_X_POS = MAX_X_POS / 2 - 64;
    public static final float DEFAULT_Y_POS = 8f;
    public static final float MIN_DIRECTION = 0f;
    public static final float MAX_DIRECTION = 2f * (float)Math.PI;
    public static final float DEFAULT_DIRECTION = 0f;

    // constructor
    public AsteroidPlayerModel(float xPos, float yPos, float direction)
    {
        setPosition(xPos, yPos);
        setDirection(direction);
    }

    // get methods
    public Vector2 getPosition()
    {
        return position;
    }

    public float getDirection()
    {
        return direction;
    }

    // set methods
    public void setPosition(float xPos, float yPos)
    {
        if (xPos >= MIN_X_POS && xPos <= MAX_X_POS && yPos >= MIN_Y_POS && yPos <= MAX_Y_POS)
            this.position = new Vector2(xPos, yPos);
        else
            this.position = new Vector2(DEFAULT_X_POS, DEFAULT_Y_POS);
    }

    public void setDirection(float direction)
    {
        if (direction >= MIN_DIRECTION && direction <= MAX_DIRECTION)
            this.direction = direction;
        else
            this.direction = DEFAULT_DIRECTION;
    }
}

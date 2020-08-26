package asu.gunma.MiniGames.Views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.DbContainers.VocabWord;
import asu.gunma.MiniGames.Controllers.AsteroidGameController;
import asu.gunma.speech.ActionResolver;
import asu.gunma.ui.screen.menu.MainMenuScreen;
import asu.gunma.ui.util.AssetManagement.GameAssets;
import asu.gunma.ui.util.GradeSystem;
import asu.gunma.ui.util.lives.LivesDrawer;

// Use this class for the front-end of the asteroid mini-game
// You'll want to use the AsteroidGameController class
public class AsteroidGameView implements Screen
{
    private AsteroidGameController controller;
    private GradeSystem gradeSystem;

    private Game game;
    private Music gameMusic;
    public static float masterVolume = 5;
    public ActionResolver speechGDX;
    DbInterface dbCallback;
    private Screen previousScreen;
    ArrayList<VocabWord> activeList;
    private Music incorrectSound;

    // UI variables
    private Stage stage;

    private SpriteBatch batch;

    // textures
    private Texture asteroidTexture;
    private Texture rocketTexture;
    private Texture explosionTexture;

    // fonts
    private BitmapFont buttonFont;
    private BitmapFont asteroidFont;

    FreeTypeFontGenerator generator;
    ArrayList<FreeTypeFontGenerator.FreeTypeFontParameter> parameterList;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter2;
    public Preferences prefs;

    // buttons
    private TextButton backButton;
    private TextButton pauseButton;

    // labels
    private Label youLose;
    private Label youWin;

    private ArrayList<GlyphLayout> wordLayoutList;
    private ArrayList<String> asteroidWordList;

    private LivesDrawer livesDrawer;

    // timers
    private int playerExplosionTimer;
    private int asteroidExplosionTimer;
    private int generateAsteroidTimer;

    // final asteroid positions
    private float finalAsteroidPositionX;
    private float finalAsteroidPositionY;

    private Table winTable;
    private Table loseTable;

    private GameAssets gameAssets;

    private boolean isPaused;
    private int numAsteroidsOnScreen;

    // constants
    private static final int DEFAULT_ASTEROID_SIZE = 128;
    private static final int DEFAULT_EXPLOSION_SIZE = 160;

    public AsteroidGameView(Game game, ActionResolver speechGDX, Music music, DbInterface dbCallback,
                            Screen previous, ArrayList<VocabWord> activeList, Preferences prefs,
                            GameAssets gameAssets, AsteroidGameController controller)
    {
        this.game = game;
        this.speechGDX = speechGDX;
        this.gameMusic = music;
        this.dbCallback = dbCallback;
        this.previousScreen = previous;
        this.activeList = activeList;
        this.prefs = prefs;
        this.gameAssets = gameAssets;
        this.controller = controller;

        //fonts
        generator = new FreeTypeFontGenerator(Gdx.files.internal(gameAssets.fontPath));
        buttonFont = gameAssets.getFont();
        buttonFont.setColor(Color.WHITE);
        asteroidFont = gameAssets.getFont();
        asteroidFont.setColor(Color.BLACK);
        asteroidFont.getData().setScale(0.5f);

        // pictures
        asteroidTexture = new Texture("circle-xxl.png");
        rocketTexture = new Texture("rocket2.png");
        explosionTexture = new Texture("explosion.png");

        // initialize lists
        asteroidWordList = new ArrayList<String>();
        wordLayoutList = new ArrayList<GlyphLayout>();

        // sounds
        incorrectSound = Gdx.audio.newMusic(Gdx.files.internal("incorrect_ohno.mp3"));

        // timers
        playerExplosionTimer = 0;
        asteroidExplosionTimer = 0;
        generateAsteroidTimer = 0;

        // game tracking variables
        isPaused = false;
        numAsteroidsOnScreen = 0;
    }

    // Override Screen class methods
    @Override
    public void show()
    {
        // Black background
        Gdx.gl.glClearColor(0, 0, 0, 1);
        stage = new Stage();
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);
        this.livesDrawer = new LivesDrawer(this.batch);
        loseTable = new Table();
        loseTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        winTable = new Table();
        winTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // for displaying words on asteroids
        for (int i = 0; i < controller.getAsteroidList().size(); i++)
        {
            asteroidWordList.add(controller.getAsteroidList().get(i).getWord().getEngSpelling());
        }

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = buttonFont;
        textButtonStyle.fontColor = Color.WHITE;

        // buttons
        backButton = new TextButton(gameAssets.getResourceBundle().getString("Back"), textButtonStyle);
        backButton.setPosition(Gdx.graphics.getWidth() - 100, 0);
        pauseButton = new TextButton(gameAssets.getResourceBundle().getString("Pause"), textButtonStyle);
        pauseButton.setPosition(Gdx.graphics.getWidth() - 200, 0);

        // return to main menu
        backButton.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                speechGDX.stopRecognition();
                // isPaused = true;
                gameMusic.dispose();
                gameMusic = Gdx.audio.newMusic(Gdx.files.internal(gameAssets.introMusicPath));
                gameMusic.setLooping(false);
                gameMusic.setVolume(masterVolume);
                gameMusic.play();
                game.setScreen(new MainMenuScreen(game, speechGDX, gameMusic, dbCallback,
                        activeList, prefs, gameAssets));
                previousScreen.dispose();
                dispose();
            }
        });

        // pause mini-game
        pauseButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y)
            {
                if (isPaused)
                {
                    try
                    {
                        speechGDX.startRecognition();
                    }
                    catch(Exception e)
                    {
                        System.out.println(e);
                    }

                    isPaused = false;
                }
                else
                {
                    speechGDX.stopRecognition();
                    isPaused = true;
                }
            }
        });

        Label.LabelStyle youLoseStyle = new Label.LabelStyle(buttonFont, Color.WHITE);
        youLose = new Label("YOU LOSE", youLoseStyle);
        youLose.setFontScale(2);

        Label.LabelStyle youWinStyle = new Label.LabelStyle(buttonFont, Color.WHITE);
        youWin = new Label("YOU WIN!", youWinStyle);
        youWin.setFontScale(2);

        stage.addActor(backButton);
        stage.addActor(pauseButton);
        loseTable.add(youLose).padBottom(100);
        loseTable.row();
        winTable.add(youWin).padBottom(100);
        winTable.row();

        // initiate speech recognition
        try {
            speechGDX.startRecognition();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        // asteroids do not move when the game is paused
        if (!isPaused)
            transformAsteroids(delta);

        // ************************************* BEGIN BATCH ***************************************
        batch.begin();

        // show the game only if the player still has lives and there are vocabulary words remaining
        if (controller.getNumLives() > 0 && controller.getActiveVocabList().size() > 0)
        {
            // display lives and score
            this.livesDrawer.render();
            buttonFont.draw(batch, "Score: " + controller.getScore(),
                    Gdx.graphics.getWidth() - 160, Gdx.graphics.getHeight() - 16);

            // allows the correct number of asteroids to be displayed on screen at specific time
            // intervals so the asteroids aren't cluttered together
            for (int i = 0; i < controller.getAsteroidList().size(); i++) {
                if (generateAsteroidTimer >= i * 400) {
                    if (!controller.getAsteroidList().get(i).getIsDisplayed()) {
                        controller.getAsteroidList().get(i).setIsDisplayed(true);
                    }

                    batch.draw(asteroidTexture, controller.getAsteroidList().get(i).getPosition().x,
                            controller.getAsteroidList().get(i).getPosition().y, DEFAULT_ASTEROID_SIZE,
                            DEFAULT_ASTEROID_SIZE);
                    asteroidFont.draw(batch,
                            controller.getAsteroidList().get(i).getWord().getEngSpelling(),
                            controller.getAsteroidList().get(i).getPosition().x + 8,
                            controller.getAsteroidList().get(i).getPosition().y
                                    + 2 * DEFAULT_ASTEROID_SIZE / 3,
                            DEFAULT_ASTEROID_SIZE - 16, 1, true);
                }
                System.out.println(generateAsteroidTimer);
                generateAsteroidTimer++;
            }

            batch.draw(rocketTexture, controller.getPlayer().getPosition().x,
                    controller.getPlayer().getPosition().y, DEFAULT_ASTEROID_SIZE,
                    DEFAULT_ASTEROID_SIZE);

            // continue to show rocket explosion
            if (playerExplosionTimer > 0) {
                batch.draw(explosionTexture, controller.getPlayer().getPosition().x,
                        controller.getPlayer().getPosition().y, DEFAULT_EXPLOSION_SIZE,
                        DEFAULT_EXPLOSION_SIZE);

                playerExplosionTimer++;
            }

            // continue to show asteroid explosion
            if (asteroidExplosionTimer > 0)
            {
                batch.draw(explosionTexture, finalAsteroidPositionX, finalAsteroidPositionY,
                        DEFAULT_EXPLOSION_SIZE, DEFAULT_EXPLOSION_SIZE);

                asteroidExplosionTimer++;
            }
        }
        else if (controller.getNumLives() <= 0)
        {
            stage.addActor(loseTable);
            stage.draw();
        }
        else if (controller.getActiveVocabList().size() <= 0 && controller.getNumLives() > 0)
        {
            stage.addActor(winTable);
            stage.draw();
        }

        batch.end();
        // ************************************** END BATCH ****************************************

        if (controller.getNumLives() > 0 && controller.getActiveVocabList().size() > 0)
        {
            String spokenWord = speechGDX.getWord();

            ArrayList<String> cWordsList = new ArrayList<String>();
            ArrayList<String[]> correctWordsList = new ArrayList<String[]>();
            ArrayList<Boolean> correctList = new ArrayList<Boolean>();

            for (int i = 0; i < controller.getAsteroidList().size(); i++)
            {
                if (controller.getAsteroidList().get(i).getIsDisplayed())
                {
                    cWordsList.add(controller.getAsteroidList().get(i).getWord().getCorrectWords());
                }
                else
                {
                    cWordsList.add("");
                }
            }

            for (int i = 0; i < controller.getAsteroidList().size(); i++)
            {
                if (controller.getAsteroidList().get(i).getIsDisplayed())
                {
                    correctWordsList.add(cWordsList.get(i).split("\\s*,\\s*"));
                }
                else
                {
                    correctWordsList.add(new String[0]);
                }
            }

            for (int i = 0; i < controller.getAsteroidList().size(); i++)
            {
                if (controller.getAsteroidList().get(i).getIsDisplayed())
                {
                    correctList.add(gradeSystem.grade(correctWordsList.get(i), spokenWord));
                }
                else
                {
                    correctList.add(false);
                }
            }

            for (int i = 0; i < controller.getAsteroidList().size(); i++)
            {
                if (correctList.get(i).booleanValue() == true)
                {
                    // print statements are for debugging
                    System.out.println("Correct!");


                    // get the final position of the asteroid to trigger explosion in that position
                    finalAsteroidPositionX = controller.getAsteroidList().get(i).getPosition().x;
                    finalAsteroidPositionY = controller.getAsteroidList().get(i).getPosition().y;

                    if (controller.destroyAsteroid(spokenWord))
                    {
                        System.out.println("Successfully destroyed the asteroid and added a new one to " +
                                "the screen.");

                        controller.increaseScore();

                        asteroidWordList.remove(i);
                        asteroidWordList.add(controller.getLevel() - 1,
                                controller.getAsteroidList().get(controller.getLevel() - 1).getWord().getEngSpelling());
                    }
                    else
                        System.out.println("Failed to destroy asteroid.");

                    asteroidExplosionTimer++;

                    break;
                }

                // asteroid colliding with rocket
                if (controller.getAsteroidList().get(i).getPosition().y < 140)
                {
                    System.out.println("Incorrect!");
                    incorrectSound.setLooping(false);
                    incorrectSound.setVolume(masterVolume);
                    incorrectSound.play();
                    livesDrawer.takeLife();
                    controller.decreaseNumLives();
                    controller.destroyAsteroid(controller.getAsteroidList().get(i).getWord().getEngSpelling());

                    // initiate rocket explosion
                    playerExplosionTimer++;
                }

                if (playerExplosionTimer > 60)
                {
                    playerExplosionTimer = 0;
                }
                System.out.println(playerExplosionTimer);

                if (asteroidExplosionTimer > 60)
                {
                    asteroidExplosionTimer = 0;
                }
            }
        }
    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {
        buttonFont.dispose();
        batch.dispose();
        stage.dispose();
    }

    // other methods
    private void transformAsteroids(float delta)
    {
        for (int i = 0; i < controller.getAsteroidList().size(); i++)
        {
            // only account for asteroids that are currently displayed
            if (controller.getAsteroidList().get(i).getIsDisplayed())
            {
                // transforms asteroid according to the change in time, the asteroid's velocity,
                // direction, etc.
                controller.getAsteroidList().get(i).transformPosition(delta);
            }
        }
    }
}

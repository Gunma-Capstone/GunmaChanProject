package asu.gunma.MiniGames.Views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.DbContainers.VocabWord;
import asu.gunma.MiniGames.Controllers.AsteroidGameController;
import asu.gunma.MiniGames.Controllers.WordScrambleGameController;
import asu.gunma.MiniGames.Models.AsteroidGameModel;
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

    private ArrayList<GlyphLayout> wordLayoutList;
    private ArrayList<String> asteroidWordList;

    private LivesDrawer livesDrawer;

    private int explosionTimer;

    private Table table;

    private GameAssets gameAssets;

    boolean isPaused;

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

        explosionTimer = 0;
        isPaused = false;
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
        table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

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

        stage.addActor(backButton);
        stage.addActor(pauseButton);
        table.add(youLose).padBottom(100);
        table.row();

        // initiate speech recognition
        try {
            speechGDX.startRecognition();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        // asteroids do not move when the game is paused
        if (!isPaused)
            transformAsteroids(delta);

        // ************************************* BEGIN BATCH ***************************************
        batch.begin();

        // show the game only if the player still has lives
        if (controller.getNumLives() > 0)
        {
            // display lives and score
            this.livesDrawer.render();
            buttonFont.draw(batch,"Score: " + controller.getScore(),
                    Gdx.graphics.getWidth() - 160, Gdx.graphics.getHeight() - 16);

            for (int i = 0; i < controller.getAsteroidList().size(); i++)
            {
                batch.draw(rocketTexture, controller.getPlayer().getPosition().x,
                        controller.getPlayer().getPosition().y, DEFAULT_ASTEROID_SIZE,
                        DEFAULT_ASTEROID_SIZE);
                batch.draw(asteroidTexture, controller.getAsteroidList().get(0).getPosition().x,
                        controller.getAsteroidList().get(0).getPosition().y, DEFAULT_ASTEROID_SIZE,
                        DEFAULT_ASTEROID_SIZE);
                asteroidFont.draw(batch,
                        controller.getAsteroidList().get(0).getWord().getEngSpelling(),
                        controller.getAsteroidList().get(0).getPosition().x + 8,
                        controller.getAsteroidList().get(0).getPosition().y
                                + 2 * DEFAULT_ASTEROID_SIZE / 3,
                        DEFAULT_ASTEROID_SIZE - 16, 1, true);
            }

            // continue to show explosion
            if (explosionTimer > 0)
            {
                batch.draw(explosionTexture, controller.getPlayer().getPosition().x,
                        controller.getPlayer().getPosition().y, DEFAULT_EXPLOSION_SIZE,
                        DEFAULT_EXPLOSION_SIZE);

                explosionTimer++;
            }
        }
        else
        {
            stage.dispose();
            stage.addActor(table);
            stage.draw();
        }

        batch.end();
        // ************************************** END BATCH ****************************************

        String spokenWord = speechGDX.getWord();
        String cWords = controller.getAsteroidList().get(0).getWord().getCorrectWords();
        //System.out.println(cWords);
        String[] correctWords = cWords.split("\\s*,\\s*");
        boolean correct = gradeSystem.grade(correctWords, spokenWord);

        if (correct)
        {
            // print statements are for debugging
            System.out.println("Correct!");

            if (controller.destroyAsteroid(spokenWord))
            {
                System.out.println("Successfully destroyed the asteroid and added a new one to " +
                        "the screen.");

                controller.increaseScore();

                asteroidWordList.remove(0);
                asteroidWordList.add(0, controller.getAsteroidList().get(0).getWord().getEngSpelling());
            }
            else
                System.out.println("Failed to destroy asteroid.");


        }
        // asteroid has reached rocket
        else if (controller.getAsteroidList().get(0).getPosition().y < 140)
        {
            System.out.println("Incorrect!");
            incorrectSound.setLooping(false);
            incorrectSound.setVolume(masterVolume);
            incorrectSound.play();
            livesDrawer.takeLife();
            controller.decreaseNumLives();
            controller.destroyAsteroid(controller.getAsteroidList().get(0).getWord().getEngSpelling());

            // initiate rocket explosion
            explosionTimer++;
        }

        if (explosionTimer > 60)
        {
            explosionTimer = 0;
        }
        System.out.println(explosionTimer);

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
            // transforms asteroid according to the change in time, the asteroid's velocity,
            // direction, etc.
            controller.getAsteroidList().get(i).transformPosition(delta);
        }
    }
}

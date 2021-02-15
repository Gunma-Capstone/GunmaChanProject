package asu.gunma.ui.screen.menu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.audio.Music;

import java.util.ArrayList;

import asu.gunma.MiniGames.Controllers.AsteroidGameController;
import asu.gunma.MiniGames.Controllers.StartWordScrambleGameController;
import asu.gunma.MiniGames.Controllers.WordScrambleGameController;
import asu.gunma.MiniGames.Models.AsteroidGameModel;
import asu.gunma.MiniGames.Models.WordScrambleGameModel;
import asu.gunma.MiniGames.Views.AsteroidGameView;
import asu.gunma.MiniGames.Views.WordScrambleGameView;
import asu.gunma.ui.util.Animator;
import asu.gunma.ui.util.BackgroundDrawer;
import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.DbContainers.VocabWord;
import asu.gunma.speech.ActionResolver;
import asu.gunma.ui.screen.game.FlashcardScreen;
import asu.gunma.ui.screen.game.MountainScreen;
import asu.gunma.ui.util.AssetManagement.GameAssets;

public class MainMenuScreen implements Screen {

    // This is needed to change screens later on
    private Game game;
    public ActionResolver speechGDX;
    public DbInterface dbCallback;
    public Music gameMusic;
    public static float masterVolume = 5;
    public ArrayList<VocabWord> activeVList = new ArrayList<>();
    private GameAssets gameAssets;
    private final int SCREEN_BOTTOM_ADJUST = 35;

    // Using these are unnecessary but will make our lives easier.
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Table table;
    private Texture background;

    private int testInt = 0;

    /*
        We will need 5 different buttons for this menu:
          1. Video Tutorials
          2. Flashcards
          3. Game #1
          4. Game #2
          5. Game #3
        This is based on the Project Proposal, I'd like to change this
        before the final release.
     */
    private TextButton buttonTutorial, buttonFlashcard, buttonGameFirst, buttonGameSecond, buttonGameThird, buttonSettings;

    private SpriteBatch batch;
    private Animator onionWalkAnimation;
    private Animator gunmaWalkAnimation;
    private BackgroundDrawer backgroundDrawer;

    private BitmapFont font = new BitmapFont();
    private Label heading;

    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter2;
    public Preferences prefs;

    public MainMenuScreen(Game game, ActionResolver speechGDX, Music music, DbInterface dbCallback, ArrayList<VocabWord> activeList, Preferences prefs, GameAssets gameAssets) {
        this.game = game;
        this.prefs = prefs;
        this.speechGDX = speechGDX;
        this.gameMusic = music;
        this.dbCallback = dbCallback;
        this.activeVList = activeList;
        this.gameAssets = gameAssets;
    }

    @Override
    public void show() {
        //font file
//        generator = new FreeTypeFontGenerator(Gdx.files.internal(gameAssets.fontPath));
        final String FONT_PATH = "irohamaru-mikami-Regular.ttf";

        generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_PATH));

        //font for vocab word
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        //font for other words
        parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();

        font = gameAssets.getFont();

        Color bgColor = gameAssets.backgroundColor;
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
        stage = new Stage();

        batch = new SpriteBatch();

        background = new Texture(gameAssets.backgroundImagePath);
        backgroundDrawer = new BackgroundDrawer(this.batch, this.SCREEN_BOTTOM_ADJUST, gameAssets);
        this.onionWalkAnimation = new Animator(gameAssets.onionWalkAnimationPath, 4, 2, 0.1f);
        this.gunmaWalkAnimation = new Animator(gameAssets.gunmaWalkAnimationActive, 8, 1, 0.1f);

        Gdx.input.setInputProcessor(stage);

        // Defining the regions of sprite image we're going to create
        //atlas = new TextureAtlas("ui/button.pack"); // ???
        //skin = new Skin(atlas);

        table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Skin skin = gameAssets.getColorSkin(gameAssets.color2, "color2");
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        //textButtonStyle.up = skin.getDrawable("button.up");
        //textButtonStyle.down = skin.getDrawable("button.down");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font;
        textButtonStyle.up = skin.newDrawable("color2", gameAssets.color2);

        // IMPORTANT: needs localization support
        buttonTutorial = new TextButton(gameAssets.getResourceBundle().getString("VideoTutorials"), textButtonStyle);
        buttonFlashcard = new TextButton(gameAssets.getResourceBundle().getString("Flashcards"), textButtonStyle);
        buttonGameFirst = new TextButton(gameAssets.getResourceBundle().getString("GameName"), textButtonStyle);
        buttonGameSecond = new TextButton(gameAssets.getResourceBundle().getString("Asteroids"), textButtonStyle);
        buttonGameThird = new TextButton(gameAssets.getResourceBundle().getString("WordScramble"), textButtonStyle);
        buttonSettings = new TextButton(gameAssets.getResourceBundle().getString("Settings"), textButtonStyle);
      //   IMPORTANT: needs localization support
        buttonTutorial = new TextButton(gameAssets.getResourceBundle().getString("VideoTutorials"), textButtonStyle);
        buttonFlashcard = new TextButton(gameAssets.getResourceBundle().getString("Flashcards"), textButtonStyle);
        buttonGameFirst = new TextButton(gameAssets.getResourceBundle().getString("GameName"), textButtonStyle);
        buttonGameSecond = new TextButton(gameAssets.getResourceBundle().getString("Asteroids"), textButtonStyle);
        buttonGameThird = new TextButton(gameAssets.getResourceBundle().getString("WordScramble"), textButtonStyle);
        buttonSettings = new TextButton(gameAssets.getResourceBundle().getString("Settings"), textButtonStyle);

        // Actually, should probably custom class this process
        buttonTutorial.pad(15);
        buttonFlashcard.pad(15);
        buttonGameFirst.pad(15);
        buttonGameSecond.pad(15);
        buttonGameThird.pad(15);
        buttonSettings.pad(15);


            /*
                If you want to test functions with UI instead of with console,
                add it into one of these Listeners. Each of them correspond to
                one of the buttons on the screen in top-down order.
             */
        buttonTutorial.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                testInt++;
                System.out.println(testInt);
            }
        });
        buttonFlashcard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameMusic.pause();
                gameMusic.dispose();
                //play flashcard music
                //gameMusic = new Music
                game.setScreen(new FlashcardScreen(game, speechGDX, gameMusic, dbCallback, game.getScreen(), activeVList, prefs, gameAssets));
            }
        });
        buttonGameFirst.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameMusic.pause();
                gameMusic.dispose();
                //play GameFirst music
                // gameMusic = new Music
                game.setScreen(new MountainScreen(game, speechGDX, gameMusic, dbCallback, game.getScreen(), activeVList, prefs, gameAssets));

            }
        });

        buttonGameSecond.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AsteroidGameModel asteroidGameModel = new AsteroidGameModel(1, 0, 5, activeVList);
                AsteroidGameController asteroidController = new AsteroidGameController(asteroidGameModel);
                AsteroidGameView asteroidView = new AsteroidGameView(game, speechGDX, gameMusic,
                        dbCallback, game.getScreen(), activeVList, prefs, gameAssets,
                        asteroidController);
                game.setScreen(asteroidView);
//                gameMusic.pause();
//                gameMusic.dispose();
//                game.setScreen(new GameScreen(game, speechGDX, gameMusic, dbCallback, game.getScreen(), activeVList, prefs, gameAssets));
            }
        });

        buttonGameThird.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //StartWordScrambleGameController startWordScrambleGameController = new StartWordScrambleGameController(game, speechGDX, gameMusic, prefs, activeVList);
                WordScrambleGameModel wordScrambleModel = new WordScrambleGameModel(0, activeVList);
                WordScrambleGameController wordScrambleController = new WordScrambleGameController(wordScrambleModel);
                WordScrambleGameView wordScrambleView = new WordScrambleGameView(game, speechGDX, gameMusic, game.getScreen(), prefs, wordScrambleController);
                game.setScreen(wordScrambleView);
//                gameMusic.pause();
//                gameMusic.dispose();
//                game.setScreen(new GameScreen(game, speechGDX, gameMusic, dbCallback, game.getScreen(), activeVList, prefs, gameAssets));

            }
        });

        buttonSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //testing sign in method when option menu is selected
                gameMusic.pause();
                gameMusic.dispose();
                //play OptionMenu music
                //gameMusic = new Music
                game.setScreen(new SettingsScreen(game, speechGDX, gameMusic, dbCallback, game.getScreen(), activeVList, prefs, gameAssets));
            }
        });


        table.add(buttonTutorial).padBottom(15);
        table.row();
        table.add(buttonFlashcard).padBottom(15);
        table.row();
        table.add(buttonGameFirst).padBottom(15);
        table.row();
        table.add(buttonGameSecond).padBottom(15);
        table.row();
        table.add(buttonGameThird).padBottom(15);
        table.row();
        table.add(buttonSettings);
        table.row();

        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // SpriteBatch is resource intensive, try to use it for only brief moments
        batch.begin();
        backgroundDrawer.render(false,false);
        batch.draw(this.gunmaWalkAnimation.getCurrentFrame(delta), 60, 35 + this.SCREEN_BOTTOM_ADJUST);
//        batch.draw(this.gunmaWalkAnimation.getCurrentFrame(delta), 200, 35 + this.SCREEN_BOTTOM_ADJUST);
        batch.end();

        stage.act(delta); // optional to pass delta value
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        gameMusic.pause();
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();
        stage.dispose();
    }

}


package asu.gunma.ui.screen.menu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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

import java.awt.Font;
import java.util.ArrayList;

import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.DbContainers.VocabWord;
import asu.gunma.speech.ActionResolver;
import asu.gunma.ui.screen.game.FlashcardScreen;
import asu.gunma.ui.screen.game.GameScreen;
import asu.gunma.ui.util.Animator;
import asu.gunma.ui.util.AssetManagement.GameAssets;
import asu.gunma.ui.util.BackgroundDrawer;

public class LeaderboardScreen implements Screen {

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
    private Label buttonTutorial, buttonFlashcard, buttonGameFirst, buttonGameSecond, buttonGameThird, buttonLeaderboard, buttonSettings;
    private TextButton backButton;

    private SpriteBatch batch;
    private Animator onionWalkAnimation;
    private Animator gunmaWalkAnimation;
    private BackgroundDrawer backgroundDrawer;

    private BitmapFont font;
    private Label heading;
    private int levelNumber;

    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter2;
    public Preferences prefs;

    public LeaderboardScreen(Game game, ActionResolver speechGDX, Music music, DbInterface dbCallback, ArrayList<VocabWord> activeList, Preferences prefs, GameAssets gameAssets, int levelNumber) {
        this.game = game;
        this.prefs = prefs;
        this.speechGDX = speechGDX;
        this.gameMusic = music;
        this.dbCallback = dbCallback;
        this.activeVList = activeList;
        this.gameAssets = gameAssets;
        this.levelNumber = levelNumber;
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

        background = new Texture("BG_temp.png");
        backgroundDrawer = new BackgroundDrawer(this.batch, this.SCREEN_BOTTOM_ADJUST, gameAssets);
        this.onionWalkAnimation = new Animator("onion_sheet.png", 4, 2, 0.1f);
        this.gunmaWalkAnimation = new Animator("gunma_sheet.png", 8, 1, 0.1f);

        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Skin skin = gameAssets.getColorSkin(gameAssets.color2, "color2");
        Label.LabelStyle itemStyle = new Label.LabelStyle();
        //itemStyle.up = skin.getDrawable("button.up");
        //itemStyle.down = skin.getDrawable("button.down");
        itemStyle.font = font;
        itemStyle.fontColor = Color.BLACK;
//        itemStyle.background = skin.newDrawable("color2", gameAssets.color2);

//        Label title = new Label(gameAssets.getResourceBundle().getString("Leaderboard"), itemStyle);

        for (int i = 0; i < 10; i++) {
            table.add(new Label((i + 1) + "", itemStyle)).padBottom(15).expandX();
            table.add(new Label(gameAssets.getLeaderboardNickname(i + 1, levelNumber), itemStyle)).padBottom(15).expandX();
            table.add(new Label(gameAssets.getLeaderboardScore(i + 1, levelNumber) + "", itemStyle)).padBottom(15).expandX();
            table.row();
        }

        stage.addActor(table);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.BLACK;

        backButton = new TextButton(gameAssets.getResourceBundle().getString("Back"), textButtonStyle);
        backButton.setPosition(Gdx.graphics.getWidth() - 100, 0);

        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                speechGDX.stopRecognition();
                gameMusic.dispose();
                gameMusic = Gdx.audio.newMusic(Gdx.files.internal(gameAssets.introMusicPath));
                gameMusic.setLooping(false);
                gameMusic.setVolume(masterVolume);
                gameMusic.play();
                game.setScreen(new MainMenuScreen(game, speechGDX,  gameMusic, dbCallback,activeVList, prefs, gameAssets));
                dispose(); // dispose of current GameScreen
            }
        });

        stage.addActor(backButton);


//        table.add(buttonTutorial).padBottom(15);
//        table.row();
//        table.add(buttonFlashcard).padBottom(15);
//        table.row();
//        table.add(buttonGameFirst).padBottom(15);
//        table.row();
//        table.add(buttonGameSecond).padBottom(15);
//        table.row();
//        table.add(buttonGameThird).padBottom(15);
//        table.row();
//        table.add(buttonSettings);
//        table.row();
//
//        stage.addActor(table);

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


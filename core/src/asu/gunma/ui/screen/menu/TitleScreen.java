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

import asu.gunma.ui.util.Animator;
import asu.gunma.ui.util.BackgroundDrawer;
import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.DbContainers.VocabWord;
import asu.gunma.speech.ActionResolver;
import asu.gunma.ui.screen.game.FlashcardScreen;
import asu.gunma.ui.screen.game.GameScreen;
import asu.gunma.ui.util.AssetManagement.GameAssets;

public class TitleScreen implements Screen {

    // This is needed to change screens later on
    private Game game;
    public ActionResolver speechGDX;
    public DbInterface dbCallback;
    public Music gameMusic;
    private final int SCREEN_BOTTOM_ADJUST = 35;

    // Using these are unnecessary but will make our lives easier.
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Table table;
    public static float masterVolume = 10;
    public ArrayList<VocabWord> activeVList;
    private GameAssets gameAssets;
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
    private TextButton buttonTutorial, buttonFlashcard, buttonGameFirst, buttonGameSecond, buttonGameThird;

    private SpriteBatch batch;
    private Animator onionWalkAnimation;
    private Animator placeholderAnimation1;
    private Animator placeholderAnimation2;
    private Animator placeholderAnimation3;
    private Animator placeholderAnimation4;
    private Animator gunmaWalkAnimation;
    private BackgroundDrawer backgroundDrawer;

    private BitmapFont font;
    private Label heading;

    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter2;
    public Preferences prefs;

    public TitleScreen(Game game, ActionResolver speechGDX, DbInterface dbCallback, Music music, ArrayList<VocabWord> arrayList, Preferences prefs, GameAssets gameAssets) {

        this.game = game;
        this.prefs = prefs;
        this.speechGDX = speechGDX;
        this.dbCallback = dbCallback;
        this.gameMusic = music;
        this.activeVList = arrayList;
        this.gameAssets = gameAssets;

        //font file
//        generator = new FreeTypeFontGenerator(Gdx.files.internal(gameAssets.fontPath));
        final String FONT_PATH = "irohamaru-mikami-Regular.ttf";

        generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_PATH));

        //font for vocab word
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        //font for other words
        parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();

        font = gameAssets.getFont();
    }

    @Override
    public void show() {
        Color bgColor = gameAssets.backgroundColor;
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
        stage = new Stage();

        batch = new SpriteBatch();

        background = new Texture(gameAssets.backgroundImagePath);
        backgroundDrawer = new BackgroundDrawer(this.batch, this.SCREEN_BOTTOM_ADJUST, gameAssets);

        // Update these to other frenemy sprite sheets once created
        this.placeholderAnimation1 = new Animator(gameAssets.placeholderFrenemyAnimation1Path, 4, 2, 0.1f);
        this.placeholderAnimation2 = new Animator(gameAssets.placeholderFrenemyAnimation2Path, 4, 2, 0.1f);
        this.placeholderAnimation3 = new Animator(gameAssets.placeholderFrenemyAnimation3Path, 4, 2, 0.1f);
        this.placeholderAnimation4 = new Animator(gameAssets.placeholderFrenemyAnimation4Path, 4, 2, 0.1f);

        this.onionWalkAnimation = new Animator(gameAssets.onionWalkAnimationPath, 4, 2, 0.1f);
        this.gunmaWalkAnimation = new Animator(gameAssets.gunmaWalkAnimationPath, 8, 1, 0.1f);

        Gdx.input.setInputProcessor(stage);

        // Defining the regions of sprite image we're going to create
        //atlas = new TextureAtlas("ui/button.pack"); // ???
        //skin = new Skin(atlas);

        table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        //textButtonStyle.up = skin.getDrawable("button.up");
        //textButtonStyle.down = skin.getDrawable("button.down");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.BLACK;

        // IMPORTANT: needs localization support
        buttonTutorial = new TextButton(gameAssets.getResourceBundle().getString("Start"), textButtonStyle);

        Label.LabelStyle headingStyle = new Label.LabelStyle(font, Color.BLACK);
        //

        heading = new Label(gameAssets.getResourceBundle().getString("GameName"), headingStyle);
        heading.setFontScale(2);
        //

        // Actually, should probably custom class this process
        buttonTutorial.pad(20);

            /*
                If you want to test functions with UI instead of with console,
                add it into one of these Listeners. Each of them correspond to
                one of the buttons on the screen in top-down order.
             */
        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Going from TitleScreen to MainMenuScreen");
                game.setScreen(new MainMenuScreen(game, speechGDX, gameMusic, dbCallback, activeVList, prefs, gameAssets));
            }
        });

        table.add(heading).padBottom(100);
        table.row();
//        table.add(buttonTutorial);
//        table.row();

        // Remove this later
        //table.debug();

        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // SpriteBatch is resource intensive, try to use it for only brief moments
        batch.begin();
        backgroundDrawer.render(false,false);

        // ONCE WE HAVE ANIMATIONS FOR OTHER FRENEMIES - UNCOMMENT THIS
//        batch.draw(this.placeholderAnimation1.getCurrentFrame(delta), 40, 35 + this.SCREEN_BOTTOM_ADJUST);
//        batch.draw(this.placeholderAnimation2.getCurrentFrame(delta), 180, 35 + this.SCREEN_BOTTOM_ADJUST);
//        batch.draw(this.placeholderAnimation3.getCurrentFrame(delta), 320, 35 + this.SCREEN_BOTTOM_ADJUST);
//        batch.draw(this.placeholderAnimation4.getCurrentFrame(delta), 460, 35 + this.SCREEN_BOTTOM_ADJUST);
//        batch.draw(this.onionWalkAnimation.getCurrentFrame(delta), 600, 35 + this.SCREEN_BOTTOM_ADJUST);
//        batch.draw(this.gunmaWalkAnimation.getCurrentFrame(delta), 740, 35 + this.SCREEN_BOTTOM_ADJUST);

        batch.draw(this.onionWalkAnimation.getCurrentFrame(delta), 40, 35 + this.SCREEN_BOTTOM_ADJUST);
        batch.draw(this.gunmaWalkAnimation.getCurrentFrame(delta), 180, 35 + this.SCREEN_BOTTOM_ADJUST);
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


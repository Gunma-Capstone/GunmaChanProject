package asu.gunma.ui.screen.game;

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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.DbContainers.VocabWord;
import asu.gunma.speech.ActionResolver;
import asu.gunma.ui.util.AssetManagement.GameAssets;
import asu.gunma.ui.screen.menu.MainMenuScreen;
import asu.gunma.ui.util.Animator;
import asu.gunma.ui.util.BackgroundDrawer;
import asu.gunma.ui.util.GradeSystem;
import asu.gunma.ui.util.lives.LivesDrawer;

public class GameScreen implements Screen {
    //size of round word list
    private int GAME_LIST_SIZE;
    private int currentWordIndex;

    private final int SCREEN_BOTTOM_ADJUST = 35;
    private final int CORRECT_DISPLAY_DURATION = 20;
    private final int INCORRECT_DISPLAY_DURATION = 20;

    private TextButton testButton;
    private int correctDisplayTimer;
    private int incorrectDisplayTimer;

    DbInterface dbCallback;
    private Game game;
    private Music gameMusic;
    private Music correctSound;
    private Music incorrectSound;
    private Music gameOverSound;
    private Music evilLaugh;
    private Music threeRow;
    public static float masterVolume = 5;
    public ActionResolver speechGDX;
    private Screen previousScreen;

    // Game logic variables
    private int score = 0;
    private int listCounter = 0;
    private String displayWord;
    private int row = 0;
    private List<VocabWord> dbListWords;
    public ArrayList<VocabWord> activeVList;
    private GameAssets gameAssets;

    // Using these are unnecessary but will make our lives easier.
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Table table;
    private SpriteBatch batch;

    private int lives;
    private float enemyPosition;
    private float happyPosition;
    private float sweetRollLocation;
    private float gameOverPos;
    private float satisfiedOnion;

    private boolean isGameOver;
    private boolean willDisappear;

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
    private TextButton pauseButton;
    private TextButton backButton;

    private BitmapFont font = new BitmapFont();
    private BitmapFont font2 = new BitmapFont();

    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter2;

    private Texture gunmaSprite;
    private Texture happyOnion;
    // private Texture onionWithBag;
    private Texture gunmaFaintedSprite;
    private Texture onionIdleSprite;
    private Texture background;
    private Texture correctSprite;
    private Texture incorrectSprite;
    private Texture sweetRoll;

    private GlyphLayout displayWordLayout;
    private int targetWidth = 400;

    // Animation declarations
    private Animator onionWalkAnimation;
    private Animator gunmaWalkAnimation;
    private Animator onionHungryWalkAnimation;
    private Animator onionStealAnimation;
    private Animator onionSatisfiedAnimation;

    private BackgroundDrawer backgroundDrawer;
    private LivesDrawer livesDrawer;

    boolean isPaused = false;
    boolean check = false;
    boolean endCheck = false;

    private GradeSystem gradeSystem;
    String incomingWord = null;
    boolean correct = false;
    boolean win = false;
    String cWords;
    String[] correctWordList;

    ArrayList<VocabWord> gameWords = new ArrayList<>();
    Random rand = new Random();

    Preferences prefs;
    int levelNumber;
    int numStars = 0;

    public GameScreen(Game game, ActionResolver speechGDX, Music music, DbInterface dbCallback, Screen previous, ArrayList<VocabWord> activeList, Preferences prefs, GameAssets gameAssets, int levelNumber) {
        this.game = game;
        this.prefs = prefs;
        this.speechGDX = speechGDX;
        this.dbCallback = dbCallback;
        this.previousScreen = previous;
        this.gameMusic = music;
        this.activeVList = activeList;
        this.gameAssets = gameAssets;
        this.levelNumber = levelNumber;
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal(gameAssets.introMusicPath));
        gameMusic.setLooping(false);
        gameMusic.setVolume(masterVolume);
        gameMusic.play();
    }

    @Override
    public void show() {
        gameWords = new ArrayList<>();
        // gameMusic.play();
        this.correctDisplayTimer = 0;
        this.incorrectDisplayTimer = 0;

        Gdx.gl.glClearColor(.8f, 1, 1, 1);
        stage = new Stage();

        batch = new SpriteBatch();
        // this.onionWithBag = new Texture("FrenemyWithBag.png");
       gunmaSprite = new Texture(gameAssets.gunmaSpritePath);
        this.gunmaFaintedSprite = new Texture(gameAssets.gunmaFaintedSpritePath);
      //  this.happyOnion = new Texture("happyVeggie.png");
        this.sweetRoll = new Texture(gameAssets.sweetRoll);
     //   onionIdleSprite = new Texture("");

        background = new Texture(gameAssets.backgroundImagePath);
        backgroundDrawer = new BackgroundDrawer(this.batch, this.SCREEN_BOTTOM_ADJUST, gameAssets);
        this.livesDrawer = new LivesDrawer(this.batch);

        // Animation initializations
        this.onionWalkAnimation = new Animator(gameAssets.frenemyWalkAnimationPathPerLevel[levelNumber - 1], 4, 2, 0.1f);
        this.gunmaWalkAnimation = new Animator(gameAssets.gunmaWalkAnimationActive, 8, 1, 0.1f);
        this.onionHungryWalkAnimation = new Animator(gameAssets.onionHungryWalkAnimation, 4, 2, 0.1f);
        this.onionStealAnimation = new Animator(gameAssets.onionStealAnimation, 4, 2, 0.1f);
        this.onionSatisfiedAnimation = new Animator(gameAssets.onionSatisfiedAnimation, 4, 2, 0.1f);

        // Game feedback
        this.correctSprite = new Texture(gameAssets.correctSpritePath);
        this.incorrectSprite = new Texture(gameAssets.incorrectSpritePath);

        // Spawning variables
        this.enemyPosition = Gdx.graphics.getWidth();
        this.happyPosition = Gdx.graphics.getWidth();
        this.sweetRollLocation = Gdx.graphics.getWidth();
        this.gameOverPos = Gdx.graphics.getWidth();
        this.satisfiedOnion = Gdx.graphics.getWidth();

        this.lives = 5;
        this.isGameOver = false;
        this.willDisappear = false;

        Gdx.input.setInputProcessor(stage);

        // Defining the regions of sprite image we're going to create
        //atlas = new TextureAtlas("ui/button.pack"); // ???
        //skin = new Skin(atlas);

        table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //font file
      //  final String FONT_PATH = "irohamaru-mikami-Regular.ttf";
        generator = new FreeTypeFontGenerator(Gdx.files.internal(gameAssets.fontPath));

        //font for vocab word
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        //font for other words
        parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();

        //Adds 2 of each active word into gameWords
        for(int i = 0; i < activeVList.size(); i++) {
            gameWords.add(activeVList.get(i));
            gameWords.add(activeVList.get(i));
        }

        GAME_LIST_SIZE = gameWords.size();

        //db list of vocab words
        //dbListWords = dbCallback.getDbVocab();
        currentWordIndex = randomIndex(gameWords.size());
        displayWord = gameWords.get(currentWordIndex).getEngSpelling();

        //spliced correct words for grading
        cWords = gameWords.get(currentWordIndex).getCorrectWords();
        correctWordList = cWords.split("\\s*,\\s*");

        //setting font values
        parameter.characters = displayWord;
        parameter.size = 70;
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);
        parameter2.size = 30;
        parameter2.color = Color.BLACK;
//        font2 = generator.generateFont(parameter2);
        font2 = gameAssets.getFont();
        font2.setColor(0, 0, 0, 1);

        //Alignment and Text Wrapping for Vocab Word
        displayWordLayout = new GlyphLayout();
        displayWordLayout.setText(font, displayWord, Color.BLACK, targetWidth, Align.center, true);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        //textButtonStyle.up = skin.getDrawable("button.up");
        //textButtonStyle.down = skin.getDrawable("button.down");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font2;
        textButtonStyle.fontColor = Color.BLACK;

        backButton = new TextButton(gameAssets.getResourceBundle().getString("Back"), textButtonStyle);
        backButton.setPosition(Gdx.graphics.getWidth() - 100, 0);

        Label.LabelStyle headingStyle = new Label.LabelStyle(font, Color.BLACK);

        pauseButton = new TextButton(gameAssets.getResourceBundle().getString("Pause"), textButtonStyle);
        pauseButton.setPosition(Gdx.graphics.getWidth() - 200, 0);

            /*
                If you want to test functions with UI instead of with console,
                add it into one of these Listeners. Each of them correspond to
                one of the buttons on the screen in top-down order.
             */


        pauseButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(isPaused) {
                    try{
                        speechGDX.startRecognition();
                    } catch(Exception e) {
                        System.out.println(e);
                    }

                    if(gameMusic != null) {
                        gameMusic.setVolume(masterVolume);
                        gameMusic.play();
                    }
                    isPaused = false;
                }

                else {

                    speechGDX.stopRecognition();
                    if(gameMusic != null)
                        gameMusic.pause();
                    isPaused = true;

                }
            }
        });

        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                String rewardPath = gameAssets.allGunmaAnimations[levelNumber + 2];
                if(numStars == 3 && !Arrays.asList(gameAssets.availableGunmaAnimations).contains(rewardPath)) {
                    gameAssets.availableGunmaAnimations = gameAssets.addTo(gameAssets.availableGunmaAnimations, rewardPath);
                }
                speechGDX.stopRecognition();
                isPaused = true;
                gameMusic.dispose();
                gameMusic = Gdx.audio.newMusic(Gdx.files.internal(gameAssets.introMusicPath));
                gameMusic.setLooping(false);
                gameMusic.setVolume(masterVolume);
                gameMusic.play();
                game.setScreen(new MainMenuScreen(game, speechGDX,  gameMusic, dbCallback,activeVList, prefs, gameAssets));
                dispose(); // dispose of current GameScreen
            }
        });

        // Remove this later
        table.debug();

        stage.addActor(pauseButton);
        stage.addActor(backButton);


        //Start Speech Recognition
        try {
            speechGDX.startRecognition();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // SpriteBatch is resource intensive, try to use it for only brief moments
        batch.begin();
        backgroundDrawer.render(this.isPaused, this.isGameOver);
        this.livesDrawer.render();

        //batch.draw(background, 0, 0);

        font2.draw(batch, gameAssets.getResourceBundle().getString("Correct") + score + "/" + GAME_LIST_SIZE, 10, 35);

        if (!isGameOver) {

            font.draw(batch, displayWordLayout, 325, 425);

          /*  if (score >= 0){
                font2.draw(batch, "Correct " + score + "/" + gameWords.size(), 10, 35);
            }*/

            //font2.draw(batch, "Lives: " + lives, 25, 30);

            //need to parse out correct words separating by comma and check
            //with all correct words then use grading functionality

            //Returns false if word is null(no word has been said), or if word is incorrect
            incomingWord = speechGDX.getWord();
            correct = gradeSystem.grade(correctWordList, incomingWord);

            if(correct){
                // Start correct icon display
                this.correctDisplayTimer = this.CORRECT_DISPLAY_DURATION;
                score = score + 1;
                row = row + 1;
                if(row == 3){
                    gameMusic.dispose();
                    // incorrectSound.dispose();
                    //correctSound.dispose();
                    threeRow = Gdx.audio.newMusic(Gdx.files.internal(gameAssets.threeCorrect));
                    threeRow.setLooping(false);
                    threeRow.setVolume(masterVolume);
                    threeRow.play();
                }
                else {
                    gameMusic.dispose();
                    // incorrectSound.dispose();
                    correctSound = Gdx.audio.newMusic(Gdx.files.internal(gameAssets.sweetDog));
                    correctSound.setLooping(false);
                    correctSound.setVolume(masterVolume);
                    correctSound.play();
                }
                this.sweetRollLocation = 220;
                check = true;



                gameWords.remove(currentWordIndex);

                if(gameWords.size() > 0) {
                    currentWordIndex = randomIndex(gameWords.size());
                    displayWord = gameWords.get(currentWordIndex).getEngSpelling();
                    parameter.characters = displayWord;
                    parameter.size = 70;
                    parameter.color = Color.BLACK;
                    font = generator.generateFont(parameter);
                    displayWordLayout.setText(font, displayWord, Color.BLACK, targetWidth, Align.center, true);
                    cWords = gameWords.get(currentWordIndex).getCorrectWords();
                    correctWordList = cWords.split("\\s*,\\s*");
                } else {
                    isGameOver = true;
                    win = true;
                }
            } else if(!correct && incomingWord != null){
                //change word if incorrect
                currentWordIndex = randomIndex(gameWords.size());
                displayWord = gameWords.get(currentWordIndex).getEngSpelling();
                parameter.characters = displayWord;
                parameter.size = 70;
                parameter.color = Color.BLACK;
                font = generator.generateFont(parameter);
                displayWordLayout.setText(font, displayWord, Color.BLACK, targetWidth, Align.center, true);
                cWords = gameWords.get(currentWordIndex).getCorrectWords();
                correctWordList = cWords.split("\\s*,\\s*");
                // Start incorrect icon display
                this.incorrectDisplayTimer = this.INCORRECT_DISPLAY_DURATION;
                row = 0;

                gameMusic.dispose();
                //correctSound.dispose();
                incorrectSound = Gdx.audio.newMusic(Gdx.files.internal(gameAssets.youCanDoIt));
                incorrectSound.setLooping(false);
                incorrectSound.setVolume(masterVolume);
                incorrectSound.play();
            }

            if (!this.isPaused) {
                batch.draw(this.gunmaWalkAnimation.getCurrentFrame(delta), 90, 35 + this.SCREEN_BOTTOM_ADJUST);
            } else {
                batch.draw(this.gunmaWalkAnimation.getCurrentFrame(0), 90, 35 + this.SCREEN_BOTTOM_ADJUST);
            }
            this.sweetRollThrow(delta);
            this.walkOntoScreenFromRight(delta);
            this.walkOntoScreenFromLeft(delta);
            this.satisfyOnionWalk(delta);
            //   this.gameOverWalk(delta);
            this.defeatEnemy();


        } else {

            speechGDX.stopRecognition();

            while(!endCheck){
                this.gameOverWalk(delta);
            }

            if(score >= gameAssets.threeStarRequirement[levelNumber - 1]) {
                numStars = 3;
            } else if(score >= gameAssets.twoStarRequirement[levelNumber - 1]) {
                numStars = 2;
            } else if(score >= gameAssets.oneStarRequirement[levelNumber - 1]) {
                numStars = 1;
            }
            gameAssets.setLevelStars(levelNumber, numStars);
            addScore(numStars, delta);

            // gameMusic.dispose();
            // correctSound.dispose();
            // incorrectSound.dispose();
            gameOverSound = Gdx.audio.newMusic(Gdx.files.internal(gameAssets.gameEnd));
            gameOverSound.setLooping(false);
            gameOverSound.setVolume(masterVolume);
            gameOverSound.play();

        }

        if(correctDisplayTimer > 0) { this.correctAnswerGraphic();}
        if(incorrectDisplayTimer > 0) {this.incorrectAnswerGraphic();}
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
        speechGDX.stopRecognition();
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        gameWords.clear();
        font.dispose();
        font2.dispose();
        background.dispose();
        this.correctSprite.dispose();
        this.incorrectSprite.dispose();

        this.backgroundDrawer.dispose();
        this.livesDrawer.dispose();
        this.onionWalkAnimation.dispose();
        this.gunmaWalkAnimation.dispose();
        batch.dispose();
        stage.dispose();

    }

    private void walkOntoScreenFromRight(float delta) {
        if (!isPaused) {
            // This is a temporary fix. There's a more elegant solution that's less intensive I believe.
            TextureRegion tmp = onionWalkAnimation.getCurrentFrame(delta);
            tmp.flip(true, false);
            batch.draw(tmp, this.enemyPosition, 40 + this.SCREEN_BOTTOM_ADJUST);
            tmp.flip(true, false);
            this.enemyPosition -= gameAssets.frenemySpeed[levelNumber - 1];
            if (this.enemyPosition < 200) {
                this.takeDamage();
            }
        } else {
            TextureRegion tmp = onionWalkAnimation.getCurrentFrame(0);
            tmp.flip(true, false);
            batch.draw(tmp, this.enemyPosition, 40 + this.SCREEN_BOTTOM_ADJUST);
            tmp.flip(true, false);
        }
    }

    private void walkOntoScreenFromLeft(float delta) {
        if (!isPaused) {
            TextureRegion tmp = onionHungryWalkAnimation.getCurrentFrame(delta);
            batch.draw(tmp, this.happyPosition, 40 + this.SCREEN_BOTTOM_ADJUST);
            this.happyPosition += 1.15;
            if (this.happyPosition > 350) {
                //this.happyDisappear();
            }
        } else {
            TextureRegion tmp = onionHungryWalkAnimation.getCurrentFrame(0);
            batch.draw(tmp, this.happyPosition, 40 + this.SCREEN_BOTTOM_ADJUST);
        }
    }
    private void satisfyOnionWalk(float delta) {
        if (!isPaused) {
            TextureRegion tmp = onionSatisfiedAnimation.getCurrentFrame(delta);
            batch.draw(tmp, this.satisfiedOnion, 40 + this.SCREEN_BOTTOM_ADJUST);
            this.satisfiedOnion += 1.15;

        } else {
            TextureRegion tmp = onionSatisfiedAnimation.getCurrentFrame(delta);
            batch.draw(tmp, this.satisfiedOnion, 40 + this.SCREEN_BOTTOM_ADJUST);
        }
    }
    private void sweetRollThrow(float delta) {
        if (!isPaused) {
            batch.draw(this.sweetRoll, this.sweetRollLocation, 80 + this.SCREEN_BOTTOM_ADJUST);
            this.sweetRollLocation += 1.25;
            if(this.sweetRollLocation > this.enemyPosition){
                this.disappear();
            }

        } else {
            batch.draw(this.sweetRoll, this.sweetRollLocation, 40 + this.SCREEN_BOTTOM_ADJUST);
        }
    }

    private void gameOverWalk(float delta) {

        TextureRegion tmp = onionStealAnimation.getCurrentFrame(delta);
        batch.draw(tmp, gameOverPos, 40 + this.SCREEN_BOTTOM_ADJUST);
        this.gameOverPos += 1.15;
        if(gameOverPos > 400 & this.lives == 0){
            //this.isGameOver = true;
            this.endCheck = true;
        }

    }

    private void takeDamage() {
        this.enemyPosition = Gdx.graphics.getWidth() + 50;
        this.happyPosition = 240;
        this.lives--;
        this.row = 0;
        this.livesDrawer.takeLife();

        if (this.lives == 0) {
            // this.gameOverPos = 70;
            // speechGDX.stopRecognition();
            //  batch.draw(this.gunmaFaintedSprite, 70, 10 + this.SCREEN_BOTTOM_ADJUST);
            this.isGameOver = true;
        }
    }

    private void disappear() {
        this.sweetRollLocation = Gdx.graphics.getWidth() + 50;
    }
    private void happyDisappear(){
        this.happyPosition = Gdx.graphics.getWidth() + 50;
    }


    private void defeatEnemy() {
        //batch.draw(this.happyOnion, 100, 10 + this.SCREEN_BOTTOM_ADJUST);
        if(check == true){
            if(this.sweetRollLocation > this.enemyPosition){
                this.satisfiedOnion = this.enemyPosition;
                this.enemyPosition = Gdx.graphics.getWidth();

                check = false;
            }
        }

        // However you want to change the current vocab would go here
    }

    private void correctAnswerGraphic() {
        if (this.correctDisplayTimer == this.CORRECT_DISPLAY_DURATION) {
            // Play sound effect here
        }
        batch.draw(this.correctSprite, Gdx.graphics.getWidth()/2-80, Gdx.graphics.getHeight()/4*3-140);
        this.correctDisplayTimer--;
    }

    private void incorrectAnswerGraphic() {
        if (this.incorrectDisplayTimer == this.INCORRECT_DISPLAY_DURATION) {
            // Play sound effect here
        }
        batch.draw(this.incorrectSprite, Gdx.graphics.getWidth()/2-80, Gdx.graphics.getHeight()/4*3-140);
        this.incorrectDisplayTimer--;
    }
    private int randomIndex(int size) {

        if(size == 1) {
            return 0;
        }

        Random rand = new Random();
        int randomInt = rand.nextInt(size - 1);
        return randomInt;
    }

    private void addScore(int numStars, float delta) {
        Table table = new Table();
        table.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        Texture stars = new Texture(gameAssets.getStarPath(numStars));
        Texture explosion = new Texture(gameAssets.explosionPath);
        String rewardPath = gameAssets.allGunmaAnimations[levelNumber + 2];
        Animator reward = new Animator(rewardPath, 8, 1, 0.1f);
        Texture border = new Texture(gameAssets.activeBorder);

        if(numStars == 3 && !Arrays.asList(gameAssets.availableGunmaAnimations).contains(rewardPath)) {
            batch.draw(explosion, Gdx.graphics.getWidth() / 2 - explosion.getWidth() / 8, Gdx.graphics.getHeight() / 2 - explosion.getHeight() / 4 - 20, explosion.getWidth() / 4, explosion.getHeight() / 4);
            batch.draw(reward.getCurrentFrame(delta), Gdx.graphics.getWidth() / 2 - border.getWidth() / 2, Gdx.graphics.getHeight() / 2 - explosion.getHeight() / 6 - 20);
//            gameAssets.availableGunmaAnimations = gameAssets.addTo(gameAssets.availableGunmaAnimations, rewardPath);
        }

        batch.draw(this.gunmaFaintedSprite, 70, 10 + this.SCREEN_BOTTOM_ADJUST);
        batch.draw(stars, Gdx.graphics.getWidth()/2 - stars.getWidth()/4, Gdx.graphics.getHeight()/2 + stars.getHeight()/4, stars.getWidth()/2, stars.getHeight()/2);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font2;
        textButtonStyle.fontColor = Color.BLACK;

        Label.LabelStyle headingStyle = new Label.LabelStyle(font2, Color.BLACK);

        Label heading = new Label(gameAssets.getResourceBundle().getString("YourScore") + " " + score, headingStyle);
        Skin skin = gameAssets.getColorSkin(gameAssets.color2, "color2");
        textButtonStyle.up = skin.newDrawable("color2", gameAssets.color2);

        TextButton continueButton = new TextButton(gameAssets.getResourceBundle().getString("Continue"), textButtonStyle);

        heading.setFontScale(2);


        continueButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                speechGDX.stopRecognition();
                isPaused = true;
                gameMusic.dispose();
                gameMusic = Gdx.audio.newMusic(Gdx.files.internal(gameAssets.introMusicPath));
                gameMusic.setLooping(false);
                gameMusic.setVolume(masterVolume);
                gameMusic.play();
                game.setScreen(new MainMenuScreen(game, speechGDX,  gameMusic, dbCallback, activeVList, prefs, gameAssets));
                dispose(); // dispose of current GameScreen
            }
        });

        continueButton.pad(15);
        table.add(heading);
//        table.row();
//        table.add(continueButton);

        stage.addActor(table);


//        Timer.schedule(new Timer.Task(){
//            @Override
//            public void run() {
//                Texture explosion = new Texture(gameAssets.explosionPath);
//
////                batch.draw(this.gunmaFaintedSprite, 70, 10 + this.SCREEN_BOTTOM_ADJUST);
//                batch.draw(explosion, Gdx.graphics.getWidth()/2 - explosion.getWidth()/4, Gdx.graphics.getHeight()/2 + explosion.getHeight()/2, explosion.getWidth()/2, explosion.getHeight()/2);
//            }
//        }, 3);

//        Timer timer = new Timer();
//
//        timer.scheduleTask(new Timer.Task(){
//            @Override
//            public void run() {
//                speechGDX.stopRecognition();
//                isPaused = true;
//                gameMusic.dispose();
//                gameMusic = Gdx.audio.newMusic(Gdx.files.internal(gameAssets.introMusicPath));
//                gameMusic.setLooping(false);
//                gameMusic.setVolume(masterVolume);
//                gameMusic.play();
//                game.setScreen(new MainMenuScreen(game, speechGDX, gameMusic, dbCallback, activeVList, prefs, gameAssets));
//                dispose(); // dispose of current GameScreen
//            }
//        }, 3);


    }
}
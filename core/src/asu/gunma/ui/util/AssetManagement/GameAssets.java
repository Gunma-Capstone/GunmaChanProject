package asu.gunma.ui.util.AssetManagement;

import asu.gunma.ui.util.AssetManagement.monthlyAssets.Month;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Locale;
import java.util.ResourceBundle;

/* These are the global game assets.
*
* To add a new global asset add the following line:
* public static <asset_type> <asset_name> = <asset_value>;
*
* To add a seasonal asset, see [asu.gunma.ui.util.AssetManagement.seasonalAssets.Season].
* To add a monthly asset, see [asu.gunma.ui.util.AssetManagement.monthlyAssets.Month].
*
* Note: adding an asset to this file will override any seasonal or monthly assets.
* */
public class GameAssets extends Month {
    // Global Assets Across Screens
    public static Color backgroundColor = Color.WHITE;
    public static String fontPath = "irohamaru-mikami-Regular.ttf";
    public static String introMusicPath = "IntroMusic.mp3";
    public static String titleGunmaPath = "title_gunma.png";
    public static String threeCorrect = "correct_threeinarow.mp3";
    public static String sweetDog = "correct_sweetdog.mp3";
    public static String youCanDoIt = "incorrect_youcandoit.mp3";
    public static String gameEnd = "enemytakessatchel.mp3";

    // Main Menu Screen Assets

    // Option Menu Screen Assets

    // Settings Screen Assets

    // Title Screen Assets

    // Flashcard Screen Assets
    public static String greenCirclePath = "greenCircle.png";
    public static String redXPath = "redX.png";

    // Placeholder frenemy animations
    public static String placeholderFrenemyAnimation1Path = "Gunma-chan-Japanese-character-enemy-walk-anim-sheet-02.png";
    public static String placeholderFrenemyAnimation2Path = "Gunma-chan-Japanese-character-enemy-walk-anim-sheet-03.png";
    public static String placeholderFrenemyAnimation3Path = "Gunma-chan-Japanese-character-enemy-walk-anim-sheet-04.png";
    public static String placeholderFrenemyAnimation4Path = "Gunma-chan-Japanese-character-enemy-walk-anim-sheet-05.png";

    // Gunma Assets
    public static String gunmaWalkAnimationMain = "Gunma_with_bag_small.png";
    public static String gunmaRainbowBagAnimation = "Gunma_with_bag_small_rainbow2.png";
    public static String gunmaBowTieAnimation = "Gunma_with_bag_small_bow_tie.png";
    public static String gunmaGreenBagAnimation = "Gunma_with_bag_small_green_bag.png";
    public static String gunmaBlueHatAnimation = "Gunma_with_bag_small_blue_hat.png";
    public static String gunmaPinkHatAnimation = "Gunma_with_bag_small_pink_hat.png";
    public static String gunmaRedHatAnimation = "Gunma_with_bag_small_red_hat.png";
    public static String gunmaRainbowHatAnimation = "Gunma_with_bag_small_rainbow_hat.png";
    public static String activeBorder = "brown_border.png";
    public static String inactiveBorder = "light_brown_border.png";
    public static String explosionPath = "explosion.png";
    public static String questionBorder = "question_border.png";
    public static String[] allGunmaAnimations = {gunmaWalkAnimationMain, gunmaGreenBagAnimation, gunmaBowTieAnimation, gunmaBlueHatAnimation, gunmaPinkHatAnimation, gunmaRedHatAnimation, gunmaRainbowBagAnimation, gunmaRainbowHatAnimation};
//    public static String[] availableGunmaAnimations = {gunmaWalkAnimationMain, gunmaGreenBagAnimation, gunmaBowTieAnimation, gunmaBlueHatAnimation, gunmaPinkHatAnimation, gunmaRedHatAnimation, gunmaRainbowBagAnimation, gunmaRainbowHatAnimation};
    public String[] availableGunmaAnimations = {gunmaWalkAnimationMain, gunmaGreenBagAnimation, gunmaBowTieAnimation};

    // Game Screen Assets
    public static String gunmaSpritePath = "sprite_gunma.png";
    public static String gunmaFaintedSpritePath = "gunma_fainted.png";
    public static String backgroundImagePath = "BG_temp.png";
    public static String onionWalkAnimationPath = "onion_sheet.png";
    public static String gunmaWalkAnimationPath = "gunma_sheet.png";
    public static String correctSpritePath = "background/correct.png";
    public static String incorrectSpritePath = "background/incorrect.png";
    public static String gunmaWalkAnimationActive = gunmaWalkAnimationMain;
    public static String onionHungryWalkAnimation = "onion_sheetSweetRoll.png";
    public static String onionStealAnimation = "onion_sheet.png";
    public static String onionSatisfiedAnimation = "onion_sheetSmile.png";
    public static String sweetRoll = "sweetRoll.png";
    public static String[] frenemyWalkAnimationPathPerLevel = {onionWalkAnimationPath, placeholderFrenemyAnimation1Path, placeholderFrenemyAnimation2Path, placeholderFrenemyAnimation3Path, placeholderFrenemyAnimation4Path};

    // Game Score Screen Assets
    public static String noStarsPath = "no_stars.png";
    public static String oneStarPath = "one_star.png";
    public static String twoStarsPath = "two_stars.png";
    public static String threeStarsPath = "three_stars.png";
    public String getStarPath(int num) {
        if(num == 0) {
            return noStarsPath;
        } else if(num == 1) {
            return oneStarPath;
        } else if(num == 2) {
            return twoStarsPath;
        }
        return threeStarsPath;
    }

    // Mountain Screen Assets
    public static String smallMountainImagePath = "background/cropped_mountain2.png";
    public static String level2SmallMountainImagePath = "background/mountain_with_path_level2.png";
    public static String level3SmallMountainImagePath = "background/mountain_with_path_level3.png";
    public static String level4SmallMountainImagePath = "background/mountain_with_path_level4.png";
    public static String level5SmallMountainImagePath = "background/mountain_with_path.png";
    public static String onionFrenemy = "onion.png";
    public static String cabbageFrenemy = "cabbage1.png";
    public static String konjackunFrenemy = "konjackun.png";
    public static String angrynegFrenemy = "angryneg.png";
    public static String negisanFrenemy = "negisan.png";

    public String getMountainWithPath() {
        if(level4Stars > 0) {
            return level5SmallMountainImagePath;
        } else if(level3Stars > 0) {
            return level4SmallMountainImagePath;
        } else if(level2Stars > 0) {
            return level3SmallMountainImagePath;
        } else if(level1Stars > 0) {
            return level2SmallMountainImagePath;
        } else {
            return smallMountainImagePath;
        }
    }

    // Game Level Assets
    public int level1Stars = -1;
    public int level2Stars = -1;
    public int level3Stars = -1;
    public int level4Stars = -1;
    public int level5Stars = -1;

    // Score requirements to earn a star per level
    public int[] oneStarRequirement = {2, 4, 4, 7, 10};
    public int[] twoStarRequirement = {3, 5, 6, 9, 15};
    public int[] threeStarRequirement = {5, 7, 9, 11, 20};
    public double[] frenemySpeed = {1.15, 1.3, 1.5, 1.75, 2};

    public void setLevelStars(int level, int numStars) {
        if(level == 1 && numStars > level1Stars) {
            level1Stars = numStars;
        } else if(level == 2 && numStars > level2Stars) {
            level2Stars = numStars;
        } else if(level == 3 && numStars > level3Stars) {
            level3Stars = numStars;
        } else if(level == 4 && numStars > level4Stars) {
            level4Stars = numStars;
        } else if(level == 5 && numStars > level5Stars) {
            level5Stars = numStars;
        }
    }

    public String getLevelStars(int level) {
        if(level == 1) {
            return getStarPath(level1Stars);
        } else if(level == 2) {
            return getStarPath(level2Stars);
        } else if(level == 3) {
            return getStarPath(level3Stars);
        } else if(level == 4) {
            return getStarPath(level4Stars);
        } else if(level == 5) {
            return getStarPath(level5Stars);
        }
        return "";
    }


    // Background Drawer Assets
    public static String cloud1Path = "background/cloud1.png";
    public static String cloud2Path = "background/cloud2.png";
    public static String skyImagePath = "background/skyx2.png";

    public static Skin getColorSkin(Color color, String name) {
        Skin skin = new Skin();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        skin.add(name, new Texture(pixmap));
        return skin;
    }

    // Localization assets
    public String localeString = "en";
    private Locale locale;
    private ResourceBundle bundle;
    public ResourceBundle getResourceBundle() {
        locale = new Locale(localeString);
        bundle = MyResources.getBundle("asu.gunma.ui.util.AssetManagement.MyResources", locale);
        return bundle;
    }
    public void setLocale(String lang) {
        localeString = lang;
    }

    public BitmapFont getFont() {
        Texture textureCN = new Texture(Gdx.files.internal("custom_font_hiero_2.png"), true);
        textureCN.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);

        return new BitmapFont(Gdx.files.internal("custom_font_hiero_2.fnt"), new TextureRegion(textureCN), false);
    }


    public void drawDottedLine(ShapeRenderer shapeRenderer, int dotDist, float x1, float y1, float x2, float y2) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Point);

        Vector2 vec2 = new Vector2(x2, y2).sub(new Vector2(x1, y1));
        float length = vec2.len();
        for(int i = 0; i < length; i += dotDist) {
            vec2.clamp(length - i, length - i);
            shapeRenderer.point(x1 + vec2.x, y1 + vec2.y, 0);
        }

        shapeRenderer.end();
    }

    // Add [x] to [arr].
    public void addToAvailableGunma(String x)
    {
        String newarr[] = new String[availableGunmaAnimations.length + 1];

        for (int i = 0; i < availableGunmaAnimations.length; i++)
            newarr[i] = availableGunmaAnimations[i];

        newarr[availableGunmaAnimations.length] = x;

        availableGunmaAnimations = newarr;
    }
}

package asu.gunma.ui.util.AssetManagement.seasonalAssets;

import com.badlogic.gdx.graphics.Color;

import java.text.SimpleDateFormat;
import java.util.Date;

/* These are seasonal assets.
 *
 * To add a new seasonal asset, add the following line to this file:
 * public static <asset_type> <asset_name> = currentSeason.<asset_name>;
 *
 * Then, go to each season class and override the new asset.
 * */
abstract public class Season {
    public static Season currentSeason = Season.getSeason();

    public static String grassImagePath = currentSeason.grassImagePath;
    public static String mountainImagePath = currentSeason.mountainImagePath;

    public static Color color1 = currentSeason.color1;
    public static Color color2 = currentSeason.color2;
    public static Color color3 = currentSeason.color3;
    public static Color color4 = currentSeason.color4;
    public static Color color5 = currentSeason.color5;

    public static Season getSeason() {
        Date currentDate = new Date();
        SimpleDateFormat formatMonth = new SimpleDateFormat("MM");
        SimpleDateFormat formatDay = new SimpleDateFormat("dd");

        String monthString = formatMonth.format(currentDate);
        String dayString = formatDay.format(currentDate);

        int month = Integer.parseInt(monthString);
        int day = Integer.parseInt(dayString);

        if ((month == 12 && day >= 21) || month == 1 || month == 2 || (month == 3 && day < 20)) {
            return new Winter();
        } else if (month == 3 || month == 4 || month == 5 || (month == 6 && day < 21)) {
            return new Spring();
        } else if (month == 6 || month == 7 || month == 8 || (month == 9 && day < 23)) {
            return new Summer();
        }
        return new Fall();
    }
}

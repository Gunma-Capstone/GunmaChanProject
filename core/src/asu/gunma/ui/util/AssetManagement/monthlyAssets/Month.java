package asu.gunma.ui.util.AssetManagement.monthlyAssets;

import com.badlogic.gdx.graphics.Color;

import java.text.SimpleDateFormat;
import java.util.Date;

import asu.gunma.ui.util.AssetManagement.seasonalAssets.Season;

/* These are monthly assets.
*
* To add a new monthly asset, add the following line to this file:
* public static <asset_type> <asset_name> = currentMonth.<asset_name>;
*
* Then, go to each month class and override the new asset.
* */
abstract public class Month extends Season {
    public static Month currentMonth = Month.getMonth();

    public static Color backgroundColor = currentMonth.backgroundColor;

    public static Month getMonth() {
        Date currentDate = new Date();
        SimpleDateFormat formatMonth = new SimpleDateFormat("MM");
        SimpleDateFormat formatDay = new SimpleDateFormat("dd");

        String monthString = formatMonth.format(currentDate);
        String dayString = formatDay.format(currentDate);

        int month = Integer.parseInt(monthString);
        int day = Integer.parseInt(dayString);

        if (month == 1) {
            return new January();
        } else if (month == 2) {
            return new February();
        } else if (month == 3) {
            return new March();
        } else if (month == 4) {
            return new April();
        } else if (month == 5) {
            return new May();
        } else if (month == 6) {
            return new June();
        } else if (month == 7) {
            return new July();
        } else if (month == 8) {
            return new August();
        } else if (month == 9) {
            return new September();
        } else if (month == 10) {
            return new October();
        } else if (month == 11) {
            return new November();
        } else {
            return new December();
        }
    }
}

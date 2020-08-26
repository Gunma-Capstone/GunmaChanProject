package asu.gunma.ui.util.AssetManagement.seasonalAssets;

import com.badlogic.gdx.graphics.Color;

/* These are the assets for the season.
 *
 * To add a new asset, add the following line to [asu.gunma.ui.util.AssetManagement.seasonalAssets.Season]:
 * public static <asset_type> <asset_name> = currentSeason.<asset_name>;
 *
 * Then, override the asset in this class with the following line:
 * public <asset_type> _<asset_name> = <asset_name> = <asset_value>;
 * */
public class Winter extends Season {
    public String _grassImage = grassImagePath = "background/grassx2_w.png";
    public String _mountainImage = mountainImagePath = "background/mountainx3_w.png";

    public Color _color1 = color1 = new Color(0x822b3eff);
    public Color _color2 = color2 = new Color(0xea223fff);
    public Color _color3 = color3 = new Color(0x686c58ff);
    public Color _color4 = color4 = new Color(0x003e2fff);
    public Color _color5 = color5 = new Color(0x253a49ff);
}

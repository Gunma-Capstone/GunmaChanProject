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
public class Fall extends Season {
    public String _grassImage = grassImagePath = "background/grassx2.png";
    public String _mountainImage = mountainImagePath = "background/mountainx3_f.png";


    public Color _color1 = color1 = new Color(0xd94c2cff);
    public Color _color2 = color2 = new Color(0xed861fff);
    public Color _color3 = color3 = new Color(0xf4d360ff);
    public Color _color4 = color4 = new Color(0xa8ce68ff);
    public Color _color5 = color5 = new Color(0x1b4314ff);
}

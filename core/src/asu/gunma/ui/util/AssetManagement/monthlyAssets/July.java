package asu.gunma.ui.util.AssetManagement.monthlyAssets;

/* These are the assets for the month.
 *
 * To add a new asset, add the following line to [asu.gunma.ui.util.AssetManagement.monthlyAssets.Month]:
 * public static <asset_type> <asset_name> = currentMonth.<asset_name>;
 *
 * Then, override the asset in this class with the following line:
 * public <asset_type> _<asset_name> = <asset_name> = <asset_value>;
 *
 * To make the monthly asset default to the current season asset, use the following line:
 * public <asset_type> _<asset_name> = <asset_name> = currentSeason.<asset_name>;
 * */
public class July extends Month {

}

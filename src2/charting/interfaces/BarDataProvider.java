package charting.interfaces;

import charting.data.BarData;

public interface BarDataProvider extends BarLineScatterCandleBubbleDataProvider
{

	BarData getBarData();

	boolean isDrawBarShadowEnabled();

	boolean isDrawValueAboveBarEnabled();

	boolean isDrawHighlightArrowEnabled();
}

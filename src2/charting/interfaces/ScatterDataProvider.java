package charting.interfaces;

import charting.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider
{

	ScatterData getScatterData();
}

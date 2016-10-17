package charting.interfaces;

import charting.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider
{

	BubbleData getBubbleData();
}

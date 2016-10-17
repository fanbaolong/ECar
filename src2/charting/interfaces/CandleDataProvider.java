package charting.interfaces;

import charting.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider
{

	CandleData getCandleData();
}

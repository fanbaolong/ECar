package charting.interfaces;

import charting.components.YAxis;
import charting.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider
{

	LineData getLineData();

	YAxis getAxis(YAxis.AxisDependency dependency);
}

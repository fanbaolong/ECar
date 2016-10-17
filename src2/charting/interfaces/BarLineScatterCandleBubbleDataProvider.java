package charting.interfaces;

import charting.components.YAxis.AxisDependency;
import charting.data.BarLineScatterCandleBubbleData;
import charting.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface
{

	Transformer getTransformer(AxisDependency axis);

	int getMaxVisibleCount();

	boolean isInverted(AxisDependency axis);

	int getLowestVisibleXIndex();

	int getHighestVisibleXIndex();

	BarLineScatterCandleBubbleData getData();
}

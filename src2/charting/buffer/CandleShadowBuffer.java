package charting.buffer;

import java.util.List;

import charting.data.CandleEntry;

public class CandleShadowBuffer extends AbstractBuffer<CandleEntry>
{

	public CandleShadowBuffer(int size)
	{
		super(size);
	}

	private void addShadow(float x1, float y1, float x2, float y2)
	{

		buffer[index++] = x1;
		buffer[index++] = y1;
		buffer[index++] = x2;
		buffer[index++] = y2;
	}

	@Override
	public void feed(List<CandleEntry> entries)
	{

		int size = (int) Math.ceil((mTo - mFrom) * phaseX + mFrom);

		for (int i = mFrom; i < size; i++)
		{

			CandleEntry e = entries.get(i);
			addShadow(e.getXIndex(), e.getHigh() * phaseY, e.getXIndex(), e.getLow() * phaseY);
		}

		reset();
	}
}

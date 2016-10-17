package com.xieyu.ecar.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.News;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;

/**
 * @author fbl
 * 
 *         消息详情
 */
public class NewsDetailActivity extends BackableTitleBarActivity
{
	private News mNews = new News();

	@V
	TextView new_detail_title, new_detail_content, new_detail_time;

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_news_detail);
		getTitleBar().setTitle(R.string.message_detail);

		Injector.getInstance().inject(this);

		mNews = (News) getIntent().getSerializableExtra("news");

		new_detail_title.setText(mNews.getTitle());
		new_detail_content.setText(mNews.getContent());
		new_detail_time.setText(mNews.getUpdateTime());

	}
}

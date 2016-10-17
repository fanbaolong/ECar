package com.xieyu.ecar.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fanbaolong
 * 
 *         解析url地址，获取自己想要的参数
 *
 */
public class URLAnalysisUtil
{
	private Map<String, String> paramMap = new HashMap<String, String>();

	public void analysis(String url)
	{
		paramMap.clear();
		if (!"".equals(url))
		{// 如果URL不是空字符串
			url = url.substring(url.indexOf('?') + 1);
			String paramaters[] = url.split("&");
			for (String param : paramaters)
			{
				String values[] = param.split("=");
				paramMap.put(values[0], values[1]);
			}
		}
	}

	public String getParam(String name)
	{
		return paramMap.get(name);
	}

	public static void main(String[] args)
	{
		String test = "http://xxx.com?type=helddlo&id=100";
		URLAnalysisUtil urlAnalysis = new URLAnalysisUtil();
		urlAnalysis.analysis(test);
		System.out.println("name = " + urlAnalysis.getParam("name"));
		System.out.println("id = " + urlAnalysis.getParam("id"));
	}
}

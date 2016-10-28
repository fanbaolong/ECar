package com.xieyu.ecar.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.widget.TextView;

import com.xieyu.ecar.App;

/**
 * @author fanbaolong
 *
 *         字符串相关处理
 */
public class StringUtil
{

	
	//string  默认时间格式
	public static String defaultTime = "----/--/--  --:--";
	/**
	 * 地图上显示电桩类型
	 * 
	 * @param map
	 *            传入map
	 * @return StringBuffer
	 */
	public static StringBuffer getPilesCategrey(Map<String, Integer> map)
	{
		StringBuffer stringBuffer = new StringBuffer();
		for (Map.Entry<String, Integer> entry : map.entrySet())
		{
			stringBuffer.append("\n" + entry.getKey() + ":" + entry.getValue());
		}
		return stringBuffer;

	}

	/**
	 * 将服务器获得的时间转化为 "yyyy-MM-dd HH:mm:ss" 格式
	 * 
	 * @param date
	 * @return string "yyyy-MM-dd HH:mm:ss" 格式时间
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDateTime(Long date)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return dateFormat.format(parseDate(date));
	}

	/**
	 * 当前时间毫秒转为Date
	 * 
	 * @param l
	 * @return 日期 Date
	 */
	public static Date parseDate(Long l)
	{
		Date date = new Date();
		date.setTime(l);
		return date;
	}

	public static String getTextViewContent(TextView textView)
	{
		return textView.getText().toString().trim();
	}

	/**
	 * 验证字符串是否为空，若为空就弹出 Toast，内容为 errorStringRes
	 * 
	 * @param content
	 * @param errorStringRes
	 * @return true 验证未通过，false 验证通过
	 */
	public static boolean invalidateContent(String content, int errorStringRes)
	{
		if (TextUtils.isEmpty(content))
		{
			if (errorStringRes > 0)
			{
				App.showShortToast(App.getInstance().getString(errorStringRes));
			}
			return true;
		}
		return false;
	}

	/**
	 * 字符串是否为手机号
	 * 
	 * @param phoneNo
	 * @return true 是 手机号， false 不是 手机号
	 */
	public static boolean isPhoneNo(String phoneNo)
	{
		return regxStr(phoneNo, "^(\\+86)?0?1[3|4|5|8|7]\\d{9}$");
	}

	/**
	 * 校验是否为手机号
	 * 
	 * @param text
	 * @return true 是 手机号， false 不是 手机号
	 */
	public static boolean validatePhoneNo(TextView text)
	{
		return isPhoneNo(getTextViewContent(text));
	}

	/**
	 * 正则匹配
	 * 
	 * @param validateStr
	 *            待校验的字符串
	 * @param pattern
	 *            正则表达式
	 * @return
	 */
	public static boolean regxStr(String validateStr, String pattern)
	{
		Pattern p = Pattern.compile(pattern);
		return p.matcher(validateStr).matches();
	}

	/**
	 * 验证是否为正确的固定电话
	 * 
	 * @param text
	 *            待校验的字符串
	 * @return pattern 正则表达式
	 */
	public static boolean isFixedTelePhoneNo(String text)
	{
		String str = "^(\\d{3,4}\\-?)?\\d{7,8}$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(text);
		return m.matches();
	}

	/**
	 * 验证是否为正确的邮箱
	 * 
	 * @param email
	 * @return true 为正确的邮箱， false 反之
	 */
	public static boolean isEmail(String email)
	{
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 验证是否是正确的邮编
	 * 
	 * @param zipcode
	 * @return true false
	 */
	public static boolean isZipCode(String zipcode)
	{
		String str = "[1-9]\\d{5}(?!\\d)";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(zipcode);
		return m.matches();
	}

	public static String isNull(String str)
	{
		String strTemp = "";
		if (null != str && !"null".equals(str))
		{
			if (!"".equals(str))
			{
				return str;
			}
		}
		return strTemp;
	}
	
	   /**
     * 校验身份证
     * 
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
	
	 /**
     * 正则表达式：验证身份证
     */
    public static boolean isIDCard(String idCard) {
    	String REGEX_ID_CARD = "\\d{18}|\\d{15}|\\d{17}+[a-zA-Z]{1}";
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }
    
    /**
     * 车牌号格式：汉字 + A-Z + 5位A-Z或0-9 （只包括了普通车牌号，教练车和部分部队车等车牌号不包括在内）
     * @param carnumber
     * @return
     */
    public static boolean isCarnumberNO(String carnumber) {
        String carnumRegex = "[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}";
        if (TextUtils.isEmpty(carnumber)) return false;
        else return carnumber.matches(carnumRegex);
    }

}

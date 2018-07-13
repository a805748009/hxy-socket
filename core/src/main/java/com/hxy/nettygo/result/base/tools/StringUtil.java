
package com.hxy.nettygo.result.base.tools;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * ClassName:StringUtil Function: TODO ADD FUNCTION. Date: 2017年8月23日 下午1:56:41
 * 
 * @author HXY
 */
public class StringUtil {
	public static final String SEPARATOR = String.valueOf('\035');

	public static final String VERSEP = "\\|";

	public static final String VERCOMMA = "\\,";

	public StringUtil() {
	}

	public static boolean isNotEmpty(String str) {
		return StringUtils.isNotEmpty(str);
	}

	public static boolean isEmpty(String str) {
		return StringUtils.isEmpty(str);
	}

	/**
	 * 
	 * 如果是空就为默认值
	 *
	 * @author HXY
	 * @date 2017年8月29日上午10:36:34
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static String defaultIfEmpty(String str, String defaultValue) {
		return StringUtils.defaultIfEmpty(str, defaultValue);
	}

	public static String replaceAll(String str, String regex, String replacement) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, replacement);
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 
	 * 判断是否数字
	 *
	 * @author HXY
	 * @date 2017年8月29日上午10:33:04
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		return NumberUtils.isNumber(str);
	}

	public static boolean isDigits(String str) {
		return NumberUtils.isDigits(str);
	}

	/**
	 * 
	 * 驼峰转下划线
	 *
	 * @author HXY
	 * @date 2017年8月29日上午10:52:41
	 * @param str
	 * @return
	 */
	public static String camelhumpToUnderline(String str) {
		Matcher matcher = Pattern.compile("[A-Z]").matcher(str);
		StringBuilder builder = new StringBuilder(str);
		for (int i = 0; matcher.find(); i++) {
			builder.replace(matcher.start() + i, matcher.end() + i, "_" + matcher.group().toLowerCase());
		}
		if (builder.charAt(0) == '_') {
			builder.deleteCharAt(0);
		}
		return builder.toString();
	}

	/**
	 * 
	 * 下划线转驼峰
	 *
	 * @author HXY
	 * @date 2017年8月29日上午10:52:51
	 * @param str
	 * @return
	 */
	public static String underlineToCamelhump(String str) {
		Matcher matcher = Pattern.compile("_[a-z]").matcher(str);
		StringBuilder builder = new StringBuilder(str);
		for (int i = 0; matcher.find(); i++) {
			builder.replace(matcher.start() - i, matcher.end() - i, matcher.group().substring(1).toUpperCase());
		}
		if (Character.isUpperCase(builder.charAt(0))) {
			builder.replace(0, 1, String.valueOf(Character.toLowerCase(builder.charAt(0))));
		}
		return builder.toString();
	}
	
	/**
	 * 
	 * 字符串以separator为切割点，切割成字符串数组
	 *
	 * @author HXY
	 * @date 2017年8月29日下午1:15:30
	 * @param str
	 * @param separator
	 * @return
	 */
	public static String[] splitString(String str, String separator) {
		return StringUtils.splitByWholeSeparator(str, separator);
	}

	/**
	 * 
	 * 首字母大写
	 *
	 * @author HXY
	 * @date 2017年8月29日下午1:02:46
	 * @param str
	 * @return
	 */
	public static String firstToUpper(String str) {
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}
	
	/**
	 * 
	 * 首字母小写
	 *
	 * @author HXY
	 * @date 2017年8月29日下午1:03:00
	 * @param str
	 * @return
	 */
	public static String firstToLower(String str) {
		return Character.toLowerCase(str.charAt(0)) + str.substring(1);
	}

	public static String toPascalStyle(String str, String seperator) {
		return firstToUpper(toCamelhumpStyle(str, seperator));
	}

	/**
	 * 
	 * 下划线转驼峰格式，下划线后第一个字母边大写
	 *
	 * @author HXY
	 * @date 2017年8月29日下午1:13:47
	 * @param str
	 * @param seperator
	 * @return
	 */
	public static String toCamelhumpStyle(String str, String seperator) {
		return underlineToCamelhump(toUnderlineStyle(str, seperator));
	}

	/**
	 * 
	 * 去空格，全小写，固定字符seperator转下划线
	 *
	 * @author HXY
	 * @date 2017年8月29日下午1:07:47
	 * @param str
	 * @param seperator
	 * @return
	 */
	public static String toUnderlineStyle(String str, String seperator) {
		str = str.trim().toLowerCase();
		if (str.contains(seperator)) {
			str = str.replace(seperator, "_");
		}
		return str;
	}

	public static String toDisplayStyle(String str, String seperator) {
		String displayName = "";
		str = str.trim().toLowerCase();
		if (str.contains(seperator)) {
			String[] words = splitString(str, seperator);
			for (String word : words) {
				displayName = displayName + firstToUpper(word) + " ";
			}
			displayName = displayName.trim();
		} else {
			displayName = firstToUpper(str);
		}
		return displayName;
	}
	
	/**
	 * 
	 * iso8859 轉碼 utf-8
	 *
	 * @author HXY
	 * @date 2017年8月29日上午10:31:56
	 * @param str
	 * @return
	 */
	public static String iso8859ToUTF8(String str) {
		try {
			if (isNotEmpty(str)) {
				return new String(str.getBytes("ISO-8859-1"), "utf8");
			}
			return "";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 
	 * @return
	 */
	public static String arrayToString(List<?> list){
		StringBuffer b =new StringBuffer();
		for(int x =0;x<list.size();x++){
		  if(x==list.size()-1){
				b.append(list.get(x).toString());
			}else{
				b.append(list.get(x).toString()+",");
			}
		}
		return b.toString();
	}
}

/**
 * 
 */
package cn.salesuite.saf.utils;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串的帮助类
 * @author Tony Shen
 *
 */
public class StringUtils {

    private static final char CHAR_CHINESE_SPACE = '\u3000';//中文（全角）空格
	
	/**
	 * 判断字符串是否为手机号码</br>
	 * 只能判断是否为大陆的手机号码
	 * @param str
	 * @return
	 */
	public static boolean checkMobile(String str) {
		Pattern p = Pattern.compile("1[34578][0-9]{9}");
		Matcher m = p.matcher(str);
		return m.matches();
	}
	
	/**
	 * 验证email的合法性
	 * 
	 * @param emailStr
	 * @return
	 */
	public static boolean checkEmail(String emailStr) {
		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(emailStr.trim());
		boolean isMatched = matcher.matches();
		if (isMatched) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 对字符串进行MD5加密</br>
	 * 如果返回为空，则表示加密失败
	 * @param s
	 * @return
	 */
	public static String md5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			// 使用MD5创建MessageDigest对象
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte b = md[i];
				// 将每个数(int)b进行双字节加密
				str[k++] = hexDigits[b >> 4 & 0xf];
				str[k++] = hexDigits[b & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 去掉字符串的空格
	 * @param input
	 * @return
	 */
	public static String trim(String input){
		if(input==null) return "";
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<input.length();i++){
			if(input.charAt(i)==' '){
				continue;
			}else{
				sb.append(input.charAt(i));
			}
		}
		return sb.toString();
	}
	
	/**
	 * 从字符串s中截取某一段字符串
	 * @param s
	 * @param startToken 开始标记
	 * @param endToken 结束标记
	 * @return
	 */
    public static String mid(String s, String startToken, String endToken) {
        return mid(s, startToken, endToken, 0);
    }

    public static String mid(String s, String startToken, String endToken, int fromStart) {
        if (startToken==null || endToken==null)
            return null;
        int start = s.indexOf(startToken, fromStart);
        if (start==(-1))
            return null;
        int end = s.indexOf(endToken, start + startToken.length());
        if (end==(-1))
            return null;
        String sub = s.substring(start + startToken.length(), end);
        return sub.trim();
    }
    
    /**
     * 简化字符串，通过删除空格键、tab键、换行键等实现
     * @param s
     * @return
     */
    public static String compact(String s) {
        char[] cs = new char[s.length()];
        int len = 0;
        for(int n=0; n<cs.length; n++) {
            char c = s.charAt(n);
            if(c==' ' || c=='\t' || c=='\r' || c=='\n' || c==CHAR_CHINESE_SPACE)
                continue;
            cs[len] = c;
            len++;
        }
        return new String(cs, 0, len);
    }
    
    /**
     * 生成uuid
     * @return
     */
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "").toLowerCase();
	}
	
	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
	}
	
	/**
	 * 判断字符数不为空
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str){
        return !StringUtils.isEmpty(str);
    }
	
    /**
     * <p>Checks if a String is whitespace, empty ("") or null.</p>
     *
     * <pre>
     * StringHelper.isBlank(null)      = true
     * StringHelper.isBlank("")        = true
     * StringHelper.isBlank(" ")       = true
     * StringHelper.isBlank("bob")     = false
     * StringHelper.isBlank("  bob  ") = false
     * </pre>
     *
     * @param obj  the String to check, may be null
     * @return <code>true</code> if the String is null, empty or whitespace
     */
    public static boolean isBlank(Object obj) {
    	if (null == obj) {
    		return true;
    	}
    	String str = obj.toString();
        int strLen;
        if ((strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * <p>Checks if a String is whitespace, empty ("") or null.</p>
     *
     * <pre>
     * StringHelper.isNotBlank(null)      = false
     * StringHelper.isNotBlank("")        = false
     * StringHelper.isNotBlank(" ")       = false
     * StringHelper.isNotBlank("bob")     = true
     * StringHelper.isNotBlank("  bob  ") = true
     * </pre>
     * 
     * @param obj the String to check, may be not null
     * @return
     */
    public static boolean isNotBlank(Object obj) {
    	return !StringUtils.isBlank(obj);
    }
    
    public static boolean isNotBlank(Object... objs) {
    	if (objs == null || objs.length==0) {
    		return false;
    	}

		for (Object obj:objs) {
			if (isNotBlank(obj)) {
				continue;
			} else {
				return false;
			}
		}

		return true;
    }
	
	/**
	 * 如果str字符串为null,返回为"";如果字符串不为空,返回原来的字符串
	 * @param str
	 * @return
	 */
	public static String nullToBlank(String str) {
		return (str == null ? "" : str);
	}
	
	/**
	 * 求两个字符串数组的并集，利用set的元素唯一性  
	 * @param arr1
	 * @param arr2
	 * @return
	 */
    public static String[] union(String[] arr1, String[] arr2) {  
        Set<String> set = new HashSet<String>();  
        for (String str : arr1) {  
            set.add(str);  
        }  
        for (String str : arr2) {  
            set.add(str);  
        }
        String[] result = {};  
        return set.toArray(result);  
    }
    
    /**
     * 求两个字符串数组的交集 
     * @param arr1
     * @param arr2
     * @return
     */
    public static String[] intersect(String[] arr1, String[] arr2) {  
        Map<String, Boolean> map = new HashMap<String, Boolean>();  
        LinkedList<String> list = new LinkedList<String>();  
        for (String str : arr1) {  
            if (!map.containsKey(str)) {  
                map.put(str, Boolean.FALSE);  
            }  
        }
        
        for (String str : arr2) {  
            if (map.containsKey(str)) {  
                map.put(str, Boolean.TRUE);  
            }  
        }  
  
        for (Entry<String, Boolean> e : map.entrySet()) {  
            if (e.getValue().equals(Boolean.TRUE)) {  
                list.add(e.getKey());  
            }  
        }  
  
        String[] result = {};  
        return list.toArray(result);  
    }
    
    /**
     * 求两个字符串数组的差集  
     * @param arr1
     * @param arr2
     * @return
     */
    public static String[] minus(String[] arr1, String[] arr2) {  
        LinkedList<String> list = new LinkedList<String>();  
        LinkedList<String> history = new LinkedList<String>();  
        String[] longerArr = arr1;  
        String[] shorterArr = arr2;  
        //找出较长的数组来减较短的数组  
        if (arr1.length > arr2.length) {  
            longerArr = arr2;  
            shorterArr = arr1;  
        }  
        for (String str : longerArr) {  
            if (!list.contains(str)) {  
                list.add(str);  
            }  
        }  
        for (String str : shorterArr) {  
            if (list.contains(str)) {  
                history.add(str);  
                list.remove(str);  
            } else {  
                if (!history.contains(str)) {  
                    list.add(str);  
                }  
            }  
        }  
  
        String[] result = {};  
        return list.toArray(result);  
    }
    
    /**
     * 字符串反转
     * @param str
     * @return
     */
    public static String reverse(String str){
    	return new StringBuffer(str).reverse().toString();  
    }
    
	/**
	 * 字符串数组反转
	 * @param strs
	 * @return
	 */
	public static String[] reverse(String[] strs) {
		for (int i = 0; i < strs.length; i++) {
			String top = strs[0];
			for (int j = 1; j < strs.length - i; j++) {
				strs[j - 1] = strs[j];
			}
			strs[strs.length - i - 1] = top;
		}
		return strs;
	}

    /**
     * html的转义字符转换成正常的字符串
     * 
     * @param html
     * @return
     */
    public static String htmlEscapeCharsToString(String html) {
        if (isEmpty(html)) {
            return html;
        } else {
            return html.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&quot;","\"");
        }
    }
    
	/**
	 * 判断字符串是否为数字,可以判断正数、负数、int、float、double
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$"); 
		return pattern.matcher(str).matches();
	}
	
    public static boolean isNumber(String str) {
        if (isLong(str)) {
            return true;
        }
        Pattern pattern = Pattern.compile("(-)?(\\d*)\\.{0,1}(\\d*)");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
    
    public static boolean isLong(String str) {
        if ("0".equals(str.trim())) {
            return true;
        }
        Pattern pattern = Pattern.compile("^[^0]\\d*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isFloat(String str) {
        if (isLong(str)) {
            return true;
        }
        Pattern pattern = Pattern.compile("\\d*\\.{1}\\d+");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}

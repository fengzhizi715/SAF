/**
 * 
 */
package cn.salesuite.saf.http.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 构造url的builder，用法:
 * <pre>
 * <code>
 * UrlBuilder builder = new UrlBuilder("http://localhost:8080/test");
 * builder.parameter("one", "one");
 * builder.parameter("two", "two");
 * String url = builder.buildUrl();
 * </code>
 * </pre>
 * <pre>
 * <code>
 * UrlBuilder builder = new UrlBuilder("http://localhost:8080/test.json?apiVersion=1.0");
 * builder.parameter("one", "one");
 * builder.parameter("two", "two");
 * String url = builder.buildUrl();
 * </code>
 * </pre>
 * <pre>
 * <code>
 * UrlBuilder builder = new UrlBuilder("http://localhost:8080/test.json?apiVersion=1.0&");
 * builder.parameter("one", "one");
 * builder.parameter("two", "two");
 * String url = builder.buildUrl();
 * </code>
 * </pre>
 * @author Tony Shen
 *
 */
public class UrlBuilder {

    private final String urlFormat;
    private Map<String, String> parametersMap = new HashMap<String, String>();
    private boolean firstParameter = true;
    
    public UrlBuilder(String urlFormat) {
		this.urlFormat = urlFormat;
		int index = urlFormat.indexOf("?");
		if(index!=-1) {
			String parameter = urlFormat.substring(index+1);
			String[] params = parameter.split("&");  
	        for (int i = 0; i < params.length; i++) {  
	            String[] p = params[i].split("=");  
	            if (p.length == 2) {
	    			firstParameter = false;
	    			break;
	            }  
	        }
		}
	}
    
	/**
	 * 增加一个url参数
	 * 
	 * @param name Name.
	 * @param value Value.
	 * @return Current instance for builder pattern.
	 */
    public UrlBuilder parameter(String name, String value) {
    	return this.parameter(name, value, true);
	}

	/**
	 * 增加一个url参数
	 * 
	 * @param name Name.
	 * @param value Value.
	 * @param escape Whether the value needs to be escaped.
	 * @return Current instance for builder pattern.
	 */
    public UrlBuilder parameter(String name, String value, boolean escape) {
    	if (escape) {
    		this.parametersMap.put(name, UrlBuilder.encodeUrl(value));
    	} else {
    		this.parametersMap.put(name, value);
    	}
    	
    	return this;
    }

	/**
	 * 增加一个url参数
	 * 
	 * @param name Name.
	 * @param value Value.
	 * @return Current instance for builder pattern.
	 */
    public UrlBuilder parameter(String name, int value) {
		return this.parameter(name, Integer.toString(value));
	}

	/**
	 * 增加一个url参数
	 * 
	 * @param name Name.
	 * @param value Value.
	 * @return Current instance for builder pattern.
	 */
    public UrlBuilder parameter(String name, boolean value) {
		return this.parameter(name, Boolean.toString(value));
	}
    
    public UrlBuilder parameter(Map<String, String> map) {
    	parametersMap.putAll(map);
    	return this;
    }
    
	/**
	 * 判断url中是否存在这个Parameter
	 * 
	 * @param name Name of key.
	 * @return True if the key exists in the parameter set.
	 */
    protected boolean hasParameter(String name) {
		return this.parametersMap.containsKey(name);
	}

	/**
	 * 构造url
	 * 
	 * @return String representation of the URL.
	 */
    public String buildUrl() {
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(this.urlFormat);
		if (urlFormat.endsWith("&")) {
			urlBuilder.deleteCharAt(urlBuilder.length()-1);
		}
		
		if (this.parametersMap.size() > 0) {
			for (String parameterName : this.parametersMap.keySet()) {
				if (firstParameter) {
					firstParameter = false;
					urlBuilder.append("?");
				} else {
					urlBuilder.append("&");
				}

				urlBuilder.append(parameterName);
				urlBuilder.append("=");
				urlBuilder.append(this.parametersMap.get(parameterName));
			}
		}
		return urlBuilder.toString();
	}

    /**
	 * Encode URL内容
	 * 
	 * @param content Content to encode.
	 * @return Encoded string.
	 */
    protected static String encodeUrl(String content) {
    	try {
			return URLEncoder.encode(content, RestConstant.CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			return content;
		}
    }
}

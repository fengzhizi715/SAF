/**
 * 
 */
package cn.salesuite.saf.http.rest;

/**
 * @author Tony Shen
 * 
 */
public class RestConstant {

	/**
	 * 'UTF-8' charset name
	 */
	public static final String CHARSET_UTF8 = "UTF-8";

	/**
	 * 'application/x-www-form-urlencoded' content type header value
	 */
	public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";

	/**
	 * 'application/json' content type header value
	 */
	public static final String CONTENT_TYPE_JSON = "application/json";

	/**
	 * 'gzip' encoding header value
	 */
	public static final String ENCODING_GZIP = "gzip";

	/**
	 * 'Accept' header name
	 */
	public static final String HEADER_ACCEPT = "Accept";

	/**
	 * 'Accept-Charset' header name
	 */
	public static final String HEADER_ACCEPT_CHARSET = "Accept-Charset";

	/**
	 * 'Accept-Encoding' header name
	 */
	public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";

	/**
	 * 'Authorization' header name
	 */
	public static final String HEADER_AUTHORIZATION = "Authorization";

	/**
	 * 'Cache-Control' header name
	 */
	public static final String HEADER_CACHE_CONTROL = "Cache-Control";

	/**
	 * 'Content-Encoding' header name
	 */
	public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";

	/**
	 * 'Content-Length' header name
	 */
	public static final String HEADER_CONTENT_LENGTH = "Content-Length";

	/**
	 * 'Content-Type' header name
	 */
	public static final String HEADER_CONTENT_TYPE = "Content-Type";

	/**
	 * 'Date' header name
	 */
	public static final String HEADER_DATE = "Date";

	/**
	 * 'ETag' header name
	 */
	public static final String HEADER_ETAG = "ETag";

	/**
	 * 'Expires' header name
	 */
	public static final String HEADER_EXPIRES = "Expires";

	/**
	 * 'If-None-Match' header name
	 */
	public static final String HEADER_IF_NONE_MATCH = "If-None-Match";

	/**
	 * 'Last-Modified' header name
	 */
	public static final String HEADER_LAST_MODIFIED = "Last-Modified";

	/**
	 * 'Location' header name
	 */
	public static final String HEADER_LOCATION = "Location";

	/**
	 * 'Server' header name
	 */
	public static final String HEADER_SERVER = "Server";

	/**
	 * 'User-Agent' header name
	 */
	public static final String HEADER_USER_AGENT = "User-Agent";

	/**
	 * 'DELETE' request method
	 */
	public static final String METHOD_DELETE = "DELETE";

	/**
	 * 'GET' request method
	 */
	public static final String METHOD_GET = "GET";

	/**
	 * 'HEAD' request method
	 */
	public static final String METHOD_HEAD = "HEAD";

	/**
	 * 'OPTIONS' options method
	 */
	public static final String METHOD_OPTIONS = "OPTIONS";

	/**
	 * 'POST' request method
	 */
	public static final String METHOD_POST = "POST";

	/**
	 * 'PUT' request method
	 */
	public static final String METHOD_PUT = "PUT";

	/**
	 * 'TRACE' request method
	 */
	public static final String METHOD_TRACE = "TRACE";

	/**
	 * 'charset' header value parameter
	 */
	public static final String PARAM_CHARSET = "charset";

	public static final String BOUNDARY = "00content0boundary00";

	public static final String CONTENT_TYPE_MULTIPART = "multipart/form-data; boundary="
			+ BOUNDARY;

	public static final String CRLF = "\r\n";
	
	public static int DEFAULT_READ_TIMEOUT = 30000;
	
	public static int DEFAULT_CONNECTION_TIMEOUT = 30000;
	
	public static int DEFAULT_RETRY_NUM = 3;
	
	public static final int SUCCESS = 200;
}

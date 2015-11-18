/**
 * 
 */
package cn.salesuite.saf.http.rest;

import java.io.IOException;

/**
 * @author Tony Shen
 *
 */
public class RestException extends RuntimeException{

	private static final long serialVersionUID = 8520390726356829268L;
	public static final String RETRY_CONNECTION = "retry";
	private String errorCode;

	protected RestException(IOException cause) {
		super(cause);
	}
	
	protected RestException(IOException cause,String code) {
		super(cause);
		this.errorCode = code;
	}

	protected RestException(String code) {
		super();
		this.errorCode = code;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	@Override
	public IOException getCause() {
		return (IOException) super.getCause();
	}
}

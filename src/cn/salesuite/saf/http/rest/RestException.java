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

	protected RestException(IOException cause) {
		super(cause);
	}

	@Override
	public IOException getCause() {
		return (IOException) super.getCause();
	}
}

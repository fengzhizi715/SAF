/**
 * 
 */
package cn.salesuite.saf.http.rest;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * 封装BufferedOutputStream
 * @author Tony Shen
 *
 */
public class RestOutputStream extends BufferedOutputStream{
	private final CharsetEncoder encoder;

	public RestOutputStream(OutputStream stream, String charset,
			int bufferSize) {
		super(stream, bufferSize);

		encoder = Charset.forName(RestUtil.getValidCharset(charset)).newEncoder();
	}

	/**
	 * 将string的值写入stream
	 * 
	 * @param value
	 * @return this stream
	 * @throws IOException
	 */
	public RestOutputStream write(String value) throws IOException {
		final ByteBuffer bytes = encoder.encode(CharBuffer.wrap(value));

		super.write(bytes.array(), 0, bytes.limit());

		return this;
	}
}

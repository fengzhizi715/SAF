/*
 * Copyright (C) 2012 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package retrofit.converter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import retrofit.mime.MimeUtil;

import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

import com.alibaba.fastjson.JSON;

/**
 * TODO frankswu : change GSON to fastjson
 * 
 * A {@link Converter} which uses GSON for serialization and deserialization of
 * entities.
 * 
 * @author Jake Wharton (jw@squareup.com)
 */
public class FastjsonConverter implements Converter {

	private String charset;
	private static final int BUFFER_SIZE = 0x400; // 1024

	/**
	 * Create an instance using the supplied {@link JSON} object for conversion.
	 * Encoding to JSON and decoding from JSON (when no charset is specified by
	 * a header) will use UTF-8.
	 */
	public FastjsonConverter() {
		this("UTF-8");
	}

	/**
	 * Create an instance using the supplied {@link JSON} object for conversion.
	 * Encoding to JSON and decoding from JSON (when no charset is specified by
	 * a header) will use the specified charset.
	 */
	public FastjsonConverter(String charset) {
		this.charset = charset;
	}

	@Override
	public Object fromBody(TypedInput body, Type type)
			throws ConversionException {
		String charset = this.charset;
		if (body.mimeType() != null) {
			charset = MimeUtil.parseCharset(body.mimeType(), charset);
		}
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(body.in(), charset);
			StringBuilder sb = new StringBuilder();
			char[] buf = new char[BUFFER_SIZE];
			for (;;) {
				int res = isr.read(buf, 0, buf.length);
				if (res < 0) {
					break;
				}
				sb.append(buf, 0, res);
			}

			return JSON.parseObject(sb.toString(), type);
		} catch (IOException e) {
			throw new ConversionException(e);
		} catch (Exception e) {
			throw new ConversionException(e);
		} finally {
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException ignored) {
				}
			}
		}
	}

	@Override
	public TypedOutput toBody(Object object) {
		try {
			return new JsonTypedOutput(JSON.toJSONString(object).getBytes(
					charset), charset);
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
	}

	private static class JsonTypedOutput implements TypedOutput {
		private final byte[] jsonBytes;
		private final String mimeType;

		JsonTypedOutput(byte[] jsonBytes, String encode) {
			this.jsonBytes = jsonBytes;
			this.mimeType = "application/json; charset=" + encode;
		}

		@Override
		public String fileName() {
			return null;
		}

		@Override
		public String mimeType() {
			return mimeType;
		}

		@Override
		public long length() {
			return jsonBytes.length;
		}

		@Override
		public void writeTo(OutputStream out) throws IOException {
			out.write(jsonBytes);
		}
	}
}
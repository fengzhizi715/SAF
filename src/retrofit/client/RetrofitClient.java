/**
 * 
 */
package retrofit.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;
import cn.salesuite.saf.http.rest.RestClient;

/**
 * @author Tony Shen
 * 
 */
public class RetrofitClient implements Client {

	private static final int CHUNK_SIZE = 4096;

	public RetrofitClient() {
	}

	@Override
	public Response execute(Request request) throws IOException {
		RestClient restClient = openConnection(request);
		prepareRequest(restClient, request);
		return readResponse(restClient, request);
	}

	protected RestClient openConnection(Request request) throws IOException {
		return new RestClient(request.getUrl(), request.getMethod());
	}

	void prepareRequest(RestClient restClient, Request request)
			throws IOException {

		for (Header header : request.getHeaders()) {
			restClient.header(header.getName(), header.getValue());
		}

		TypedOutput body = request.getBody();
		if (body != null) {
			body.writeTo(restClient.getOutput());
		}
	}

	Response readResponse(RestClient restClient, Request request)
			throws IOException {
		int status = restClient.code();
		String reason = restClient.getConnection().getResponseMessage();
		if (reason == null)
			reason = ""; // HttpURLConnection treats empty reason as null.

		List<Header> headers = new ArrayList<Header>();
		for (Map.Entry<String, List<String>> field : restClient.headers()
				.entrySet()) {
			String name = field.getKey();
			for (String value : field.getValue()) {
				headers.add(new Header(name, value));
			}
		}

		String mimeType = restClient.contentType();
		int length = restClient.contentLength();
		InputStream stream = restClient.stream();
		TypedInput responseBody = new TypedInputStream(mimeType, length, stream);
		return new Response(request.getUrl(), status, reason, headers,
				responseBody);
	}

	private static class TypedInputStream implements TypedInput {
		private final String mimeType;
		private final long length;
		private final InputStream stream;

		private TypedInputStream(String mimeType, long length,
				InputStream stream) {
			this.mimeType = mimeType;
			this.length = length;
			this.stream = stream;
		}

		@Override
		public String mimeType() {
			return mimeType;
		}

		@Override
		public long length() {
			return length;
		}

		@Override
		public InputStream in() throws IOException {
			return stream;
		}
	}

}

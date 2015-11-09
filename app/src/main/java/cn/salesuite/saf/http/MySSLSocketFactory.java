/**
 * 
 */
package cn.salesuite.saf.http;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.*;
import javax.net.ssl.*;

/**
 * @author Tony Shen
 * 
 */
public class MySSLSocketFactory extends
		org.apache.http.conn.ssl.SSLSocketFactory {

	private SSLSocketFactory sslFactory = HttpsURLConnection
			.getDefaultSSLSocketFactory();

	public MySSLSocketFactory(KeyStore truststore)
			throws NoSuchAlgorithmException, KeyManagementException,
			KeyStoreException, UnrecoverableKeyException {
		super(truststore);

		try {
			SSLContext context = SSLContext.getInstance("TLS");

			// Create a trust manager that does not validate certificate chains
			// and simply
			// accept all type of certificates
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new java.security.cert.X509Certificate[] {};
				}

				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}
			} };

			// Initialize the socket factory
			context.init(null, trustAllCerts, new SecureRandom());
			sslFactory = context.getSocketFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Socket createSocket(Socket socket, String host, int port,
			boolean autoClose) throws IOException, UnknownHostException {
		return sslFactory.createSocket(socket, host, port, autoClose);
	}

	@Override
	public Socket createSocket() throws IOException {
		return sslFactory.createSocket();
	}
}

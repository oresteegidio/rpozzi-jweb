package com.rp.rest.client;

import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.SslConfigurator;

/*===== Jersey based client =====*/
public class RestClient {
	private String url;
	private boolean isSSLEnabled;
	private String truststore_path = "C:/Users/IBM_ADMIN/Downloads/Bluemix_SecureGatewayCerts_IT056547_WASLiberty_TLS/sg_trust.jks";
	private String truststore_password = "password";
	private String keystore_path = "C:/Users/IBM_ADMIN/Downloads/Bluemix_SecureGatewayCerts_IT056547_WASLiberty_TLS/sg_key.p12";
	private String keystore_password = "password";
	
	public RestClient(String url) {
		this(url, false);
	}
	
	public RestClient(String url, boolean isSSLEnabled) {
		super();
		this.url = url;
		this.isSSLEnabled = isSSLEnabled;
	}
	
	private Client buildClient() {
		Client client = null;
		if (isSSLEnabled) {
			try {
				/*ClientConfig config = new DefaultClientConfig();
				SSLContext ctx = SSLContext.getInstance("SSL");
				KeyManager myKeyManager[] = new KeyManager[]{new MyX509KeyManager(keystore_path, keystore_password.toCharArray())};
				TrustManager myTrustManager[] = new TrustManager[]{new MyX509TrustManager(truststore_path, truststore_password.toCharArray())};
		        ctx.init(myKeyManager, myTrustManager, null);
				config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(getHostnameVerifier(), ctx));
				client = Client.create(config);*/
				SslConfigurator sslConfig = SslConfigurator.newInstance()
				        .trustStoreFile(truststore_path)
				        .trustStorePassword(truststore_password)
				        .keyStoreFile(keystore_path)
				        .keyPassword(keystore_password);
				SSLContext sslContext = sslConfig.createSSLContext();
				client = ClientBuilder.newBuilder().sslContext(sslContext).build();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			client = ClientBuilder.newClient();
		}
		return client;
	}
	
	public String GETinvoke() {
		// create the rest client instance
		Client client = buildClient();
		// create the Builder instance to interact with
		WebTarget webTarget = client.target(url);
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		// perform a GET on the resource. The resource will be returned as plain text
		Response response = invocationBuilder.get();
		System.out.println(response.getStatus());
		String jsonStr = response.readEntity(String.class);
		System.out.println(jsonStr);
		return jsonStr;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isSSLEnabled() {
		return isSSLEnabled;
	}

	public void setSSLEnabled(boolean isSSLEnabled) {
		this.isSSLEnabled = isSSLEnabled;
	}
	
	private static HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
        };
    }
	
	/**
     * Taken from http://java.sun.com/javase/6/docs/technotes/guides/security/jsse/JSSERefGuide.html
     *
     */
    static class MyX509TrustManager implements X509TrustManager {
        /*
         * The default PKIX X509TrustManager9.  We'll delegate
         * decisions to it, and fall back to the logic in this class if the
         * default X509TrustManager doesn't trust it.
         */
        X509TrustManager pkixTrustManager;

        MyX509TrustManager(String trustStore, char[] password) throws Exception {
            this(new File(trustStore), password);
        }

        MyX509TrustManager(File trustStore, char[] password) throws Exception {
            // create a "default" JSSE X509TrustManager.
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(trustStore), password);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
            tmf.init(ks);

            TrustManager tms[] = tmf.getTrustManagers();

            /*
             * Iterate over the returned trustmanagers, look
             * for an instance of X509TrustManager.  If found,
             * use that as our "default" trust manager.
             */
            for (int i = 0; i < tms.length; i++) {
                if (tms[i] instanceof X509TrustManager) {
                    pkixTrustManager = (X509TrustManager) tms[i];
                    return;
                }
            }

            /*
             * Find some other way to initialize, or else we have to fail the
             * constructor.
             */
            throw new Exception("Couldn't initialize");
        }

        /*
         * Delegate to the default trust manager.
         */
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            try {
                pkixTrustManager.checkClientTrusted(chain, authType);
            } catch (CertificateException excep) {
                // do any special handling here, or rethrow exception.
            }
        }

        /*
         * Delegate to the default trust manager.
         */
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            try {
                pkixTrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException excep) {
                /*
                 * Possibly pop up a dialog box asking whether to trust the
                 * cert chain.
                 */
            }
        }

        /*
         * Merely pass this through.
         */
        public X509Certificate[] getAcceptedIssuers() {
            return pkixTrustManager.getAcceptedIssuers();
        }
    }

    /**
     * Inspired from http://java.sun.com/javase/6/docs/technotes/guides/security/jsse/JSSERefGuide.html
     *
     */
    static class MyX509KeyManager implements X509KeyManager {
        /*
         * The default PKIX X509KeyManager.  We'll delegate
         * decisions to it, and fall back to the logic in this class if the
         * default X509KeyManager doesn't trust it.
         */
        X509KeyManager pkixKeyManager;

        MyX509KeyManager(String keyStore, char[] password) throws Exception {
            this(new File(keyStore), password);
        }

        MyX509KeyManager(File keyStore, char[] password) throws Exception {
            // create a "default" JSSE X509KeyManager.
            //KeyStore ks = KeyStore.getInstance("JKS");
        	KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(keyStore), password);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509", "SunJSSE");
            kmf.init(ks, password);

            KeyManager kms[] = kmf.getKeyManagers();

            /*
             * Iterate over the returned keymanagers, look
             * for an instance of X509KeyManager.  If found,
             * use that as our "default" key manager.
             */
            for (int i = 0; i < kms.length; i++) {
                if (kms[i] instanceof X509KeyManager) {
                    pkixKeyManager = (X509KeyManager) kms[i];
                    return;
                }
            }

            /*
             * Find some other way to initialize, or else we have to fail the
             * constructor.
             */
            throw new Exception("Couldn't initialize");
        }

        public PrivateKey getPrivateKey(String arg0) {
            return pkixKeyManager.getPrivateKey(arg0);
        }

        public X509Certificate[] getCertificateChain(String arg0) {
            return pkixKeyManager.getCertificateChain(arg0);
        }

        public String[] getClientAliases(String arg0, Principal[] arg1) {
            return pkixKeyManager.getClientAliases(arg0, arg1);
        }

        public String chooseClientAlias(String[] arg0, Principal[] arg1, Socket arg2) {
            return pkixKeyManager.chooseClientAlias(arg0, arg1, arg2);
        }

        public String[] getServerAliases(String arg0, Principal[] arg1) {
            return pkixKeyManager.getServerAliases(arg0, arg1);
        }

        public String chooseServerAlias(String arg0, Principal[] arg1, Socket arg2) {
            return pkixKeyManager.chooseServerAlias(arg0, arg1, arg2);
        }
    }
}
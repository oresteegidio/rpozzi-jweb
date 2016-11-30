package com.rp.securegateway.client;

import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.rp.rest.jersey.client.JerseyRestClient;

public class SecureGatewayClient {
	static final String baseDir = "secgateway-client-endpoints";
	static final String truststorePath = baseDir + "/truststore/sg_trust.jks";
	static final String truststorePassword = "password";
	static final String nodejsIT056547AppNoTLSEndpoint = "http://cap-sg-prd-3.integration.ibmcloud.com:15602";
	static final String nodejsIT056547AppTLSMutualAuthEndpoint = "https://cap-sg-prd-3.integration.ibmcloud.com:15626";
	static final String wasIT056547RestaurantsTLSMutualAuthEndpoint = "https://cap-sg-prd-3.integration.ibmcloud.com:15606";
	static final String wasIT056547CantieriTLSMutualAuthEndpoint = "https://cap-sg-prd-3.integration.ibmcloud.com:15630";

	public static void main(String[] args) {
		_GET_BluemixSecurityGateway_NodeJs_NoTLS();
		_GET_BluemixSecurityGateway_NodeJs_TLSMutualAuth();
		//_GET_BluemixSecurityGateway_WasRestaurants_TLSMutualAuth();
		//_GET_BluemixSecurityGateway_WasCantieri_TLSMutualAuth();
	}
	
	/*
	 * Calls a NO TLS endpoint deployed on a local workstation through Bluemix Secure Gateway
	 * This needs to start NodeJs rpozzi-nodejs application on local workstation
	 * On Mac:
	 * - cd to /Users/robertopozzi/git/bluemix/rpozzi-nodejs
	 * - start application issuing 'node app'
	 * */
	private static void _GET_BluemixSecurityGateway_NodeJs_NoTLS() {
		System.out.println("############### Call Bluemix Secure Gateway (NO TLS NodeJs Endpoint) - START");
		try {
			String allItemsRestPath = nodejsIT056547AppNoTLSEndpoint + "/items";
			// Call Jersey Rest Client
			String jsonStr = new JerseyRestClient().callREST_GET_NoAuth_NoSSL(allItemsRestPath, null);
			// Parsing and printing result
			JSONParser json = new JSONParser();
			JSONObject jsonResponse = (JSONObject) json.parse(jsonStr);
			JSONArray jsonArray = (JSONArray) jsonResponse.get("rows");
			for (Iterator<JSONObject> iterator = jsonArray.iterator(); iterator.hasNext();) {
				JSONObject jsonObj = iterator.next();
				String obj = jsonObj.toJSONString();
				System.out.println(obj);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("############### Call Bluemix Secure Gateway (NO TLS NodeJs Endpoint) - END");
		}
	}
	
	/*
	 * Calls a secure endpoint (TLS Mutual Authentication) deployed on a local workstation through Bluemix Secure Gateway
	 * This needs to start NodeJs rpozzi-nodejs application on local workstation
	 * On Mac Workstation:
	 * - cd to /Users/robertopozzi/git/bluemix/rpozzi-nodejs
	 * - start application issuing 'node app'
	 * */
	private static void _GET_BluemixSecurityGateway_NodeJs_TLSMutualAuth() {
		System.out.println("############### Call Items REST service through Bluemix Secure Gateway (TLS Mutual Auth NodeJs Endpoint) - START");
		try {
			String allItemsRestPath = nodejsIT056547AppTLSMutualAuthEndpoint + "/items";
			// KeyStore File Handling
			String fileName = "sg_key.p12";
			File keyStoreFile = new File(ClassLoader.getSystemResource(baseDir + "/RPozzi_Mac_Nodejs_TLS/" + fileName).getFile());
			String keyStoreFilePath = keyStoreFile.getPath();
			String keystorePassword = "password";
			// TrustStore File Handling
			File trustStoreFile = new File(ClassLoader.getSystemResource(truststorePath).getFile());
			String trustStoreFilePath = trustStoreFile.getPath();
			// Call Jersey Rest Client
			String jsonStr = new JerseyRestClient(keyStoreFilePath, keystorePassword, trustStoreFilePath, truststorePassword).callREST_GET_NoAuth_TLS(allItemsRestPath);
			// Parsing and printing result
			JSONParser json = new JSONParser();
			JSONObject jsonResponse = (JSONObject) json.parse(jsonStr);
			JSONArray jsonArray = (JSONArray) jsonResponse.get("rows");
			for (Iterator<JSONObject> iterator = jsonArray.iterator(); iterator.hasNext();) {
				JSONObject jsonObj = iterator.next();
				String obj = jsonObj.toJSONString();
				System.out.println(obj);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("############### Call Items REST service through Bluemix Secure Gateway (TLS Mutual Auth NodeJs Endpoint) - END");
		}
	}
	
	/*
	 * Calls a secure endpoint (TLS Mutual Authentication) deployed on a local workstation through Bluemix Secure Gateway
	 * This needs to start WAS rpozzi-restaurants application on local workstation
	 * On Mac:
	 * - Start Eclipse
	 * - Start WAS Test Environment 'WAS Liberty V9_BETA_2'
	 * */
	private static void _GET_BluemixSecurityGateway_WasRestaurants_TLSMutualAuth() {
		System.out.println("=============== Call Restaurants REST service through Bluemix Secure Gateway (TLS Mutual Auth WAS Endpoint) - START");
		try {
			String restaurantsRestPath = wasIT056547RestaurantsTLSMutualAuthEndpoint + "/rest/restaurants";
			// KeyStore File Handling
			String fileName = "sg_key.p12";
			File keyStoreFile = new File(ClassLoader.getSystemResource(baseDir + "/RPozzi_Mac_WASLibertyRestaurants_TLS/" + fileName).getFile());
			String keyStoreFilePath = keyStoreFile.getPath();
			String keystorePassword = "password";
			// TrustStore File Handling
			File trustStoreFile = new File(ClassLoader.getSystemResource(truststorePath).getFile());
			String trustStoreFilePath = trustStoreFile.getPath();
			// Call Jersey Rest Client
			String jsonStr = new JerseyRestClient(keyStoreFilePath, keystorePassword, trustStoreFilePath, truststorePassword).callREST_GET_NoAuth_TLS(restaurantsRestPath);
			// Parsing and printing result
			JSONParser json = new JSONParser();
			JSONArray jsonArray;
			jsonArray = (JSONArray) json.parse(jsonStr);
			for (Iterator<JSONObject> iterator = jsonArray.iterator(); iterator.hasNext();) {
				JSONObject jsonObj = iterator.next();
				String obj = jsonObj.toJSONString();
				System.out.println(obj);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("=============== Call Restaurants REST service through Bluemix Secure Gateway (TLS Mutual Auth WAS Endpoint) - END");
		}
	}
	
	/*
	 * Calls a secure endpoint (TLS Mutual Authentication) deployed on a local workstation through Bluemix Secure Gateway
	 * This needs to start WAS BolognaCantieri application on local workstation
	 * On Mac:
	 * - Start Eclipse
	 * - Start WAS Test Environment 'WAS Liberry V8.5.5.1'
	 * */
	private static void _GET_BluemixSecurityGateway_WasCantieri_TLSMutualAuth() {
		System.out.println("=============== Call Cantieri REST service through Bluemix Secure Gateway (TLS Mutual Auth WAS Endpoint) - START");
		try {
			String cantieriRiqUrbanaRestPath = wasIT056547CantieriTLSMutualAuthEndpoint + "/api/cantieri/riqurbana";
			// KeyStore File Handling
			String fileName = "sg_key.p12";
			File keyStoreFile = new File(ClassLoader.getSystemResource(baseDir + "/RPozzi_Mac_WASLibertyCantieri_TLS/" + fileName).getFile());
			String keyStoreFilePath = keyStoreFile.getPath();
			String keystorePassword = "password";
			// TrustStore File Handling
			File trustStoreFile = new File(ClassLoader.getSystemResource(truststorePath).getFile());
			String trustStoreFilePath = trustStoreFile.getPath();
			// Call Jersey Rest Client
			String jsonStr = new JerseyRestClient(keyStoreFilePath, keystorePassword, trustStoreFilePath, truststorePassword).callREST_GET_NoAuth_TLS(cantieriRiqUrbanaRestPath);
			// Parsing and printing result
			JSONParser json = new JSONParser();
			JSONArray jsonArray;
			jsonArray = (JSONArray) json.parse(jsonStr);
			for (Iterator<JSONObject> iterator = jsonArray.iterator(); iterator.hasNext();) {
				JSONObject jsonObj = iterator.next();
				String obj = jsonObj.toJSONString();
				System.out.println(obj);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("=============== Call Cantieri REST service through Bluemix Secure Gateway (TLS Mutual Auth WAS Endpoint) - END");
		}
	}
	
	/*private static void callRestThroughTLSBluemixSecureGateway() {
		System.out.println("############### Call Bluemix Secure Gateway TLS Endpoint - START");
		try {
			String path = "https://cap-sg-prd-4.integration.ibmcloud.com:15143/BolognaCantieri/api/cantieri/getCantieri/riqurbana";;
			ClientConfig config = new DefaultClientConfig();
			SSLContext ctx = SSLContext.getInstance("SSL");
			KeyManager myKeyManager[] = new KeyManager[]{new MyX509KeyManager(keystore_path, keystore_password.toCharArray())};
			TrustManager myTrustManager[] = new TrustManager[]{new MyX509TrustManager(truststorePath, truststorePassword.toCharArray())};
	        ctx.init(myKeyManager, myTrustManager, null);
			config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(getHostnameVerifier(), ctx));
			Client client = Client.create(config);
			Builder builder = client.resource(path).accept("application/json");
			ClientResponse response = builder.get(ClientResponse.class);
			String jsonStr = response.getEntity(String.class);
			System.out.println(jsonStr);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("############### Call Bluemix Secure Gateway TLS Endpoint - END");
		}
	}*/
	
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

package alex.ssltoolmessage2;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;

public class SimpleHttpClient {

    public static final int HTTP_TIMEOUT = 5 * 1000; // milliseconds

    private static HttpClient mHttpClient;

    private static HttpClient getHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, "utf-8");

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    public static String executeHttpPost(String url, ArrayList<NameValuePair> postParameters) throws Exception {
        BufferedReader in = null;
        try {
            HttpClient client = getHttpClient();
            System.out.println(checkCert(url));
            if (true) {
                HttpPost request = new HttpPost(url);
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                request.setEntity(formEntity);
                HttpResponse response = client.execute(request);
                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
                in.close();

                String result = sb.toString();
                return result;
            } else {
                return "Invalid Certificate";
            }
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean checkCert(String address) {
        boolean check = false;
        try {
            disableCertificateValidation();
            URL url = new URL(address);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            //Open connection
            InputStream in = con.getInputStream();

            //Grabs details of certificate
            String DN = "";
            Date afterDate = null;
            Date notBeforeDate = null;
            X500Principal CA = null;
            Certificate[]certs = con.getServerCertificates();
            for (Certificate cert : certs) {
                if (cert instanceof X509Certificate) {
                    try {
                        afterDate = (((X509Certificate) cert).getNotAfter());
                        notBeforeDate = (((X509Certificate) cert).getNotBefore());
                        DN = (((X509Certificate) cert).getSubjectDN().getName());
                        CA = (((X509Certificate) cert).getIssuerX500Principal());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            String hostname = DN.split(",")[0];
            System.out.println("Cert: " + hostname + " | " + afterDate + "| " + notBeforeDate + " | " + CA);

            if (hostname.equals("https://192.168.227.1/SSLTestServer/AppResponse")) {
                check = true;
            } else {
                check = false;
            }
            if (true && check) {
            } else {
                check = false;
            }
            if (true && check) {
            } else {
                check = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }

    public static void disableCertificateValidation() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }};

        // Ignore differences between given hostname and certificate hostname
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) { return true; }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
        } catch (Exception e) {}
    }
}
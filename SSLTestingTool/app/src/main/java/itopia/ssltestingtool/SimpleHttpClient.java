package itopia.ssltestingtool;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;

public class SimpleHttpClient {

    public static String executeHttpPost (String address, String... postParameters) {
        //toCheck determines which values to check, 0 is hostname, 1 is date, 2 is ca
        Boolean[] toCheck = new Boolean[3];
        toCheck[0] = Boolean.valueOf(postParameters[2]);
        toCheck[1] = Boolean.valueOf(postParameters[3]);
        toCheck[2] = Boolean.valueOf(postParameters[4]);

        String message = ("username=" + postParameters[0] + "&password=" + postParameters[1]);
        String response = "";
        HttpsURLConnection con = null;

        try {
            disableCertificateValidation();

            //Connect to the server
            URL url = new URL(address);
            con = (HttpsURLConnection) url.openConnection();
            con.setDoOutput(true);
            OutputStream out = con.getOutputStream();

            //Grabs details of certificate
            String DN = "";
            Date notAfterDate = null;
            Date notBeforeDate = null;
            X500Principal CA = null;
            Certificate[]certs = con.getServerCertificates();
            for (Certificate cert : certs) {
                if (cert instanceof X509Certificate) {
                    try {
                        notAfterDate = (((X509Certificate) cert).getNotAfter());
                        notBeforeDate = (((X509Certificate) cert).getNotBefore());
                        DN = (((X509Certificate) cert).getSubjectDN().getName());
                        CA = (((X509Certificate) cert).getIssuerX500Principal());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            String commonName = DN.split(",")[0];

            if (checkCert(commonName, notAfterDate, notBeforeDate, CA, toCheck)) {
                //print the username and password to the server
                OutputStreamWriter writer = new OutputStreamWriter(out);
                writer.write(message);
                writer.close();
                out.close();

                //get the response from the server
                InputStream in = con.getInputStream();
                response = new BufferedReader(new InputStreamReader(in)).readLine();
                in.close();
            } else {
                response = "Invalid Certificate";
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = "Unable to connect to server";
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return response;
    }

    private static boolean checkCert (String commonName, Date notAfterDate, Date notBeforeDate, X500Principal CA, Boolean[] toCheck) {
        Boolean check;

        // Statements always evaluate to true if the relevant toCheck value is true,
        // this indicates that the certificate property is not to be checked (as per user input)

        // Check the CN of the certificate against a static value and check the used url against the actual url
        if (commonName.equals("CN=192.168.227.1") || !toCheck[0]) {
            check = true;
        } else {
            check = false;
        }

        // Check whether the certificate is used within its valid lifetime
        Date currentDate = new Date();
        if ((currentDate.before(notAfterDate) && currentDate.after(notBeforeDate) && check) || !toCheck[1]) {
        } else {
            check = false;
        }

        // Check the signing certificate common name against a static value
        if ((CA.toString().split(",")[0].equals("CN=192.168.227.1") && check) || !toCheck[2]) {
        } else {
            check = false;
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
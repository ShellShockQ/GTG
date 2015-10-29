package com.gametimegiving.mobile.Parse;

import android.os.AsyncTask;

import com.gametimegiving.mobile.Application.BaseApplication;
import com.gametimegiving.mobile.R;
import com.gametimegiving.mobile.Utils.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class HttpRequest extends AsyncTask<String, Void, String> {
    private static String TAG = "HttpRequest";
    private String mApiServerUrl;
    private String mMethod;
    private String mArgs;
    private HashMap<Object, Object> mHashMap;

    private List<HttpRequestListener> mListeners = new ArrayList<>();

    public HttpRequest() {
        init(null, null, null);
    }

    public HttpRequest(String method, String args) {

        init(method, args, null);
    }

    public HttpRequest(String method, String args, HashMap<Object, Object> map) {
        init(method, args, map);
    }

    private void init(String method, String args, HashMap<Object, Object> map) {
        mMethod = method;
        mArgs = args;
        mHashMap = map;
        mApiServerUrl = BaseApplication.getInstance().getMetaData(BaseApplication.META_DATA_API_SERVER_URL);
    }

    public synchronized void addEventListener(HttpRequestListener listener) {
        mListeners.add(listener);
    }

    public synchronized void removeEventListener(HttpRequestListener listener) {
        mListeners.remove(listener);
    }

    @Override
    protected String doInBackground(String... arg0) {
        //String url = String.format(java.util.Locale.ENGLISH,  "%s/api/%s?args=%s", mApiServerUrl, mMethod, mArgs);
        //return HttpsGet(url);
        String url = String.format(java.util.Locale.ENGLISH, "%s/api/%s", mApiServerUrl, mMethod);
        if (mHashMap == null) mHashMap = new HashMap<>();
        if (mArgs != null) mHashMap.put("args", mArgs);
        return HttpPost(url, mHashMap);
    }

    protected void onPostExecute(String result) {
        Iterator<HttpRequestListener> i = mListeners.iterator();
        while (i.hasNext()) {
            i.next().onResult(mMethod, result);
        }
    }

    private String HttpsGet(String Url) {
        Log.d(TAG, Url);
        String retval = null;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) (new URL(Url)).openConnection();
            boolean isSSL = (conn instanceof HttpsURLConnection);

            if (isSSL) {

                boolean storeLoaded = false;
                KeyStore store = KeyStore.getInstance("BKS");
                InputStream in = BaseApplication.getContext().getResources().openRawResource(R.raw.keystore);
                try {
                    store.load(in, "secret".toCharArray());
                    storeLoaded = true;
                } finally {
                    in.close();
                }

                if (storeLoaded) {

                    TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    factory.init(store);

                    SSLContext context = SSLContext.getInstance("TLS");
                    context.init(null, factory.getTrustManagers(), null);

                    ((HttpsURLConnection) conn).setSSLSocketFactory(context.getSocketFactory());

                } else {
                    Log.e(TAG, "Store not loaded");
                }
            }

            conn.setUseCaches(false);
            conn.setRequestProperty("Cache-Control", "max-age=0");

            Log.d(TAG, "getInputStream");
            InputStream strm = conn.getInputStream();

            if (isSSL) {
                try {
                    Certificate[] certs = ((HttpsURLConnection) conn).getLocalCertificates();
                    if (certs != null) {
                        for (Certificate cert : certs) {
                            Log.d(TAG, String.format(java.util.Locale.ENGLISH, "local:%s", cert.toString()));
                        }
                    }
                } catch (Exception ex) {
                    Log.e(TAG, ex);
                }
                try {
                    Certificate[] certs = ((HttpsURLConnection) conn).getServerCertificates();
                    if (certs != null) {
                        for (Certificate cert : certs) {
                            Log.d(TAG, String.format(java.util.Locale.ENGLISH, "server:%s", cert.toString()));
                        }
                    }
                } catch (Exception ex) {
                    Log.e(TAG, ex);
                }
            }

            Log.d(TAG, "BufferedInputStream");
            BufferedInputStream bstrm = new BufferedInputStream(strm);
            StringBuilder sb = new StringBuilder();
            int n = 0;
            byte[] buffer = new byte[1024];
            while ((n = bstrm.read(buffer, 0, buffer.length)) != -1) {
                String value = new String(buffer, 0, n);
                sb.append(value);
            }
            retval = sb.toString();

        } catch (Exception exc) {
            retval = exc.toString();
            Log.e(TAG, exc);
        } finally {
            if (conn != null) conn.disconnect();
        }
        //Log.d(TAG, retval);
        return retval;
    }

    private String HttpGet(String Url) {
        Log.d(TAG, Url);

        String retval = null;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) (new URL(Url)).openConnection();
            conn.setUseCaches(false);
            conn.setRequestProperty("Cache-Control", "max-age=0");

            InputStream strm = conn.getInputStream();
            BufferedInputStream bstrm = new BufferedInputStream(strm);

            StringBuilder sb = new StringBuilder();
            int n = 0;
            byte[] buffer = new byte[1024];
            while ((n = bstrm.read(buffer, 0, buffer.length)) != -1) {
                String value = new String(buffer, 0, n);
                sb.append(value);
            }
            retval = sb.toString();
        } catch (Exception exc) {
            retval = exc.toString();
            Log.e(TAG, retval);
        } finally {
            if (conn != null) conn.disconnect();
        }
        //Log.d(TAG, retval);
        return retval;
    }

    @SuppressWarnings("unchecked")
    public String HttpPost(String Url, HashMap<Object, Object> map) {
        Log.d(TAG, Url);
        String retval = null;
        HttpURLConnection conn = null;
        try {

            String boundary = "----WebKitFormBoundarybh8YBIxEOKvEYUAh";

            conn = (HttpURLConnection) (new URL(Url)).openConnection();

            boolean isSSL = (conn instanceof HttpsURLConnection);

            if (isSSL) {

                boolean storeLoaded = false;
                KeyStore store = KeyStore.getInstance("BKS");
                InputStream in = BaseApplication.getContext().getResources().openRawResource(R.raw.keystore);
                try {
                    store.load(in, "secret".toCharArray());
                    storeLoaded = true;
                } finally {
                    in.close();
                }

                if (storeLoaded) {

                    TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    factory.init(store);

                    SSLContext context = SSLContext.getInstance("TLS");
                    context.init(null, factory.getTrustManagers(), null);

                    ((HttpsURLConnection) conn).setSSLSocketFactory(context.getSocketFactory());

                } else {
                    Log.e(TAG, "Store not loaded");
                }
            }

            conn.setDoOutput(true);
            //conn.setChunkedStreamingMode(0);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            conn.setUseCaches(false);
            conn.setRequestProperty("Cache-Control", "max-age=0");
            // conn.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
            // conn.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
            // conn.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
            //conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            //conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.97 Safari/537.22");

            ByteArrayOutputStream mstrm = new ByteArrayOutputStream();
            OutputStreamWriter mwriter = new OutputStreamWriter(mstrm);

            StringBuilder sb = new StringBuilder();

            if (map != null) {
                Set<Object> keyValues = map.keySet();
                for (Object keyValue : keyValues) {
                    String key = keyValue.toString();
                    Object value = map.get(key);

                    if (key.equals("args"))
                        Log.d(TAG, String.format(java.util.Locale.ENGLISH, "%s:%s", key, value));

                    if (key.indexOf("file") == -1) {
                        sb.append("--").append(boundary).append("\r\n");
                        sb.append(String.format(java.util.Locale.ENGLISH, "Content-Disposition: form-data; name=\"%s\"\r\n\r\n", key));
                        sb.append(value.toString()).append("\r\n");
                    }
                }

                String params = sb.toString();
                mwriter.write(params, 0, params.length());
                mwriter.flush();

                byte[] file_data = null;
                String filename = "userfile.jpg";

                if (map.containsKey("userfile")) {
                    file_data = (byte[]) map.get("userfile");
                    if (map.containsKey("userfilename")) {
                        filename = (String) map.get("userfilename");
                    }

                    String line1 = "--" + boundary + "\r\n";
                    String line2 = String.format(java.util.Locale.ENGLISH, "Content-Disposition: form-data; name=\"userfile\"; filename=\"%s\"\r\n", filename);
                    String line3 = "Content-Type: image/jpeg\r\n\r\n";
                    String line4 = "\r\n";

                    mwriter.write(line1, 0, line1.length());
                    mwriter.write(line2, 0, line2.length());
                    mwriter.write(line3, 0, line3.length());
                    mwriter.flush();

                    mstrm.write(file_data);
                    mstrm.flush();

                    mwriter.write(line4, 0, line4.length());
                }

                String closer = "--" + boundary + "--\r\n";
                mwriter.write(closer, 0, closer.length());
                mwriter.flush();
            }

            int content_length = mstrm.size();
            conn.setRequestProperty("Content-length", String.format(java.util.Locale.ENGLISH, "%d", content_length));

            OutputStream ostrm = conn.getOutputStream();
            byte[] mdata = mstrm.toByteArray();
            ostrm.write(mdata, 0, mdata.length);
            ostrm.flush();

            InputStream istrm = conn.getInputStream();
            BufferedInputStream ibstrm = new BufferedInputStream(istrm);

            sb = new StringBuilder();
            int n = 0;
            byte[] buffer = new byte[1024];
            while ((n = ibstrm.read(buffer, 0, buffer.length)) != -1) {
                String value = new String(buffer, 0, n);
                sb.append(value);
            }
            retval = sb.toString();

        } catch (Exception exc) {
            retval = exc.toString();
            Log.e(TAG, retval);
        } finally {
            if (conn != null) conn.disconnect();
        }

        //Log.d(TAG, retval);
        return retval;
    }

}
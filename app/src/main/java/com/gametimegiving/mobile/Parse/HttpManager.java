package com.gametimegiving.mobile.Parse;


import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpManager {
    private static final String TAG = "HTTPManager";

    public static String getData(RequestPackage p) {
//        AndroidHttpClient client = AndroidHttpClient.newInstance("AndroidAgent");
//        HttpGet request = new HttpGet(uri);
//        HttpResponse response;
//       try {
//           response = client.execute(request);
//           return EntityUtils.toString(response.getEntity());
//
//       }catch(Exception e){
//            e.printStackTrace();
//           return null;
//        }finally{
//            client.close();
//        }

        //   }
        BufferedReader reader = null;
        String uri = p.getUri();
        if (p.getMethod().equals("GET")) {
            uri += "?" + p.getEncodedParams();

        }
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(p.getMethod());
            if (p.getMethod().equals("POST")) {
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                // writer.write(p.getEncodedParams());
                //writer.write("{""token"":0,""page"":0,""game_id"":12}");
                writer.write("{token:0,page:0,game_id:12}");
                Log.d(TAG, url.toString());
                // Log.d(TAG, p.getEncodedParams());

                writer.flush();
            }
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {

                sb.append(line + "\n");
            }
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }


        }
    }

    public static String getData(RequestPackage p, String userName, String password) {
//        AndroidHttpClient client = AndroidHttpClient.newInstance("AndroidAgent");
//        HttpGet request = new HttpGet(uri);
//        HttpResponse response;
//       try {
//           response = client.execute(request);
//           return EntityUtils.toString(response.getEntity());
//
//       }catch(Exception e){
//            e.printStackTrace();
//           return null;
//        }finally{
//            client.close();
//        }

        //   }
        BufferedReader reader = null;
        String uri = p.getUri();
        byte[] loginBytes = (userName + ":" + password).getBytes();
        StringBuilder loginBuilder = new StringBuilder()
                .append("Basic ")
                .append(Base64.encodeToString(loginBytes, Base64.DEFAULT));
        HttpURLConnection con = null;
        try {
            URL url = new URL(uri);
            con = (HttpURLConnection) url.openConnection();
            con.addRequestProperty("Authorization", loginBuilder.toString());
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {

                sb.append(line + "\n");
            }
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                int status = con.getResponseCode();
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;

            }

            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }


        }
    }


}

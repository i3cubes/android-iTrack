package com.i3c.itrack.communication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class httpExecutor {
    public String url_str;

    public httpExecutor(String url) {
        this.url_str=url;
    }

    public JSONArray getJSON(Map<String, Object> params){
        try{
            URL url=new URL(this.url_str);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            System.out.println("POST DATA:"+postData.toString());
            System.out.println("POST URL:"+url_str);
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            System.out.println(postDataBytes.toString());

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();


            for (int c; (c = in.read()) >= 0; )
                sb.append((char) c);
            String response = sb.toString();
            System.out.println(response);
            JSONArray jsonArray = new JSONArray(response);

            return jsonArray;

        } catch (MalformedURLException | UnsupportedEncodingException | ProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            Log.e ("Exception", "Ex-No Internet");
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

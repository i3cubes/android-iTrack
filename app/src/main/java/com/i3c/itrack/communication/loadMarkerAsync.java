package com.i3c.itrack.communication;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.i3c.itrack.R;
import com.i3c.itrack.data.GpsData;
import com.i3c.itrack.ui.login.LoggedInUserView;
import com.i3c.itrack.ui.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class loadMarkerAsync extends AsyncTask<String, Void, List<GpsData>> {
    private GoogleMap map;
    Fragment home;
    public loadMarkerAsync(GoogleMap map,Fragment h) {
        this.map=map;
        this.home=h;
    }

    @Override
    protected List<GpsData> doInBackground(String... urls) {
        List<GpsData> markerLocations = new ArrayList<GpsData>();
        System.out.println("LOAD MARKER:"+urls[0]+"/"+urls[1]);
        try {
            httpExecutor http=new httpExecutor(urls[0]);

            Map<String, Object> params=new LinkedHashMap<>();
            params.put("UID",urls[1]);
            //params.put("PS",strings[1]);
            //params.put("UID","314");
            params.put("SID","50");
            JSONArray jsonArray=http.getJSON(params);
            LoginResult login_res = null;
            if(jsonArray!=null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    //Log.i("MARKER", obj.getString("result"));
                    String name = obj.getString("device_name");
                    try {
                        double lat = obj.getDouble("lat");
                        double lng = obj.getDouble("lon");
                        GpsData dev=new GpsData();
                        dev.name=name;
                        dev.location=new LatLng(lat, lng);
                        dev.online=obj.getInt("online");
                        dev.speed=obj.getInt("speed");
                        dev.acc=obj.getInt("acc");
                        dev.info=obj.getString("last_update");
                        markerLocations.add(dev);
                    }
                    catch (Exception e){
                        //System.out.println("Data missing");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return markerLocations;
    }

    @Override
    protected void onPostExecute(List<GpsData> markerLocations) {
        for (GpsData dev : markerLocations) {
            map.addMarker(new MarkerOptions().position(dev.location).title(dev.name).icon(this.getIcon(dev.online,dev.speed,dev.acc)).snippet(dev.info));
        }
    }

    private BitmapDescriptor getIcon(int online,int speed,int acc){
        if(online==1){
            if(speed>0) {
                return this.BitmapFromDrawable(this.home.getParentFragment().getContext(), R.drawable.ambulance_green);
            }
            else{
                if(acc==3){
                    return this.BitmapFromDrawable(this.home.getParentFragment().getContext(), R.drawable.ambulance_blue);
                }
                else{
                    return this.BitmapFromDrawable(this.home.getParentFragment().getContext(), R.drawable.ambulance_pink);
                }
            }
        }
        else{
            return this.BitmapFromDrawable(this.home.getParentFragment().getContext(), R.drawable.ambulance_gray);
        }
    }
    private BitmapDescriptor BitmapFromDrawable(Context context, int imageID) {
        // on below line we are creating a drawable from its id.
        Drawable imageDrawable = ContextCompat.getDrawable(context, imageID);
        // below line is use to set bounds to our vector drawable.
        imageDrawable.setBounds(0, 0, imageDrawable.getIntrinsicWidth(), imageDrawable.getIntrinsicHeight());
        // on below line is use to create a bitmap for our drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(imageDrawable.getIntrinsicWidth(), imageDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        // on below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);
        // below line is use to draw our
        // vector drawable in canvas.
        imageDrawable.draw(canvas);
        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}

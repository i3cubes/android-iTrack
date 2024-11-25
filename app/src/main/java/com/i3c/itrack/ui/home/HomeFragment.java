package com.i3c.itrack.ui.home;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.i3c.itrack.R;
import com.i3c.itrack.config.ConfigData;
import com.i3c.itrack.databinding.FragmentHomeBinding;
import com.i3c.itrack.communication.loadMarkerAsync;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private FragmentHomeBinding binding;
    private GoogleMap mMap;
    private Bundle savedInstant;
    private MapView mapView;
    private LocationManager lm;
    private LocationListener ll;
    private Location l;
    private LatLng pos;
    private String UID,NAME;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.savedInstant = savedInstanceState;

        // Retrieve arguments passed from the activity
        if (getArguments() != null) {
            this.UID = getArguments().getString("UID");
            this.NAME = getArguments().getString("NAME");
        }

        lm = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        //String provider = lm.getBestProvider(new Criteria(), true);
        String provider = LocationManager.GPS_PROVIDER;

        if (ActivityCompat.checkSelfPermission(this.getParentFragment().getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getParentFragment().getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            if(ContextCompat.checkSelfPermission(this.getParentFragment().getContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this.getParentFragment().getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            if(ContextCompat.checkSelfPermission(this.getParentFragment().getContext(),Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this.getParentFragment().getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},2);
            }
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }

        l = lm.getLastKnownLocation(provider);
        if(l==null){
            //new LatLng(6.903992, 80.00183);
            l=new Location(LocationManager.GPS_PROVIDER);
            l.setLatitude(6.903992);
            l.setLongitude(80.00183);
        }
        ll = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                l = (Location) location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mapView = binding.mapView;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1 || requestCode==2){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                mapView.onCreate(this.savedInstant);
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        System.out.println("MAP CALLBACK ---55");

        mMap = googleMap;
        System.out.println("MAP CALLBACK .... MARKER ADDED");
        if(ContextCompat.checkSelfPermission(this.getParentFragment().getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lm.requestLocationUpdates(lm.NETWORK_PROVIDER, 0, 0, ll);
        }
        if (ActivityCompat.checkSelfPermission(this.getParentFragment().getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getParentFragment().getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            if(ContextCompat.checkSelfPermission(this.getParentFragment().getContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this.getParentFragment().getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            if(ContextCompat.checkSelfPermission(this.getParentFragment().getContext(),Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this.getParentFragment().getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},2);
            }
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                System.out.println("My Loc But Clicked");
                mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                return true;
            }
        });
        new loadMarkerAsync(mMap,this).execute(new ConfigData().HOST_URL+"/get_current_location.php?",this.UID);
        System.out.println("MAP CALLBACK .... LOADING MARKER");
        // Add a marker in Sydney and move the camera
        //LatLng pos;// = new LatLng(6.903992, 80.00183);
        if(l.getLatitude()<5 || l.getLatitude()>8){
            pos=new LatLng(6.903992, 80.00183);
        }
        else{
            pos = new LatLng(l.getLatitude(), l.getLongitude());
        }
        mMap.addMarker(new MarkerOptions().position(pos).title("You are Here").icon(BitmapFromDrawable(this.getParentFragment().getContext(),R.drawable.man_red)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
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
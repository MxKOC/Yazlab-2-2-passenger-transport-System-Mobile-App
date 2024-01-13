package com.xxx.proje2;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.xxx.proje2.databinding.ActivityKulmap1Binding;
import com.xxx.proje2.directionhelpers.FetchURL;
import com.xxx.proje2.directionhelpers.TaskLoadedCallback;

public class kulmap1 extends FragmentActivity implements OnMapReadyCallback , TaskLoadedCallback, GoogleMap.OnMapClickListener{

    GoogleMap map;
    MarkerOptions place1,place2;
    Polyline currentPolyline;
    private ActivityKulmap1Binding binding;

    private String ilceler[]={ "Başiskele", "Çayırova", "Darıca", "Derince", "Dilovası", "Gebze", "Gölcük", "Kandıra", "Karamürsel", "Kartepe", "Körfez", "İzmit" };
    ListView myList;
    private TextView str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityKulmap1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        place1 = new MarkerOptions().position(new LatLng(27.658143,85.3199503)).title("İzmit");
        place2 = new MarkerOptions().position(new LatLng(27.667491 ,85.3208583)).title("NCity");

        String url = getUrl(place1.getPosition(),place2.getPosition(),"driving");
        new FetchURL(kulmap1.this).execute(url,"driving");

        myList=(ListView) findViewById(R.id.listView3);
        myList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, ilceler));
        str = (TextView) findViewById(R.id.sayi3);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                str.setText(ilceler[i]);
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        map=googleMap;
        map.addMarker(place1);
        map.addMarker(place2);


    }



    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }



    @Override
    public void onTaskDone(Object... values) {
        if(currentPolyline!=null){
            currentPolyline.remove();
        }
        currentPolyline = map.addPolyline((PolylineOptions)values[0]);
    }


    @Override
    public void onMapClick(@NonNull LatLng point) {
        Log.d("DEBUG","Map clicked [" + point.latitude + " / " + point.longitude + "]");
    }






}
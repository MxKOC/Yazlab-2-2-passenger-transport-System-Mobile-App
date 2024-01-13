package com.xxx.proje2;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.xxx.proje2.databinding.ActivityAdminmap2Binding;
import com.xxx.proje2.directionhelpers.FetchURL;
import com.xxx.proje2.directionhelpers.TaskLoadedCallback;

import java.util.ArrayList;

public class adminmap2 extends FragmentActivity implements OnMapReadyCallback , TaskLoadedCallback, GoogleMap.OnMapClickListener{


    private ActivityAdminmap2Binding binding;

    GoogleMap map;
    MarkerOptions place1,place2;
    Polyline currentPolyline;
    private String ilceler[]={ "Başiskele", "Çayırova", "Darıca", "Derince", "Dilovası", "Gebze", "Gölcük", "Kandıra", "Karamürsel", "Kartepe", "Körfez", "İzmit" };
    ListView myList;
    private TextView str;

    ArrayList<String> duraklar = new ArrayList<>();
    ArrayList<MarkerOptions> markerOptionsArrayList = new ArrayList<MarkerOptions>();
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminmap2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        try {
            db = this.openOrCreateDatabase("proje",MODE_PRIVATE,null);
            db.execSQL("CREATE TABLE IF NOT EXISTS Duraklar(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,durak_adi VARCHAR,yolcu_sayisi INT,enlem FLOAT,boylam FLOAT)");
            getAllData();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        place1 = new MarkerOptions().position(new LatLng(27.658143,85.3199503)).title("İzmit");
        place2 = new MarkerOptions().position(new LatLng(27.667491 ,85.3208583)).title("NCity");

        String url = getUrl(place1.getPosition(),place2.getPosition(),"driving");
        new FetchURL(adminmap2.this).execute(url,"driving");



        myList=(ListView) findViewById(R.id.listView2);
        myList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, ilceler));
        str = (TextView) findViewById(R.id.sayi2);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                str.setText(duraklar.get(i));
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

        LatLng sydney = new LatLng(40.824562, 29.921665);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));



        map=googleMap;

        try {
            db = this.openOrCreateDatabase("proje",MODE_PRIVATE,null);
            Cursor cursor = db.rawQuery("Select * from Duraklar",null);

            while(cursor.moveToNext()){
                String durak_adi = cursor.getString(1);
                float enlem = cursor.getFloat(3);
                float boylam = cursor.getFloat(4);

                MarkerOptions place = new MarkerOptions().position(new LatLng(enlem ,boylam)).title(durak_adi);
                map.addMarker(place);
                markerOptionsArrayList.add(place);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        for(int i=0;i<markerOptionsArrayList.size()-1;i++){
            System.out.println(markerOptionsArrayList.size()-1);
            MarkerOptions p1 = markerOptionsArrayList.get(i);
            MarkerOptions p2 = markerOptionsArrayList.get(i+1);

            String url = getUrl(p1.getPosition(),p2.getPosition(),"driving");
            new FetchURL(adminmap2.this).execute(url,"driving");
        }

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

    private void getAllData(){
        //Tüm Durakların Yolcu Sayıları Edittextlere yazılıyor.
        Cursor cursor = db.rawQuery("Select * from Duraklar",null);
        while (cursor.moveToNext()){
            System.out.println("ID : "+cursor.getString(0) + ",Durak Adı : "+cursor.getString(1) + ",Yolcu : "+cursor.getString(2)+",ENLEM :"+cursor.getFloat(3)+"BOYLAM :"+cursor.getFloat(4));
            String yolcu_sayisi = cursor.getString(2);
            String durak_adi = cursor.getString(1);
            duraklar.add(durak_adi + " : "+yolcu_sayisi);
        }

        cursor.close();
    }


    @Override
    public void onMapClick(@NonNull LatLng point) {
        Log.d("DEBUG","Map clicked [" + point.latitude + " / " + point.longitude + "]");
    }
}
package com.xxx.proje2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AnakullaniciActivity extends AppCompatActivity {
    private Button h1kul;
    private Button h2kul;

    private String ilceler[]={ "Başiskele", "Çayırova", "Darıca", "Derince", "Dilovası", "Gebze", "Gölcük", "Kandıra", "Karamürsel", "Kartepe", "Körfez", "İzmit" };
    ArrayList<String> duraklar = new ArrayList<String>();
    ListView myList;
    private TextView str;

    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anakullanici);

        try {
            db = this.openOrCreateDatabase("proje",MODE_PRIVATE,null);
            db.execSQL("CREATE TABLE IF NOT EXISTS Duraklar(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,durak_adi VARCHAR,yolcu_sayisi INT,enlem FLOAT,boylam FLOAT)");
            getAllData();
        }
        catch (Exception e){
            e.printStackTrace();
        }


        h1kul = (Button) findViewById(R.id.ister1kul);
        h1kul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                harita1();
            }
        });

        h2kul = (Button) findViewById(R.id.ister2kul);
        h2kul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                harita2();
            }
        });





        myList=(ListView) findViewById(R.id.listView);
        myList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, duraklar));
        str = (TextView) findViewById(R.id.textView18);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                str.setText(ilceler[i]);
            }
        });



    }

    public void harita1() {
        Intent intent = new Intent(this, kulmap1.class);
        startActivity(intent);
    }


    public void harita2() {
        Intent intent = new Intent(this, kulmap2.class);
        startActivity(intent);
    }

    private void getAllData(){
        //Tüm Durakların Yolcu Sayıları Edittextlere yazılıyor.
        Cursor cursor = db.rawQuery("Select * from Duraklar",null);
        while (cursor.moveToNext()){
            System.out.println("ID : "+cursor.getString(0) + ",Durak Adı : "+cursor.getString(1) + ",Yolcu : "+cursor.getString(2)+",ENLEM :"+cursor.getString(3)+"BOYLAM :"+cursor.getString(4));
            String yolcu_sayisi = cursor.getString(2);
            String durak_adi = cursor.getString(1);
            duraklar.add(durak_adi + " : "+yolcu_sayisi);
        }

        cursor.close();
    }
}
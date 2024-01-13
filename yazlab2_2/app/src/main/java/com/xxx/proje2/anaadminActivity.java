package com.xxx.proje2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class anaadminActivity extends AppCompatActivity {
    private Button h1admin;
    private Button h2admin;
    private EditText input_yolcuSayisi;

    private SQLiteDatabase db;
    ListView myList;
    ArrayList<String> duraklar = new ArrayList<String>();

    String name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anaadmin);
        //DB İŞLEMLERİ
        try {
            db = this.openOrCreateDatabase("proje",MODE_PRIVATE,null);
            db.execSQL("CREATE TABLE IF NOT EXISTS Duraklar(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,durak_adi VARCHAR,yolcu_sayisi INT,enlem FLOAT,boylam FLOAT)");
        /*
            db.execSQL("INSERT INTO Duraklar(durak_adi,yolcu_sayisi,enlem,boylam) VALUES('Başiskele',0,40.715298,29.927692)");
            db.execSQL("INSERT INTO Duraklar(durak_adi,yolcu_sayisi,enlem,boylam) VALUES('Çayırova',0,40.8105331,29.3474124)");
            db.execSQL("INSERT INTO Duraklar(durak_adi,yolcu_sayisi,enlem,boylam) VALUES('Darıca',0,40.773863,29.400298)");
            db.execSQL("INSERT INTO Duraklar(durak_adi,yolcu_sayisi,enlem,boylam) VALUES('Derince',0,40.75751,29.82923)");
            db.execSQL("INSERT INTO Duraklar(durak_adi,yolcu_sayisi,enlem,boylam) VALUES('Dilovası',0,40.7879004,29.5354513)");
            db.execSQL("INSERT INTO Duraklar(durak_adi,yolcu_sayisi,enlem,boylam) VALUES('Gebze',0,40.8266757,29.4182846)");
            db.execSQL("INSERT INTO Duraklar(durak_adi,yolcu_sayisi,enlem,boylam) VALUES('Gölcük',0,40.651516,29.7233058)");
            db.execSQL("INSERT INTO Duraklar(durak_adi,yolcu_sayisi,enlem,boylam) VALUES('Kandıra',0,41.0736033,30.1420463)");
            db.execSQL("INSERT INTO Duraklar(durak_adi,yolcu_sayisi,enlem,boylam) VALUES('Karamürsel',0,40.69144,29.61568)");
            db.execSQL("INSERT INTO Duraklar(durak_adi,yolcu_sayisi,enlem,boylam) VALUES('Kartepe',0,40.7453162,30.015141)");
            db.execSQL("INSERT INTO Duraklar(durak_adi,yolcu_sayisi,enlem,boylam) VALUES('Körfez',0,40.7606132,29.742299)");
            db.execSQL("INSERT INTO Duraklar(durak_adi,yolcu_sayisi,enlem,boylam) VALUES('İzmit',0,40.7711737,29.8993959)");
        */
            //db.execSQL("delete from Duraklar");
            //db.execSQL("delete from Duraklar Where durak_adi='yeni'");
            getAllData();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        myList = (ListView)findViewById(R.id.listViewx);
        input_yolcuSayisi = (EditText)findViewById(R.id.sayix);
        myList.setAdapter(new ArrayAdapter<String>(anaadminActivity.this, android.R.layout.simple_list_item_1,duraklar));

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("index: "+i +", name: "+ duraklar.get(i));
                String s = duraklar.get(i);
                int start = s.indexOf(":");
                String result = s.substring(start+1);
                String result2 = s.substring(0,start);
                result = result.trim();
                result2 = result2.trim();
                System.out.println(result);
                System.out.println(result2);
                input_yolcuSayisi.setText(result);
                name = result2;
            }
        });

        h1admin = (Button) findViewById(R.id.ister1admin);
        h1admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                har1();
            }
        });

        h2admin = (Button) findViewById(R.id.ister2admin);
        h2admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                har2();
            }
        });

    }

    public void har1() {
        Intent intent = new Intent(this, adminmap1.class);
        startActivity(intent);
    }


    public void har2() {
        Intent intent = new Intent(this, adminmap2.class);
        startActivity(intent);
    }

    public void btnDegistir(View v){
        updateData(name,Integer.parseInt(input_yolcuSayisi.getText().toString()));
        duraklar.clear();
        getAllData();
        myList.setAdapter(new ArrayAdapter<String>(anaadminActivity.this, android.R.layout.simple_list_item_1,duraklar));
    }

    public void btnDurakSil(View v){
        deleteData(name);
        duraklar.clear();
        getAllData();
        myList.setAdapter(new ArrayAdapter<String>(anaadminActivity.this, android.R.layout.simple_list_item_1,duraklar));
    }

    public void btnYeniDurak(View v){
        Intent intent = new Intent(anaadminActivity.this,admin_yeniDurak.class);
        startActivity(intent);
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

    private void updateData(String id,int yolcu_sayisi){
        ContentValues values = new ContentValues();
        values.put("yolcu_sayisi",yolcu_sayisi);
        db.update("Duraklar",values,"durak_adi=?",new String[]{id});
    }

    private void deleteData(String durak_adi){
        db.execSQL("delete from Duraklar Where durak_adi='"+durak_adi+"'");
    }

}
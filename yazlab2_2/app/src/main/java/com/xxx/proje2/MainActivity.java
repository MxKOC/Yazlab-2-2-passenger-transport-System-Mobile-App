package com.xxx.proje2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button butonadmin;
    private Button butonkul;

    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            db = this.openOrCreateDatabase("proje",MODE_PRIVATE,null);
            //db.execSQL("drop Table if exists Ogrenciler");
            db.execSQL("CREATE TABLE IF NOT EXISTS Servisler(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,yolcu_kapasitesi INTEGER)");
            /*
            db.execSQL("INSERT INTO Servisler(yolcu_kapasitesi) VALUES(25)");
            db.execSQL("INSERT INTO Servisler(yolcu_kapasitesi) VALUES(30)");
            db.execSQL("INSERT INTO Servisler(yolcu_kapasitesi) VALUES(40)");
             */
            getAllData();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        butonadmin = (Button) findViewById(R.id.buton1);
        butonadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminActivity();
            }
        });


        butonkul = (Button) findViewById(R.id.buton2);
        butonkul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kullActivity();
            }
        });

    }


    public void adminActivity() {
        Intent intent = new Intent(this, adminlogin.class);
        startActivity(intent);
    }


    public void kullActivity() {
        Intent intent = new Intent(this, kullanicilogin.class);
        startActivity(intent);
    }

    private void getAllData(){
        Cursor cursor = db.rawQuery("Select * from Servisler",null);
        while (cursor.moveToNext()){
            System.out.println("ID : "+cursor.getString(0) + ",Yolcu Kapasitesi : "+cursor.getInt(1));
        }
        cursor.close();
    }

}
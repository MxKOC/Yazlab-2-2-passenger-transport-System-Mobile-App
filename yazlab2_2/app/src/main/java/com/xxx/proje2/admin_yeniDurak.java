package com.xxx.proje2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class admin_yeniDurak extends AppCompatActivity {
    EditText input_durakAdi,input_enlem,input_boylam;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_yeni_durak);

        input_durakAdi = (EditText)findViewById(R.id.input_durakAdi);
        input_enlem = (EditText)findViewById(R.id.input_enlem);
        input_boylam = (EditText)findViewById(R.id.input_boylam);
    }

    public void btnDurakEkle(View v){
        try {
            db = this.openOrCreateDatabase("proje",MODE_PRIVATE,null);
            String durak_adi = input_durakAdi.getText().toString();
            float enlem = Float.parseFloat(input_enlem.getText().toString());
            float boylam = Float.parseFloat(input_boylam.getText().toString());
            db.execSQL("INSERT INTO Duraklar(durak_adi,yolcu_sayisi,enlem,boylam) VALUES('"+durak_adi+"',0,"+enlem+","+boylam+")");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Intent intent = new Intent(admin_yeniDurak.this,anaadminActivity.class);
        startActivity(intent);
    }


}
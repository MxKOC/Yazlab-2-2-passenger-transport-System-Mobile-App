package com.xxx.proje2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class kullanicilogin extends AppCompatActivity {

    private SQLiteDatabase db;
    EditText txt_username,txt_password;
    TextView lbl_hata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanicilogin);

        try {
            db = this.openOrCreateDatabase("proje",MODE_PRIVATE,null);
            //db.execSQL("drop Table if exists Ogrenciler");
            db.execSQL("CREATE TABLE IF NOT EXISTS Users(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,username VARCHAR,password VARCHAR)");
            //db.execSQL("INSERT INTO Users(username,password) VALUES('k1','111')");
            getAllData();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        txt_username = (EditText)findViewById(R.id.txt_username);
        txt_password = (EditText)findViewById(R.id.txt_password);
        lbl_hata = (TextView)findViewById(R.id.lbl_hata);



    }


    private void getAllData(){
        Cursor cursor = db.rawQuery("Select * from Users",null);

        while (cursor.moveToNext()){
            System.out.println("ID : "+cursor.getString(0) + ",AD : "+cursor.getString(1) + ",PASSWORD : "+cursor.getString(2));
        }

        cursor.close();
    }

    public void btnLogin(View v){
        String username = txt_username.getText().toString();
        String password = txt_password.getText().toString();



        if(!username.equals("") && !password.equals("")){
            try {
                Cursor cursor = db.rawQuery("Select id From Users Where username='"+username+"' and password='"+password+"'",null);

                if(cursor.getCount()==0){
                    lbl_hata.setText("Kullanıcı adı ve ya şifre hatalı.");
                }
                else {
                    cursor.close();
                    Intent intent = new Intent(kullanicilogin.this,AnakullaniciActivity.class);
                    startActivity(intent);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            lbl_hata.setText("Kullanıcı adı ve şifre bilgileri doldurulmalı.");
        }

    }


}
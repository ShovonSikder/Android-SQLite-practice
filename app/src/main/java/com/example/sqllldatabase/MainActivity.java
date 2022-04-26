//main activity
package com.example.sqllldatabase;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity {
EditText inname,incollege;
Button insert,display,exit;
SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //reff
        inname=findViewById(R.id.inname);
        incollege=findViewById(R.id.incollege);
        insert=findViewById(R.id.insert);
        display=findViewById(R.id.display);
        exit=findViewById(R.id.exit);

        db=openOrCreateDatabase("Mydb",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS students(name VARCHAR,college VARCHAR);");

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String nam=inname.getText().toString();
                String coll=incollege.getText().toString();
                if(!nam.equals("") && !coll.equals("")) {//make sure all fields filled up
                    db.execSQL("INSERT INTO students VALUES ('" + nam + "','" + coll + "');");
                    Toast.makeText(getApplicationContext(), "Row Inserted", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Name and College Requires",Toast.LENGTH_LONG).show();
                }
            }
        });

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),display.class);
                startActivity(intent);
                finish();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
    }
}
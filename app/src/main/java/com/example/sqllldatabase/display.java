//preview activity
package com.example.sqllldatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class display extends AppCompatActivity {
    EditText stname, stcollege, searchdata;
    Button pre, next, home, delete, update, searchbutton;
    ListView listrecords;
    SQLiteDatabase db;
    Cursor c;
    int cursor_position;
    String nam_id;
    ArrayList<String> records=new ArrayList();
    ArrayAdapter<String> adapter;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        //ref
        //edittext fields
        stname = findViewById(R.id.stname);
        stcollege = findViewById(R.id.stcollege);
        searchdata = findViewById(R.id.searchdata);

        //buttons
        pre = findViewById(R.id.pre);
        next = findViewById(R.id.next);
        home = findViewById(R.id.home);
        delete = findViewById(R.id.delete);
        update = findViewById(R.id.update);
        searchbutton = findViewById(R.id.searchbutton);

        listrecords = findViewById(R.id.listrecords);


        db = openOrCreateDatabase("Mydb", MODE_PRIVATE, null);

        c = db.rawQuery("SELECT * FROM students", null);


        try {
            c.moveToFirst();
            cursor_position = c.getPosition();
            stname.setText(nam_id = c.getString(c.getColumnIndex("name")));
            stcollege.setText(c.getString(c.getColumnIndex("college")));
        } catch (Exception e0) {
            stname.setText("Empty");
            stcollege.setText("empty");
            records.add("Empty");
        }



        adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,records);
        listrecords.setAdapter(adapter);
        LoadToList();


        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    c.moveToPrevious();
                    cursor_position = c.getPosition();
                    stname.setText(nam_id = c.getString(c.getColumnIndex("name")));
                    stcollege.setText(c.getString(c.getColumnIndex("college")));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Fisrt record", Toast.LENGTH_SHORT).show();
                    c.moveToNext();
                    cursor_position = c.getPosition();
                }
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    c.moveToNext();
                    cursor_position = c.getPosition();
                    stname.setText(nam_id = c.getString(c.getColumnIndex("name")));
                    stcollege.setText(c.getString(c.getColumnIndex("college")));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Last record", Toast.LENGTH_SHORT).show();
                    c.moveToPrevious();
                    cursor_position = c.getPosition();
                }
            }
        });


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                c.close();
                finish();
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c.getCount()==0) return;

                db.execSQL("DELETE FROM students WHERE name='" + stname.getText() + "' AND college='" + stcollege.getText() +"'");
                c = db.rawQuery("SELECT * FROM students", null);
                c.moveToPosition(cursor_position);
                try {
                    stname.setText(nam_id = c.getString(c.getColumnIndex("name")));
                    stcollege.setText(c.getString(c.getColumnIndex("college")));
                } catch (Exception e) {
                    try {
                        c.moveToPrevious();
                        cursor_position = c.getPosition();
                        stname.setText(nam_id = c.getString(c.getColumnIndex("name")));
                        stcollege.setText(c.getString(c.getColumnIndex("college")));
                    } catch (Exception e1) {
                        stname.setText("Empty");
                        stcollege.setText("empty");
                    }
                }
                LoadToList();

                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c.getCount()==0) return;

                String new_name = stname.getText().toString();
                String new_coll = stcollege.getText().toString();
                db.execSQL("UPDATE students SET name='" + new_name + "',college='" + new_coll + "' WHERE name='" + nam_id + "'");
                c = db.rawQuery("SELECT * FROM students", null);
                LoadToList();
                //records.
                c.moveToPosition(cursor_position);
                stname.setText(nam_id = c.getString(c.getColumnIndex("name")));
                stcollege.setText(c.getString(c.getColumnIndex("college")));
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
            }
        });

        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nam=searchdata.getText().toString().trim();
                Cursor cor=db.rawQuery("SELECT * FROM students WHERE name='"+ nam +"'", null);
                try{
                    cor.moveToFirst();
                    records.clear();
                    records.add(cor.getString(cor.getColumnIndex("name"))+"                          "+cor.getString(cor.getColumnIndex("college")));
                    adapter.notifyDataSetChanged();
                }catch (Exception e){
                    LoadToList();
                    Toast.makeText(getApplicationContext(),"Record not found",Toast.LENGTH_LONG).show();
                }

            }
        });



    }

    @SuppressLint("Range")
    private void LoadToList(){
        try {
            records.clear();
            c.moveToFirst();
            while (true){
                records.add(c.getString(c.getColumnIndex("name"))+"                          "+c.getString(c.getColumnIndex("college")));
                c.moveToNext();
            }
        }catch (Exception e){
           if (records.size()<=0) records.add("Empty from method");
            c.moveToPosition(cursor_position);
        }
        adapter.notifyDataSetChanged();
    }
}
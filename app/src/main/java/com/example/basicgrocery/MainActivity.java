package com.example.basicgrocery;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ListView mainListView ;
    private CustomAdapter myCustomAdapter;

@Override
protected void onResume(){
    super.onResume();
    ArrayList<String> fileList = new ArrayList<String>();

    fileList = new ArrayList(Arrays.asList(getApplicationContext().fileList()));

    myCustomAdapter = new CustomAdapter(fileList, getApplicationContext());

    mainListView.setAdapter( myCustomAdapter );

}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureNewListButton ();
        configureListView();
    }

   private void configureNewListButton(){
        ImageButton newListButton = (ImageButton) findViewById(R.id.newlistBtn);

       newListButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view) {
               startActivity(new Intent(MainActivity.this, NewListActivity.class));
           }

       });
   }
    private void configureListView() {
        // Find the ListView resource.
        mainListView = (ListView) findViewById( R.id.mainlistListView );

        Context context = getApplicationContext();

        ArrayList<String> fileList = new ArrayList<String>();
        fileList = new ArrayList(Arrays.asList(context.fileList()));

        myCustomAdapter = new CustomAdapter( fileList , this);

        mainListView.setAdapter( myCustomAdapter );

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Intent myIntent = new Intent(MainActivity.this, OldListActivity.class);
                myIntent.putExtra("fileName",parent.getItemAtPosition(position).toString());
                startActivity(myIntent);
            }
        });


    }

}

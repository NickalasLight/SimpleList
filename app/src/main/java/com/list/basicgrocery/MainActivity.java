package com.list.basicgrocery;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ListView mainListView ;
    private CustomMainAdapter myCustomMainAdapter;

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.mymenu, menu);


            super.onCreateOptionsMenu(menu);


        }
        catch(Exception e){
            Log.d("bleh", "onCreateOptionsMenu: " + e);

        }finally {
            return super.onCreateOptionsMenu(menu);
        }
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuprivacypolicy) {
            Intent intent = new Intent(MainActivity.this, PrivacyPolicy.class);

            startActivity(intent);

        }
            // do something here

        return super.onOptionsItemSelected(item);
    }

@Override
protected void onResume(){
    super.onResume();
    ArrayList<String> fileList = new ArrayList<String>();
Log.i("Main onResume:", "IN onResume in main");

    fileList = new ArrayList(Arrays.asList(getApplicationContext().fileList()));

    myCustomMainAdapter = new CustomMainAdapter(fileList, getApplicationContext());

    mainListView.setAdapter(myCustomMainAdapter);

}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureNewListButton ();
        configureListView();
        //getSupportActionBar().setTitle("My Lists");
        getSupportActionBar().setTitle(getResources().getString(R.string.main_title));
    }

   private void configureNewListButton(){
        ImageButton newListButton = (ImageButton) findViewById(R.id.newlistBtn);

       newListButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view) {

               Intent myIntent = new Intent(MainActivity.this, ListActivity.class);
               //myIntent.putExtra("fileName", "newList");
               startActivity(myIntent);
           }

       });
   }


    private void configureListView() {
        // Find the ListView resource.
        mainListView = (ListView) findViewById( R.id.mainlistListView );

        Context context = getApplicationContext();

        ArrayList<String> fileList = new ArrayList<String>();
        fileList = new ArrayList(Arrays.asList(context.fileList()));

        myCustomMainAdapter = new CustomMainAdapter( fileList , this);

        mainListView.setAdapter(myCustomMainAdapter);

        /*mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Intent myIntent = new Intent(MainActivity.this, ListActivity.class);
                myIntent.putExtra("fileName",parent.getItemAtPosition(position).toString());
                startActivity(myIntent);
            }
        });
        */


    }

}

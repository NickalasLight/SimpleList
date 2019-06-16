package com.list.basicgrocery;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ListView mainListView ;
    private CustomMainAdapter myCustomMainAdapter;

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        //configureMenuSpinner();


       // MenuItem item = menu.findItem(R.id.spinner1);
       // Spinner item = (Spinner) menu.findItem(R.id.spinner1);

        //Spinner dropdown = item;// (Spinner) menu.findItem(R.id.spinner1).getActionView();//.getActionView(item);

//create a list of items for the spinner.
        //String[] items = new String[]{"1", "2", "three"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this)
//set the spinners adapter to the previously created one.
        //item.setAdapter(adapter);


        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //if (id == R.id.mybutton) {
            // do something here
       // }
        if(id == R.id.spinner1) {

        }
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
       // configureMenu();
    }

   private void configureNewListButton(){
        ImageButton newListButton = (ImageButton) findViewById(R.id.newlistBtn);

       newListButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view) {

               Intent myIntent = new Intent(MainActivity.this, OldListActivity.class);
               //myIntent.putExtra("fileName", "newList");
               startActivity(myIntent);
           }

       });
   }

   private void configureMenuSpinner(){
       //get the spinner from the xml.
       Spinner dropdown = findViewById(R.id.spinner1);
//create a list of items for the spinner.
       String[] items = new String[]{"1", "2", "three"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
       ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
       dropdown.setAdapter(adapter);
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

                Intent myIntent = new Intent(MainActivity.this, OldListActivity.class);
                myIntent.putExtra("fileName",parent.getItemAtPosition(position).toString());
                startActivity(myIntent);
            }
        });
        */


    }

}

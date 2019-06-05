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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    private CustomAdapter myCustomeAdapter;
@Override
protected void onResume(){
    super.onResume();
    ArrayList<String> fileList = new ArrayList<String>();

    fileList = new ArrayList(Arrays.asList(getApplicationContext().fileList()));


    // Create and populate a List of planet names.
    //String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
    //          "Jupiter", "Saturn", "Uranus", "Neptune"};
    // ArrayList<String> planetList = new ArrayList<String>();
    //  planetList.addAll( Arrays.asList(planets) );

    // Create ArrayAdapter using the planet list.
    listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, fileList );


    // Add more planets. If you passed a String[] instead of a List<String>
    // into the ArrayAdapter constructor, you must not add more items.
    // Otherwise an exception will occur.
    //listAdapter.add( "Ceres" );
    //listAdapter.add( "Pluto" );
    //listAdapter.add( "Haumea" );
    //listAdapter.add( "Makemake" );
    //listAdapter.add( "Eris" );

    // Set the ArrayAdapter as the ListView's adapter.
    mainListView.setAdapter( listAdapter );
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureNewListButton ();
        configureListView();
    }

   private void configureNewListButton(){
        Button newListButton = (Button) findViewById(R.id.newlistBtn);

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
        //new ArrayList( Arrays.asList( new String[]{"abc", "def"} ) );
        ArrayList<String> fileList = new ArrayList<String>();
        fileList = new ArrayList(Arrays.asList(context.fileList()));


        // Create and populate a List of planet names.
        //String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
        //          "Jupiter", "Saturn", "Uranus", "Neptune"};
        // ArrayList<String> planetList = new ArrayList<String>();
        //  planetList.addAll( Arrays.asList(planets) );

        // Create ArrayAdapter using the planet list.
        //listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, fileList );
        myCustomeAdapter = new CustomAdapter( fileList , this);


        // Add more planets. If you passed a String[] instead of a List<String>
        // into the ArrayAdapter constructor, you must not add more items.
        // Otherwise an exception will occur.
        //listAdapter.add( "Ceres" );
        //listAdapter.add( "Pluto" );
        //listAdapter.add( "Haumea" );
        //listAdapter.add( "Makemake" );
        //listAdapter.add( "Eris" );

        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( myCustomeAdapter );
        //mainListView.setAdapter( listAdapter );
        //addlistItem("Test Item23435");

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //Intent intent = new Intent(NewListActivity.this, SendMessage.class);
                //String message = "abc";
                //intent.putExtra(EXTRA_MESSAGE, message);
                //startActivity(intent);
               //listAdapter.remove(parent.getItemAtPosition(position).toString());
                //listAdapter.notifyDataSetChanged();
                Intent myIntent = new Intent(MainActivity.this, OldListActivity.class);
                myIntent.putExtra("fileName",parent.getItemAtPosition(position).toString());
                startActivity(myIntent);
            }
        });
    }

    private void addlistItem(String item) {
        listAdapter.add(item);
        mainListView.smoothScrollToPosition(mainListView.getMaxScrollAmount());
        //mainListView.setAdapter(listAdapter);
    }



}

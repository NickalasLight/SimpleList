package com.example.basicgrocery;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class NewListActivity extends AppCompatActivity {

    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newlistview);

        configureFinishButton();
        configureListView();
        configureAddItemButton();
    }

    private void writeToFile(Context context, String listName) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(listName, Context.MODE_PRIVATE));

            for(int i=0; i < listAdapter.getCount(); i++) {

                String value = listAdapter.getItem(i).toString();

                //outputStreamWriter.write(value+";");
                outputStreamWriter.append(value+";");
            }
            outputStreamWriter.close();
        }
        catch (IOException e) {
           // Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void saveNewList() {
        //set textbox somewhere to have a default name, check so it doesn't conflict with oter lists
        //text is used to set list name. Able to be changed by user.
        EditText listName = (EditText) findViewById(R.id.newlistNameText); //grab text of this to set filename

        File directory = NewListActivity.this.getFilesDir();
        File file = new File(directory, listName.getText().toString());
        if(!file.exists()) {
            try {
                file.createNewFile();
                writeToFile(getApplicationContext(),listName.getText().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
    private void addlistItem(String item) {
        listAdapter.add(item);
        mainListView.smoothScrollToPosition(mainListView.getMaxScrollAmount());
        //mainListView.setAdapter(listAdapter);
    }
    private void configureListView() {
        // Find the ListView resource.
        mainListView = (ListView) findViewById( R.id.newlistListview);

        // Create and populate a List of planet names.
        //String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
      //          "Jupiter", "Saturn", "Uranus", "Neptune"};
      // ArrayList<String> planetList = new ArrayList<String>();
      //  planetList.addAll( Arrays.asList(planets) );

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow);


        // Add more planets. If you passed a String[] instead of a List<String>
        // into the ArrayAdapter constructor, you must not add more items.
        // Otherwise an exception will occur.
       // listAdapter.add( "Ceres" );
       // listAdapter.add( "Pluto" );
      //  listAdapter.add( "Haumea" );
       // listAdapter.add( "Makemake" );
       /// listAdapter.add( "Eris" );

        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( listAdapter );


        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //Intent intent = new Intent(NewListActivity.this, SendMessage.class);
                //String message = "abc";
                //intent.putExtra(EXTRA_MESSAGE, message);
                //startActivity(intent);
                listAdapter.remove(parent.getItemAtPosition(position).toString());
                listAdapter.notifyDataSetChanged();
            }
        });
    }
    private void configureAddItemButton(){
        Button additemButton = (Button) findViewById(R.id.newaddlistitemButton);
        final EditText newlistitemText = (EditText) findViewById(R.id.newlistitemEditText);

        additemButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO: code to save list somewhere on phone.
                if (newlistitemText.length() > 0) {
                    addlistItem(newlistitemText.getText().toString());
                    newlistitemText.setText("");
                }

            }

        });
    }

    private void configureFinishButton(){
        Button finishButton = (Button) findViewById(R.id.newlistviewFinishButton);

        finishButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO: code to save list somewhere on phone.
                saveNewList();

                finish();
            }

        });
    }
}
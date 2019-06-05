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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class OldListActivity extends AppCompatActivity {

    private String fileName;
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    private ArrayList<String> dataArray = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oldlistview);
        fileName = getIntent().getStringExtra("fileName");

        configureFinishButton();
        configureAddItemButton();
        //getIntent().getExtras();
        configureListNameText();
        readFromFiletoArrayList();
        configureListView();

    }
    private void configureListNameText(){
        EditText myEditText = (EditText) findViewById(R.id.oldlistNameText);
        myEditText.setText(fileName);
    }

    private void readFromFiletoArrayList(){
        Context context = getApplicationContext();
        File myDir = OldListActivity.this.getFilesDir();
        try {
            String filePath = myDir.getPath()+"/"+fileName;
            String fileString = getStringFromFile(filePath);
            while(fileString.indexOf(";") != -1) {
                dataArray.add(fileString.substring(0, fileString.indexOf(";")));
                fileString = fileString.substring(fileString.indexOf(";")+1,fileString.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile (String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
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

    private void saveOldList() {
        //set textbox somewhere to have a default name, check so it doesn't conflict with oter lists
        //text is used to set list name. Able to be changed by user.
        EditText listName = (EditText) findViewById(R.id.oldlistNameText); //grab text of this to set filename

        File directory = OldListActivity.this.getFilesDir();
        File file = new File(directory, listName.getText().toString());
        if(file.exists()){file.delete();}

            try {
                file.createNewFile();
                writeToFile(getApplicationContext(),listName.getText().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }


        


    }
    private void addlistItem(String item) {
        listAdapter.add(item);
        mainListView.smoothScrollToPosition(mainListView.getMaxScrollAmount());
        //mainListView.setAdapter(listAdapter);
    }
    private void configureListView() {
        // Find the ListView resource.
        mainListView = (ListView) findViewById( R.id.oldlistListView);

        // Create and populate a List of planet names.
        //String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
        //          "Jupiter", "Saturn", "Uranus", "Neptune"};
        // ArrayList<String> planetList = new ArrayList<String>();
        //  planetList.addAll( Arrays.asList(planets) );

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow,dataArray);


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
        Button additemButton = (Button) findViewById(R.id.oldaddlistitemButton);
        final EditText newlistitemText = (EditText) findViewById(R.id.oldlistitemEditText);

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
        Button finishButton = (Button) findViewById(R.id.oldlistviewFinishButton);

        finishButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO: code to save list somewhere on phone.
                saveOldList();
                finish();
            }

        });
    }
}

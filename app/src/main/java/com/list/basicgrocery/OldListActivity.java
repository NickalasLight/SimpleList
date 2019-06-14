package com.list.basicgrocery;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class OldListActivity extends AppCompatActivity {

    private String fileName;
    private ListView mainListView ;
    private CustomListAdapter customListAdapter;
    private ArrayList<String> dataArray = new ArrayList<String>();
    private Dictionary activityDict = new Hashtable<Integer,String>();
    //private String oldListName;
private boolean ifFileExists(String fileName)
{
    File directory = OldListActivity.this.getFilesDir();
    File file = new File(directory, fileName);
    if(file.exists()){return true;}
    else {return false;}

}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

        if(fileName == null){fileName = "newList";
        int i = 1;
        while(ifFileExists(fileName)) {
            fileName = "newList " + i;
            i = i + 1;

            }



        myEditText.setText(fileName);
        myEditText.setFocusable(true);
        myEditText.requestFocus();
        myEditText.selectAll();
        }
        else{
            myEditText.setText(fileName);
        }


    }
    public void setActivityDict(int position, String value){
        activityDict.put(position,value);
    }

    private void readFromFiletoArrayList(){
        Context context = getApplicationContext();
        File myDir = OldListActivity.this.getFilesDir();
        try {
            String filePath = myDir.getPath()+"/"+fileName;
            String fileString = getStringFromFile(filePath);
            int i = 0;
            while(fileString.indexOf(";") != -1) {
                if(fileString.substring(0, fileString.indexOf(";")).equals("true") || fileString.substring(0, fileString.indexOf(";")).equals("false")){
                    this.setActivityDict(i,fileString.substring(0, fileString.indexOf(";")));
                    fileString = fileString.substring(fileString.indexOf(";")+1,fileString.length());
                    i=i+1;
                }
                else{
                dataArray.add(fileString.substring(0, fileString.indexOf(";")));
                    fileString = fileString.substring(fileString.indexOf(";")+1,fileString.length());


            }}
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

            for(int i = 0; i < customListAdapter.getCount(); i++) {

                String value = customListAdapter.getItem(i).toString();
                String isChecked = customListAdapter.getIsChecked(i);

                //outputStreamWriter.write(value+";");
                outputStreamWriter.append(value+";"+isChecked+";");
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
        File file = new File(directory, fileName);
        if(file.exists()){file.delete();}
        file = new File(directory, listName.getText().toString());
        if(file.exists()) {
            Context context = getApplicationContext();

            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(OldListActivity.this);
            dlgAlert.setMessage("This is an alert with no consequence");
            dlgAlert.setTitle("App Title");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
            {
                file.delete();
            }
        }

            try {
                file.createNewFile();
                writeToFile(getApplicationContext(),listName.getText().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }


        


    }
    private void addlistItem(String item) {
        customListAdapter.Add(item); //TODO: implement adding item to list inside the customlistadapter class
        customListAdapter.notifyDataSetChanged();
        mainListView.smoothScrollToPosition(mainListView.getMaxScrollAmount());
        //mainListView.setAdapter(customListAdapter);
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

        customListAdapter = new CustomListAdapter(dataArray,getApplicationContext());
        customListAdapter.isCheckedDict = this.activityDict;


        // Add more planets. If you passed a String[] instead of a List<String>
        // into the ArrayAdapter constructor, you must not add more items.
        // Otherwise an exception will occur.
        // customListAdapter.add( "Ceres" );
        // customListAdapter.add( "Pluto" );
        //  customListAdapter.add( "Haumea" );
        // customListAdapter.add( "Makemake" );
        /// customListAdapter.add( "Eris" );

        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter(customListAdapter);


        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //Intent intent = new Intent(NewListActivity.this, SendMessage.class);
                //String message = "abc";
                //intent.putExtra(EXTRA_MESSAGE, message);
                //startActivity(intent);
                //customListAdapter.remove(parent.getItemAtPosition(position).toString());
                customListAdapter.notifyDataSetChanged();
            }
        });
    }
    private void configureAddItemButton(){
        ImageButton additemButton = (ImageButton) findViewById(R.id.oldaddlistitemButton);
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
        ImageButton finishButton = (ImageButton) findViewById(R.id.oldlistviewFinishButton);

        finishButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO: code to save list somewhere on phone.
                saveOldList();
                activityDict = customListAdapter.isCheckedDict;
                finish();
            }

        });
    }
}

package com.list.basicgrocery;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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

public class ListActivity extends AppCompatActivity {

    private String fileName;
    private ListView mainListView ;
    private CustomListAdapter customListAdapter;
    private ArrayList<String> dataArray = new ArrayList<String>();
    private Dictionary activityDict = new Hashtable<Integer,String>();
    //private String oldListName;

    private boolean ifFileExists(String fileName)
{
    File directory = ListActivity.this.getFilesDir();
    File file = new File(directory, fileName);
    if(file.exists() && !fileName.equals("")){return true;}
    else {return false;}

}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // do something on back.
            ImageButton additemButton = (ImageButton) findViewById(R.id.oldaddlistitemButton);
            additemButton.callOnClick();

            saveOldList();

            activityDict = customListAdapter.isCheckedDict;
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
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


        configureAddItemText();
        configureFinishButton();
        configureAddItemButton();
        //getIntent().getExtras();f
        configureListNameText();
        readFromFiletoArrayList();
        configureListView();
        //configureExistFileAlert();

    }


    private void ShowExistFileAlert(){
 //       Context context = getApplicationContext();

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ListActivity.this);
        dlgAlert.setMessage("A list with this name already exists! Please change to a new list name.");
        dlgAlert.setTitle("List Already Exists!");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
        //Todo: must create break here, and then create listener for messagebox. It works different from .Net.
    }
    private void configureAddItemText () {
final EditText listItemText = (EditText) findViewById(R.id.oldlistitemEditText);
        listItemText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.i("DONE:","Done pressed in listview edit text");
                   // list.set(position,newFileName);
                    //listItemText.clearFocus();

                    if (listItemText.length() > 0) {
                        addlistItem(listItemText.getText().toString());
                        listItemText.setText("");
                    }
                }
    return true;
    }
        });
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
        File myDir = ListActivity.this.getFilesDir();
        try {
            String filePath = myDir.getPath()+"/"+fileName;
            String fileString = getStringFromFile(filePath);
            int i = 0;
            while(fileString.indexOf("►") != -1) {
                if(fileString.substring(0, fileString.indexOf("►")).equals("▲") || fileString.substring(0, fileString.indexOf("►")).equals("▼")){
                    this.setActivityDict(i,fileString.substring(0, fileString.indexOf("►")));
                    fileString = fileString.substring(fileString.indexOf("►")+1,fileString.length());
                    i=i+1;
                }
                else{
                dataArray.add(fileString.substring(0, fileString.indexOf("►")));
                    fileString = fileString.substring(fileString.indexOf("►")+1,fileString.length());


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

                outputStreamWriter.append(value+"►"+isChecked+"►");
            }
            outputStreamWriter.close();
        }
        catch (IOException e) {

        }
    }

    private void saveOldList() {

        EditText listName = (EditText) findViewById(R.id.oldlistNameText); //grab text of this to set filename

        File directory = ListActivity.this.getFilesDir();
        File file = new File(directory, fileName);
        if(file.exists()){file.delete();}
        file = new File(directory, listName.getText().toString());
        if(file.exists()) {


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

        mainListView = (ListView) findViewById( R.id.oldlistListView);

        customListAdapter = new CustomListAdapter(dataArray,getApplicationContext());
        customListAdapter.isCheckedDict = this.activityDict;

        mainListView.setAdapter(customListAdapter);


        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
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
                else{
                    newlistitemText.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(newlistitemText, InputMethodManager.SHOW_IMPLICIT);

                }

            }

        });
    }

    private void configureFinishButton(){
        ImageButton finishButton = (ImageButton) findViewById(R.id.oldlistviewFinishButton);
        ImageButton additemButton = (ImageButton) findViewById(R.id.oldaddlistitemButton);
        additemButton.callOnClick();
        final EditText editTextName = (EditText) findViewById(R.id.oldlistNameText);
        finishButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO: code to save list somewhere on phone.


                String editTextString = editTextName.getText().toString();
                boolean fileExists = ifFileExists(editTextString);
                if(fileExists && !editTextString.equals(fileName))
                {ShowExistFileAlert();}
                else{
                saveOldList();
                activityDict = customListAdapter.isCheckedDict;
                finish();}
            }

        });
    }
}

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

    private String mFileName;
    private ListView listView;
    private CustomListAdapter customListAdapter;
    private ArrayList<String> dataArray = new ArrayList<String>();
    private Dictionary activityDict = new Hashtable<Integer,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.listview);

        mFileName = getIntent().getStringExtra("fileName");

        configureAddItemText();
        configureFinishButton();
        configureAddItemButton();
        configureListNameText();
        readFromFiletoArrayList();
        configureListView();

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
final EditText listItemText = (EditText) findViewById(R.id.listitemEditText);

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
private boolean ensureUniqueFileName(){

    EditText myEditText = (EditText) findViewById(R.id.listNameText);

    if(mFileName.equals(myEditText.getText().toString())) {
        return true;
    }
    else{
        mFileName = myEditText.getText().toString();
    }

        int i = 1;
        while(FileManager.ifFileExists(mFileName,ListActivity.this)) {
            String fileNameBeforeConcat;
            if(mFileName.lastIndexOf(" ") != -1){
            fileNameBeforeConcat = mFileName.substring(0,mFileName.lastIndexOf(" "));}
            else{
                fileNameBeforeConcat = mFileName;
            }
            mFileName = fileNameBeforeConcat + " " + i;
            i = i + 1;

        }

    return true;

}


    private void configureListNameText(){

        EditText myEditText = (EditText) findViewById(R.id.listNameText);

        if(mFileName == null){
            mFileName = "newList";
        int i = 1;
        while(FileManager.ifFileExists(mFileName,ListActivity.this)) {
            mFileName = "newList " + i;
            i = i + 1;

            }



        myEditText.setText(mFileName);
        myEditText.setFocusable(true);
        myEditText.requestFocus();
        myEditText.selectAll();
        }
        else{
            myEditText.setText(mFileName);
        }


    }
    public void setActivityDict(int position, String value){
        activityDict.put(position,value);
    }

    private void readFromFiletoArrayList(){
        Context context = getApplicationContext();
        File myDir = ListActivity.this.getFilesDir();
        try {
            String filePath = myDir.getPath()+"/"+ mFileName;
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



        File directory = ListActivity.this.getFilesDir();
        File file = new File(directory, mFileName);
        if(file.exists()){file.delete();}
        file = new File(directory, mFileName);
        if(file.exists()) {


            {
                file.delete();
            }
        }

            try {
                file.createNewFile();
                writeToFile(getApplicationContext(),mFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }


        


    }

    private void addlistItem(String item) {
        if(item != null && item.trim()!= "") {
            customListAdapter.Add(item); //TODO: implement adding item to list inside the customlistadapter class
            customListAdapter.notifyDataSetChanged();
            listView.smoothScrollToPosition(listView.getMaxScrollAmount());
            //listView.setAdapter(customListAdapter);
        }
        }

    private void configureListView() {

        listView = (ListView) findViewById( R.id.listListView);

        customListAdapter = new CustomListAdapter(dataArray,getApplicationContext());
        customListAdapter.isCheckedDict = this.activityDict;

        listView.setAdapter(customListAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                customListAdapter.notifyDataSetChanged();
            }
        });
    }



    private void configureFinishButton(){
        ImageButton finishButton = (ImageButton) findViewById(R.id.listviewFinishButton);
        finishButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();}
        });
    }

    public void saveList(String fileName) {

        boolean fileExists = FileManager.ifFileExists(fileName,ListActivity.this);
        if(fileExists && !fileName.equals(mFileName))
        {ShowExistFileAlert();}
        else{
            saveOldList();}
    }

    //@Override
    public void onPause() {

        activityDict = customListAdapter.isCheckedDict;

        ImageButton additemButton = (ImageButton) findViewById(R.id.addlistitemButton);

        EditText editTextName = (EditText) findViewById(R.id.listNameText);

        additemButton.callOnClick();

        ensureUniqueFileName();

        saveList(mFileName);

       super.onPause();
    }
    private void configureAddItemButton(){
        ImageButton additemButton = (ImageButton) findViewById(R.id.addlistitemButton);
        final EditText newlistitemText = (EditText) findViewById(R.id.listitemEditText);

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
}

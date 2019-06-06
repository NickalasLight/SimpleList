package com.example.basicgrocery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;



public class CustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private boolean getViewFlag = true;



    public CustomAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;

        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        Log.i("CustomAdapter getView", "in getView in CustomAdapter");
        View view = convertView;
        if(getViewFlag){
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.complexrow, null);
        }

        //Handle TextView and display string from your list
        final EditText listItemText = (EditText) view.findViewById(R.id.listitemEditText);
        listItemText.setText(list.get(position));
        listItemText.setCursorVisible(false);


        //final KeyListener orgKeyListener = listItemText.getKeyListener();
        //listItemText.setKeyListener(null);
        listItemText.setFocusableInTouchMode(false);
        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton) view.findViewById(R.id.listitemDeleteButton);
        ImageButton addBtn = (ImageButton) view.findViewById(R.id.listitemAddButton);

        final String result = listItemText.getText().toString();

        listItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                //list.remove(position); //or some other task
                //notifyDataSetChanged();
                //listItemText.setText("TEST");
                try {
                    /*
                    Context myContext = v.getContext();
                    Intent myIntent = new Intent(v.getContext(), OldListActivity.class);
                    myIntent.putExtra("fileName", result);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //throws exception if this line is removed
                    myContext.startActivity(myIntent);
                    */
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        listItemText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // If it loses focus...

                if (!hasFocus) {
                    // Hide soft keyboard.
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(listItemText.getWindowToken(), 0);
                    // Make it non-editable again.
                    listItemText.setFocusableInTouchMode(false);
                    listItemText.setSelectAllOnFocus(false);
                    listItemText.setCursorVisible(false);
                    getViewFlag = true;

                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                String fileName = list.get(position).toString();
                FileManager.deleteFile(v.getContext(), fileName);
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                try {

                    Log.i("addBtn.onClick", "editBtn.setonClick clicked");

                    final EditText myEditText = listItemText;


                    //TextView listItemText = (TextView) v.findViewById(R.id.listitemTextView);

                    myEditText.setFocusableInTouchMode(true);
                    //myEditText.setKeyListener(orgKeyListener);
                    myEditText.setSelectAllOnFocus(true);
                    myEditText.requestFocus();

                    myEditText.selectAll();
                    myEditText.setCursorVisible(true);
                    //myEditText.setSelection(myEditText.getText().length());
                    // myEditText.selectAll();
                    //myEditText.setSelection(2);
                    // myEditText.setVisibility(View.INVISIBLE);
                    InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    getViewFlag = false;
                    // myEditText.requestFocus();
                    //myEditText.selectAll();
                    //android.os.SystemClock.sleep(10000);

                    //myEditText.selectAll();
                    //myEditText.setCursorVisible(true);
                    // selectTest(myEditText);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
        return view;
    }

    private void selectTest(EditText myEditText) {
        myEditText.selectAll();
        //myEditText.setCursorVisible(true);
    }
}
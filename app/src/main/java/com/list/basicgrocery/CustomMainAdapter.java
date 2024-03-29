package com.list.basicgrocery;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;



public class CustomMainAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private boolean getViewFlag = true;



    public CustomMainAdapter(ArrayList<String> list, Context context) {
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

    private void ShowDeleteListAlert(final View v, final int position){

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(new ContextThemeWrapper(context.getApplicationContext(), R.style.myDialog));
        dlgAlert.setMessage("Are you sure you want to permanently delete this list?");
        dlgAlert.setTitle("Are You Sure?");
        dlgAlert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Continue with delete operation
                String fileName = list.get(position).toString();
                FileManager.deleteFile(v.getContext(), fileName);
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }});
        dlgAlert.setNegativeButton("NO",null);
        dlgAlert.setCancelable(true);
       dlgAlert.show();
        // dlgAlert.create().show();

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        Log.i("CustomMainAdapter", "in getView in CustomMainAdapter");
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
        ImageButton editBtn = (ImageButton) view.findViewById(R.id.listitemEditButton);

        final String result = listItemText.getText().toString();

        listItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String myResult = result;
                    Context myContext = v.getContext();
                    Intent myIntent = new Intent(v.getContext(), ListActivity.class);

                    if(result == null) {myResult = "newList";}
                    myIntent.putExtra("fileName", myResult);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //throws exception if this line is removed
                    myContext.startActivity(myIntent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        listItemText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

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

        listItemText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.i("DONE:","Done pressed in listview edit text");
                    String oldFileName = list.get(position).toString();
                    String newFileName = listItemText.getText().toString();
                    FileManager.renameFile(context,oldFileName,newFileName);
                    list.set(position,newFileName);
                    listItemText.clearFocus();
                }

                return false;
            }
        });
            listItemText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                        Log.i("ENTER:","Enter pressed in edittext");
                        String oldFileName = list.get(position).toString();
                        String newFileName = listItemText.getText().toString();
                        FileManager.renameFile(context,oldFileName,newFileName);
                    }
                    return false;
                }
            });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fileName = list.get(position).toString();
                FileManager.deleteFile(v.getContext(), fileName);
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Log.i("addBtn.onClick", "editBtn.setonClick clicked");

                    final EditText myEditText = listItemText;

                    myEditText.setFocusableInTouchMode(true);
                    myEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    myEditText.setSelectAllOnFocus(true);
                    myEditText.requestFocus();

                    myEditText.selectAll();
                    myEditText.setCursorVisible(true);
                    InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                    getViewFlag = false;

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
    }
}
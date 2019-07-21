package com.list.basicgrocery;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
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
import java.util.Dictionary;
import java.util.Hashtable;


public class CustomListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private boolean getViewFlag = true;
    public Dictionary isCheckedDict = new Hashtable<Integer,String>();



    public CustomListAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void Add(String item){this.list.add(item);this.isCheckedDict.put(this.list.size()-1,"▼");}

public void setCheckedPosition(int position, String value) {
        isCheckedDict.put(position,value);
}
public String getIsChecked(int position){
        if (isCheckedDict.get(position) != null) {
        return (String) isCheckedDict.get(position);}
        else{return "▼";}
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


        Log.i("CustomListAdapter", "in getView in CustomListAdapter");
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

            if(isCheckedDict.get(position) != null) {
            if(isCheckedDict.get(position).toString().equals("▲")){
                listItemText.setPaintFlags(listItemText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                listItemText.setTextColor(Color.GRAY);

            }
            else{
                listItemText.setPaintFlags(listItemText.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                listItemText.setTextColor(Color.BLACK);


            }
            }


            //final KeyListener orgKeyListener = listItemText.getKeyListener();
            //listItemText.setKeyListener(null);
            listItemText.setFocusableInTouchMode(false);
            //Handle buttons and add onClickListeners
            ImageButton deleteBtn = (ImageButton) view.findViewById(R.id.listitemDeleteButton);
            ImageButton addBtn = (ImageButton) view.findViewById(R.id.listitemEditButton);

            final String result = listItemText.getText().toString();

            listItemText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //do something
                    //list.remove(position); //or some other task
                    //notifyDataSetChanged();
                    //listItemText.setText("TEST");
                    //if listItem text.focus() = true skip over this block.
                    try {

                        /*Context myContext = v.getContext();
                        Intent myIntent = new Intent(v.getContext(), ListActivity.class);
                        myIntent.putExtra("fileName", result);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //throws exception if this line is removed
                        myContext.startActivity(myIntent);

*/
                        if(!listItemText.hasFocus()) {
                            if (isCheckedDict.get(position) == null) {
                                isCheckedDict.put(position, "▼");
                            }

                            Boolean isChecked = Boolean.valueOf(isCheckedDict.get(position).toString());

                            if (isCheckedDict.get(position).toString().equals("▼")) {
                                isCheckedDict.put(position, "▲");
                            } else {
                                isCheckedDict.put(position, "▼");
                            }

                            int color = listItemText.getCurrentTextColor();
                            if (color == Color.GRAY) {
                                listItemText.setTextColor(Color.BLACK);
                            } else {
                                listItemText.setTextColor(Color.GRAY);
                            }

                            listItemText.setPaintFlags(listItemText.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
                        }


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

            listItemText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    if(actionId == EditorInfo.IME_ACTION_DONE) {
                        Log.i("DONE:","Done pressed in listview edit text");
                        //String oldFileName = list.get(position).toString();
                       // String newFileName = listItemText.getText().toString();
                        //FileManager.renameFile(context,oldFileName,newFileName);
                        list.set(position,listItemText.getText().toString());
                        listItemText.setSingleLine(false);
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
                    //do something
                    //String fileName = list.get(position).toString();
                    //FileManager.deleteFile(v.getContext(), fileName);
                    list.remove(position); //or some other task
                    //deleting checked
                    for(int i = position; i < list.size(); i++) {
                        isCheckedDict.remove(i);
                        isCheckedDict.put(i, isCheckedDict.get(i + 1));
                        isCheckedDict.remove(i+1);
                    }

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
                        myEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
                        myEditText.setSingleLine(true);
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
package com.example.basicgrocery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;



public class CustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;



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
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.complexrow, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.listitemTextView);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.listitemDeleteButton);
        ImageButton addBtn = (ImageButton)view.findViewById(R.id.listitemAddButton);

        final String result = listItemText.getText().toString();

        listItemText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                //list.remove(position); //or some other task
                //notifyDataSetChanged();
                //listItemText.setText("TEST");
                try {
                    Context myContext = v.getContext();
                    Intent myIntent = new Intent(v.getContext(), OldListActivity.class);
                    myIntent.putExtra("fileName", result);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //throws exception if this line is removed
                    myContext.startActivity(myIntent);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });


        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                String fileName = list.get(position).toString();
                FileManager.deleteFile(v.getContext(),fileName);
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                try {
                    Context context = v.getContext();

                    TextView listItemText = (TextView) v.findViewById(R.id.listitemTextView);

                    TextView textView = null;
                    ViewGroup row = (ViewGroup) v.getParent();
                    for (int itemPos = 0; itemPos < row.getChildCount(); itemPos++) {
                        View view = row.getChildAt(itemPos);
                        if (view instanceof TextView) {
                            textView = (TextView) view; //Found it!
                            break;
                        }
                    }

                    textView.setFocusable(true);
                    textView.requestFocus();

                    String debugString = textView.getText().toString();

                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(textView, InputMethodManager.SHOW_IMPLICIT);
                    notifyDataSetChanged();
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
}
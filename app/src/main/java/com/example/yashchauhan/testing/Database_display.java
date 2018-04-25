package com.example.yashchauhan.testing;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Database_display extends Fragment {
    Context context;
    FeedReaderContract.FeedReaderDbHelper mDbHelper;
//    String data[]= new String[];
    ArrayList<String> itemIds = new ArrayList<String>();
    TextView columnone,columntwo;


    public Database_display() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = this.getContext();
        mDbHelper =  new FeedReaderContract.FeedReaderDbHelper(context);


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_database_display, container, false);
        columnone = rootView.findViewById(R.id.columnone);
        columntwo = rootView.findViewById(R.id.columntwo);
        itemIds = mDbHelper.getdata();
        Log.d("data is:",String.valueOf(mDbHelper.getdata()));
        Log.d("size of list is:",String.valueOf(itemIds.size()));

        for (int i=0;i<itemIds.size();i++){
            if(i<(itemIds.size()/2))
            columnone.append(itemIds.get(i));
            else
                columntwo.append(itemIds.get(i));
            Log.d("lis values are",itemIds.get(i));
        }
//        columnone.setText(itemIds);
    return rootView;
    }

}

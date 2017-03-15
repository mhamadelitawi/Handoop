package com.handoop.mhamad.handoop.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.handoop.mhamad.handoop.Models.Result;
import com.handoop.mhamad.handoop.R;
import com.handoop.mhamad.handoop.ResultActivity;
import com.handoop.mhamad.handoop.ShowResult;
import com.handoop.mhamad.handoop.Tools.MyListener;

import java.util.ArrayList;

/**
 * Created by Mhamad on 13-Feb-17.
 */

public class ResultsExpandableListAdapter extends BaseExpandableListAdapter {

    public Context context;
    ArrayList<Result> listResults;


    public ResultsExpandableListAdapter(Context context, ArrayList<Result> listResults) {
        this.context = context;
        this.listResults = listResults;
    }

    @Override
    public int getGroupCount() {
        return listResults.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listResults.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listResults.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView( int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final int number = groupPosition;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.result_element_group, null);
        ((TextView) convertView.findViewById(R.id.title)  ).setText(  listResults.get(groupPosition).title  );
        ((TextView) convertView.findViewById(R.id.date)  ).setText(  listResults.get(groupPosition).date  );
        ((TextView) convertView.findViewById(R.id.time)  ).setText(  listResults.get(groupPosition).time  );

        ImageButton delete = (ImageButton)  convertView.findViewById(R.id.delete);
        delete.setFocusable(false);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultActivity r = (ResultActivity) context;
                r.removeAnElement(number);
                Toast.makeText(context, "delete " + number, Toast.LENGTH_LONG).show();
            }
        });


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.result_element_child, null);

        ImageButton showpreview = (ImageButton)  convertView.findViewById(R.id.showpreview);
        showpreview.setFocusable(false);
        showpreview.setOnClickListener(new MyListener(context , groupPosition) {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ShowResult.class);
                intent.putExtra("guid" , listResults.get(position).guid );
                intent.putExtra("preview" , listResults.get(position).preview );
                intent.putExtra("task" , listResults.get(position).task );
                context.startActivity(intent);
                Toast.makeText(context, "showpreview " + position, Toast.LENGTH_LONG).show();
            }
        });


        ImageButton downloadfull = (ImageButton)  convertView.findViewById(R.id.downloadfull);
        downloadfull.setFocusable(false);
        downloadfull.setOnClickListener(new MyListener(context , groupPosition) {
            @Override
            public void onClick(View v) {
                download( listResults.get(position).pathresult , listResults.get(position).guid );
                Toast.makeText(context, "downloadfull " + position, Toast.LENGTH_LONG).show();
            }
        });


        ImageButton copytoclipboard = (ImageButton)  convertView.findViewById(R.id.copytoclipboard);
        copytoclipboard.setFocusable(false);


        copytoclipboard.setOnClickListener(new MyListener(context , groupPosition) {
            @Override
            public void onClick(View v) {
               // download( listResults.get(position).pathresult , listResults.get(position).guid );
                setClipboard( getDownloadLink(listResults.get(position).pathresult));
                Toast.makeText(context, "copytoclipboard " + position, Toast.LENGTH_LONG).show();
            }
        });





        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

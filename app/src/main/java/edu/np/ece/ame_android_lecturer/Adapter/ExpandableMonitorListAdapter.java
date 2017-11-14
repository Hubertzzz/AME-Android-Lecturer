package edu.np.ece.ame_android_lecturer.Adapter;

import android.content.Context;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.np.ece.ame_android_lecturer.Model.ListAttendanceStatus;
import edu.np.ece.ame_android_lecturer.Model.StudentInfo;
import edu.np.ece.ame_android_lecturer.R;

/**
 * Created by MIYA on 07/11/17.
 */

public class ExpandableMonitorListAdapter extends BaseExpandableListAdapter {

    Context context;
    int layoutResourceId;
    List<ListAttendanceStatus> data;
    List<StudentInfo> studentInfos;
    String status;
    List<List<String>> expandedData;

    public ExpandableMonitorListAdapter(Context context, int layoutResourceId, List<ListAttendanceStatus> data, List<StudentInfo> studentInfos, List<List<String>> expandedData) {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.studentInfos = studentInfos;
        this.expandedData = expandedData;
    }

    @Override
    public int getGroupCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return expandedData.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return expandedData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        View row = convertView;
        ExpandableMonitorListAdapter.Holder holder=null;
        if(row==null){
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = mInflater.inflate(layoutResourceId, parent, false);
            holder=new ExpandableMonitorListAdapter.Holder();
            holder.tvcard=(TextView)row.findViewById(R.id.tvstu_card);
            holder.tvorder=(TextView)row.findViewById(R.id.tvorder);
            holder.tvstu_name=(TextView)row.findViewById(R.id.tvstud_name);
            holder.imgcheckbox=(ImageView)row.findViewById(R.id.imgCheckbox);
            row.setTag(holder);
        }
        else {
            holder=(ExpandableMonitorListAdapter.Holder)row.getTag();
        }
        holder.tvstu_name.setText(studentInfos.get(groupPosition).getName()); //now showing the student_id
        holder.tvcard.setText(studentInfos.get(groupPosition).getCard());
        holder.tvorder.setText(String.valueOf(groupPosition));
        status=data.get(groupPosition).getStatus();

        if(status.equals("-1")){
            //absent
            holder.imgcheckbox.setImageResource(R.drawable.circle_red_32);
        }
        else if(status.equals("0")){
            //present
            holder.imgcheckbox.setImageResource(R.drawable.circle_green_32);
        }
        else {
            //late
            holder.imgcheckbox.setImageResource(R.drawable.circle_orange_32);
        }


        return row;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View childrow=convertView;
        ExpandableMonitorListAdapter.ChildHolder childHolder=null;
        if(childrow==null){
            LayoutInflater mInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            childrow=mInflater.inflate(R.layout.child_item_monitor,null);
            childHolder=new ExpandableMonitorListAdapter.ChildHolder();
             childHolder.tvchoice=(TextView)childrow.findViewById(R.id.tvChoice);
            childrow.setTag(childHolder);
        }else {
            childHolder=(ExpandableMonitorListAdapter.ChildHolder)childrow.getTag();
        }
        String text=expandedData.get(groupPosition).get(childPosition);
        childHolder.tvchoice.setText(text);




        return childrow;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }
    static class Holder{
        TextView tvstu_name;
        TextView tvcard;
        TextView tvorder;
        ImageView imgcheckbox;

    }
    static class ChildHolder{
        TextView tvchoice;
    }

}

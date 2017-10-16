package edu.np.ece.ame_android_lecturer.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.np.ece.ame_android_lecturer.Model.ListAttendanceStatus;
import edu.np.ece.ame_android_lecturer.Model.StudentInfo;
import edu.np.ece.ame_android_lecturer.R;

/**
 * Created by MIYA on 08/09/17.
 */

public class MonitorListAdapter extends ArrayAdapter {
    Context context;
    int layoutResourceId;
    List<ListAttendanceStatus> data;
    List<StudentInfo> studentInfos;
    String status;

    public MonitorListAdapter(Context context, int layoutResourceId , List<ListAttendanceStatus> data, List<StudentInfo> studentInfos) {
        super(context, layoutResourceId, data);
        this.context=context;
        this.layoutResourceId=layoutResourceId;
        this.data=data;
        this.studentInfos=studentInfos;

    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MonitorListAdapter.Holder holder=null;
        if(row==null){
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = mInflater.inflate(layoutResourceId, parent, false);
            holder=new MonitorListAdapter.Holder();
            holder.tvcard=(TextView)row.findViewById(R.id.tvstu_card);
            holder.tvorder=(TextView)row.findViewById(R.id.tvorder);
            holder.tvstu_name=(TextView)row.findViewById(R.id.tvstud_name);
            holder.imgcheckbox=(ImageView)row.findViewById(R.id.imgCheckbox);
            row.setTag(holder);
        }
        else {
            holder=(MonitorListAdapter.Holder)row.getTag();
        }
        holder.tvstu_name.setText(studentInfos.get(position).getName()); //now showing the student_id
        holder.tvcard.setText(studentInfos.get(position).getCard());
        holder.tvorder.setText(String.valueOf(position));
        status=data.get(position).getStatus();
        if(status.equals("-1")){
            //absent
            holder.imgcheckbox.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
        }
        else if(status.equals("0")){
            //present
            holder.imgcheckbox.setImageResource(R.drawable.ic_check_box_black_24dp);
        }
        else {
            //late
            holder.imgcheckbox.setImageResource(R.drawable.ic_check_box_black_24dp);
        }
        return row;
    }

    static class Holder{
        TextView tvstu_name;
        TextView tvcard;
        TextView tvorder;
        ImageView imgcheckbox;

    }
}

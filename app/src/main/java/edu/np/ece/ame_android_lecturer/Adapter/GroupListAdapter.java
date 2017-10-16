package edu.np.ece.ame_android_lecturer.Adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import edu.np.ece.ame_android_lecturer.Model.TimetableResult;
import edu.np.ece.ame_android_lecturer.R;


/**
 * Created by MIYA on 07/09/17.
 */

public class GroupListAdapter extends ArrayAdapter<String> {
    Context context;
    int layoutResourceId;
    List<String> data;
    List<String> catalog;
    List<String> subjectarea;


    public GroupListAdapter(Context context, int layoutResourceId, List<String> data, List<String> catalog, List<String> subjectarea){
        super(context, layoutResourceId,data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.catalog = catalog;
        this.subjectarea=subjectarea;

    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        GroupListAdapter.Holder holder=null;
        if(row==null){
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = mInflater.inflate(layoutResourceId, parent, false);
            holder=new GroupListAdapter.Holder();
            holder.tvgroup=(TextView)row.findViewById(R.id.tvgroup);
            holder.tvcatalog=(TextView)row.findViewById(R.id.tvcatalog);

        }else {
            holder=(GroupListAdapter.Holder)row.getTag();
        }
        try {
            holder.tvgroup.setText(data.get(position));
            holder.tvcatalog.setText(catalog.get(position)+" "+subjectarea.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }
    static class Holder {
        TextView tvgroup;
        TextView tvcatalog;

    }
}

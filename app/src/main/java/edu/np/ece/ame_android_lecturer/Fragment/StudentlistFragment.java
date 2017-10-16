package edu.np.ece.ame_android_lecturer.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import edu.np.ece.ame_android_lecturer.Adapter.GroupListAdapter;
import edu.np.ece.ame_android_lecturer.LogInActivity;
import edu.np.ece.ame_android_lecturer.Model.StudentInfo;
import edu.np.ece.ame_android_lecturer.Preferences;
import edu.np.ece.ame_android_lecturer.R;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerApi;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerCallBack;
import edu.np.ece.ame_android_lecturer.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Response;


public class StudentlistFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View myView;
    private String lesson_id;
    private List<StudentInfo> studentList;
    private List<String> stuname= new ArrayList<>();
    private List<String> stucard=new ArrayList<>();
    private List<String> order=new ArrayList<>();

    public StudentlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentlistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentlistFragment newInstance(String param1, String param2) {
        StudentlistFragment fragment = new StudentlistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void initStudentlist(){
        final ListView listView = (ListView) myView.findViewById(R.id.student_list);
        for (int i=0;i<studentList.size();i++){
            stuname.add(studentList.get(i).getName());
            stucard.add(studentList.get(i).getCard());
            order.add(String.valueOf(i+1));

        }

        StudentListAdapter adapter = new StudentListAdapter(getActivity(), R.layout.item_studentlist,stuname,stucard,order);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();




    }
    public void loadStudentList(){
        Bundle arguments = getArguments();
        if(arguments!=null){
            lesson_id=arguments.getString("lesson_id");
        }
        Preferences.showLoading(getActivity(), "Students List", "Loading data from server...");
        try {
            SharedPreferences pref = getActivity().getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
            String auCode = pref.getString("authorizationCode", null);

            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
            JsonObject toUp = new JsonObject();
            toUp.addProperty("lesson_id",lesson_id);
            Call<List<StudentInfo>> call = client.getStudentList(toUp);
            call.enqueue(new ServerCallBack<List<StudentInfo>>() {
                @Override
                public void onResponse(Call<List<StudentInfo>> call, Response<List<StudentInfo>> response) {
                    Preferences.dismissLoading();
                    try {
                        studentList=response.body();
                        if(studentList==null){
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("ERROR");
                            builder.setMessage("Cannot find student list");
                            builder.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialogInterface, final int i) {
                                            Preferences.clearLecturerInfo();
                                            Intent intent = new Intent(getActivity(), LogInActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    });
                            builder.create().show();
                        }
                        else {
                            initStudentlist();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });




        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView=inflater.inflate(R.layout.fragment_studentlist, container, false);
        loadStudentList();
        return myView;
    }

    public class StudentListAdapter extends ArrayAdapter {
        Context context;
        int layoutResourceId;
        List<String> name;
        List<String> card;
        List<String> order;

        public StudentListAdapter(Context context,int layoutResourceId,List<String> name,List<String> card,List<String>order) {
            super(context, layoutResourceId, name);
            this.card=card;
            this.name=name;
            this.context=context;
            this.layoutResourceId=layoutResourceId;
            this.order=order;
        }

        @Override
        public int getCount() {
            return name == null ? 0 : name.size();
        }

        @Override
        public View getView(int position,View convertView, ViewGroup parent) {
            View row = convertView;
            StudentListAdapter.Holder holder=null;
            if(row==null){
                LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = mInflater.inflate(layoutResourceId, parent, false);
                holder=new StudentListAdapter.Holder();
                holder.tvname=(TextView)row.findViewById(R.id.tvstu_name);
                holder.tvcard=(TextView)row.findViewById(R.id.tvstu_card);
                holder.tvorder=(TextView)row.findViewById(R.id.tvstu_order);

            }else {
                holder=(StudentListAdapter.Holder)row.getTag();
            }
            try {
                holder.tvname.setText(name.get(position));
                holder.tvcard.setText(card.get(position));
                holder.tvorder.setText(order.get(position));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return row;
        }



         class Holder {
            TextView tvname;
            TextView tvcard;
            TextView tvorder;
        }
    }



}

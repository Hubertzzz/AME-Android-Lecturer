package edu.np.ece.ame_android_lecturer.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.np.ece.ame_android_lecturer.Adapter.TimetableListAdapter;
import edu.np.ece.ame_android_lecturer.LogInActivity;
import edu.np.ece.ame_android_lecturer.Model.Lesson;
import edu.np.ece.ame_android_lecturer.Model.LessonBeacon;
import edu.np.ece.ame_android_lecturer.Model.LessonDate;
import edu.np.ece.ame_android_lecturer.Model.TimetableResult;
import edu.np.ece.ame_android_lecturer.Model.Venue;
import edu.np.ece.ame_android_lecturer.OrmLite.DatabaseManager;
import edu.np.ece.ame_android_lecturer.OrmLite.Subject;
import edu.np.ece.ame_android_lecturer.OrmLite.SubjectDateTime;
import edu.np.ece.ame_android_lecturer.Preferences;
import edu.np.ece.ame_android_lecturer.R;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerApi;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerCallBack;
import edu.np.ece.ame_android_lecturer.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Response;


public class TimetableFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
  /*  private String mParam1;
    private String mParam2;
    */
    private Activity context;
    private Calendar calendar;

    private List<TimetableResult> timetableList;

    private View myView;

    private List<TimetableResult> data = new ArrayList<>();

    private List<Integer> itemType = new ArrayList<>();

    private List<LessonDate> lessonDatesResult=new ArrayList<>();
    private List<LessonDate> lessonDateList=new ArrayList<>();


    public TimetableFragment() {
        // Required empty public constructor
    }


    public static TimetableFragment newInstance(String param1, String param2) {
        TimetableFragment fragment = new TimetableFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();
        calendar = Calendar.getInstance();
    }

    @Override
    public void onResume() {
        super.onResume();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(dateFormat.format(calendar.getTime()));
    }

    private boolean isOnDifferentDate(LessonDate temp1, LessonDate temp2) {

        if (temp1.getLdate().compareToIgnoreCase(temp2.getLdate()) == 0) {
            return false;
        }
        return true;
    }
    private void addItem(TimetableResult subject,LessonDate date, Integer type) {
        data.add(subject);
        lessonDateList.add(date);
        itemType.add(type);
    }


    private void initTimetableList() {
        try {
            final ListView listView = (ListView) myView.findViewById(R.id.timetable_list);

            for (int i = 0; i <timetableList.size(); i++) {
                lessonDatesResult=timetableList.get(i).getLesson_date();
                /*for(int n=0;n<lessonDatesResult.size();n++){
                    if (i == 0 || isOnDifferentDate(lessonDatesResult.get(n), lessonDatesResult.get(n - 1))) {
                        addItem(timetableList.get(i),lessonDatesResult.get(n), Preferences.LIST_ITEM_TYPE_1);
                    }
                    addItem(timetableList.get(i),lessonDatesResult.get(n), Preferences.LIST_ITEM_TYPE_2);
                }*/

                if (i == 0 || isOnDifferentDate(timetableList.get(i).getLesson_date().get(i), timetableList.get(i - 1).getLesson_date().get(i))) {
                    addItem(timetableList.get(i),timetableList.get(i).getLesson_date().get(i), Preferences.LIST_ITEM_TYPE_1);
                }
                addItem(timetableList.get(i),timetableList.get(i).getLesson_date().get(i), Preferences.LIST_ITEM_TYPE_2);

            }


            TimetableListAdapter adapter = new TimetableListAdapter(context, R.layout.item_subject, R.layout.item_week_day,data,lessonDateList,itemType);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTimetable() {
        Preferences.showLoading(context, "Timetable", "Loading data from server...");
        try {
            SharedPreferences pref = getActivity().getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
            String auCode = pref.getString("authorizationCode", null);

            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);


            String expand = new String("lesson,venue,lesson_date,beacon_lesson");
            Call<List<TimetableResult>> call = client.getTimetableCurrentWeek(expand);
            call.enqueue(new ServerCallBack<List<TimetableResult>>() {
                @Override
                public void onResponse(Call<List<TimetableResult>> call, Response<List<TimetableResult>> response) {
                    try {
                        timetableList = response.body();
                        if (timetableList == null) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Detect another login.");
                            builder.setMessage("You will automatically sign out. Click here to sign in again.");
                            builder.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialogInterface, final int i) {
                                            Preferences.clearLecturerInfo();
                                            Intent intent = new Intent(context, LogInActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    });
                            builder.create().show();

                        } else {
                            initTimetableList();


                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Preferences.dismissLoading();

                   // BeaconScanActivation.timetableList = timetableList;
                }



            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_timetable, container, false);
        loadTimetable();
        return myView;
    }





}

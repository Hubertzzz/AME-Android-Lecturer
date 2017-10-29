package edu.np.ece.ame_android_lecturer.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.np.ece.ame_android_lecturer.Preferences;
import edu.np.ece.ame_android_lecturer.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link LecturerInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LecturerInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View myView;
    private Activity context;

    // TODO: Rename and change types of parameters




    public LecturerInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LecturerInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LecturerInfoFragment newInstance(String param1, String param2) {
        LecturerInfoFragment fragment = new LecturerInfoFragment();
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

    }

    private void loadInfo(){

            final TextView tvName = (TextView)myView.findViewById(R.id.tvName);
            final TextView tvOffice = (TextView)myView.findViewById(R.id.tvOffice);
            final TextView tvEmail = (TextView)myView.findViewById(R.id.tvEmail);
        try{
            SharedPreferences pref = getActivity().getSharedPreferences(Preferences.SharedPreferencesTag,Preferences.SharedPreferences_ModeTag);
            tvName.setText(pref.getString("Lecturer_name",""));
            tvOffice.setText("BLK 7 Level 3");
            tvEmail.setText(pref.getString("Lecturer_email",""));

        }catch (Exception e ){
            e.printStackTrace();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loadInfo();
        myView = inflater.inflate(R.layout.fragment_lecturer_info,container,false);
        return myView;
    }

    // TODO: Rename method, update argument and hook method into UI event




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}

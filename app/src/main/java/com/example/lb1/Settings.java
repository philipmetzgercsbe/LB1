package com.example.lb1;

import android.content.Context;
import android.location.Criteria;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Settings.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Settings extends Fragment {

    private View view;
    private Switch SwitchFine;
    private Switch SwitchRough;
    private Switch.OnCheckedChangeListener ClicklistenerFine =  new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked) {
                MainActivity.MapCritera.setAccuracy(Criteria.ACCURACY_FINE);
            }
        }
    };
    private Switch.OnCheckedChangeListener ClicklistenerRough =  new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked) {
                MainActivity.MapCritera.setAccuracy(Criteria.ACCURACY_COARSE);

            }
        }
    };;


    private OnFragmentInteractionListener mListener;

    public Settings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Settings.
     */
    // TODO: Rename and change types and number of parameters
    public static Settings newInstance(String param1, String param2) {
        Settings fragment = new Settings();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert view != null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.powersetttings);
        SwitchFine = (Switch) view.findViewById(R.id.switchfine);
        SwitchRough = (Switch) view.findViewById(R.id.switchrough);
        SwitchFine.setOnCheckedChangeListener(ClicklistenerFine);
        SwitchRough.setOnCheckedChangeListener(ClicklistenerRough);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch(checkedId) {
                    case R.id.radioHigh:
                        MainActivity.MapCritera.setPowerRequirement(Criteria.POWER_HIGH);
                        break;
                    case R.id.radioMedium:
                        MainActivity.MapCritera.setPowerRequirement(Criteria.POWER_MEDIUM);
                        break;
                    case R.id.radioLow:
                        MainActivity.MapCritera.setPowerRequirement(Criteria.POWER_LOW);
                        break;
                    default:
                       if(SwitchFine.isChecked()){
                           MainActivity.MapCritera.setAccuracy(Criteria.ACCURACY_FINE);
                       }else if(SwitchRough.isChecked()){
                           MainActivity.MapCritera.setAccuracy(Criteria.ACCURACY_COARSE);
                       }else{
                           MainActivity.MapCritera.setPowerRequirement(Criteria.POWER_MEDIUM);

                       }
                }
            }
        });
        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }
}

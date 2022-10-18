package com.example.ctl_manageer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.color.MaterialColors;
import com.google.android.material.theme.MaterialComponentsViewInflater;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    private TextView logout;
    private TextView contactUs;
    private TextView aboutUs;
    private TextView graph;
    private SwitchCompat themeSwitch;

    public static final String MyPreff ="nightModePref";
    public static final String KEY_MODE ="isNightMode";
    SharedPreferences sharedPreferences;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            getActivity().setTheme(R.style.Theme_Dark);
        } else {
            getActivity().setTheme(R.style.Theme_Light);
        }

        contactUs = (TextView)getActivity().findViewById(R.id.tv_contactUs);
        aboutUs = (TextView)getActivity().findViewById(R.id.tv_aboutUs);
        logout = (TextView)getActivity().findViewById(R.id.tv_logout);
        graph = (TextView)getActivity().findViewById(R.id.tv_chart);
        themeSwitch = getActivity().findViewById(R.id.themeSwitch);

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),ContactUs.class);
                startActivity(i);
            }
        });

        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getActivity().getIntent();
                String username = i.getStringExtra("usernameDt");

                Intent gp = new Intent(getActivity(),progress.class);
                gp.putExtra("userN",username);
                startActivity(gp);
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(),AboutUsP.class);
                startActivity(in);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getActivity().getIntent();
                String username = intent.getStringExtra("usernameDt");

                if(username != null){
                    Toast.makeText(getActivity(), "Goodbye " + username, Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(getActivity(),Login.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getActivity(), "Goodbye", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(getActivity(),Login.class);
                    startActivity(i);
                }
            }
        });

        sharedPreferences = getActivity().getSharedPreferences(MyPreff, Context.MODE_PRIVATE);

        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    saveDayNightModeState(true);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    saveDayNightModeState(false);
                }
            }
        });
    }

    private void saveDayNightModeState(boolean nightMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_MODE, nightMode);
        editor.apply();
    }


}
package com.example.ctl_manageer;

import android.content.Intent;
import android.media.session.MediaSessionManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.cast.framework.SessionManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FolderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FolderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FolderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FolderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FolderFragment newInstance(String param1, String param2) {
        FolderFragment fragment = new FolderFragment();
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

        View view = inflater.inflate(R.layout.fragment_folder, container, false);

        // Inflate the layout for this fragment
        return view;
    }

    private ListView listView;
    private DatabaseReference reference;
    private String username;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceSate) {
        super.onActivityCreated(savedInstanceSate);

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            getActivity().setTheme(R.style.Theme_Dark);
        } else {
            getActivity().setTheme(R.style.Theme_Light);
        }

        Intent i = getActivity().getIntent();
        username = i.getStringExtra("usernameDt");

        ImageButton add = (ImageButton) getActivity().findViewById(R.id.btn_Add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(),pop.class);
                in.putExtra("username",username);
                startActivity(in);

            }
        });

        dt();
    }


    FirebaseDatabase db;
    DatabaseReference ref;
    ListView list;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> alist = new ArrayList<>();
    String[] coll;

    private void dt()
    {

        db = FirebaseDatabase.getInstance();
        ref = db.getReference("users").child(username).child("collection");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                userDetails ud = snapshot.getValue(userDetails.class);

                listView = getActivity().findViewById(R.id.lv_room_items);

                String[] collections = {ud.getListName()};

                for (String lst: collections) {
                    alist.add(lst);
                }

                ArrayAdapter<String> collectionAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_view_item_color,alist);
                listView.setAdapter(collectionAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String collect = parent.getItemAtPosition(position).toString();
                        int pos = position;

                        if(position == pos)
                        {
                            Toast.makeText(getActivity().getApplicationContext(),"clicked " + parent.getItemAtPosition(pos).toString(),Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getActivity(),Collection2.class);
                            i.putExtra("CollectionName",collect);
                            i.putExtra("username",username);
                            startActivity(i);
                        }
                        else {
                            Toast.makeText(getActivity().getApplicationContext(),"Not found",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
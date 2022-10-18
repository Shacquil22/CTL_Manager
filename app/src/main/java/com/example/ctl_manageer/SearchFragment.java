package com.example.ctl_manageer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    private ListView lv;
    private ArrayList<String> list;
    private ArrayAdapter adapter;
    private SearchView sv;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            getActivity().setTheme(R.style.Theme_Dark);
        } else {
            getActivity().setTheme(R.style.Theme_Light);
        }

        lv = (ListView) getActivity().findViewById(R.id.list);
        sv = (SearchView) getActivity().findViewById(R.id.search);

        searchItem();

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter = new ArrayAdapter(getActivity(),R.layout.list_view_item_color,list);
//                lv.setAdapter(adapter);
//
//                adapter.getFilter().filter(newText);

                Intent i = getActivity().getIntent();
                String username = i.getStringExtra("usernameDt");

                //list = new ArrayList<String>();

                //list.add(username);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("users");

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String un = String.valueOf(snapshot.child(username).child("collection").child(query).child("listName").getValue());

                            String[] u = {un};

                            for (String m: u) {
                                list.add(m);
                            }

                            adapter = new ArrayAdapter(getActivity(),R.layout.list_view_item_color,list);
                            lv.setAdapter(adapter);

                            adapter.getFilter().filter(query);

                            if(query.equals(null)){
                                list.clear();
                            }

                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

                        } else {
                            Toast.makeText(getActivity(), "Does not exist.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return false;
            }
        });

    }

    private void searchItem(){
        list = new ArrayList<String>();
//        list.add("Monday");
//        list.add("Tuesday");
//        list.add("Wednesday");
//        list.add("Thursday");
//        list.add("Friday");
//        list.add("Saturday");
//        list.add("Sunday");

        //searchDatabaseItem();

    }

//    private void searchDatabaseItem(){
//
//    }

}
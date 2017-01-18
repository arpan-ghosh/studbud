package com.hophacks.hhspring;

/**
 * Created by Rebecca on 2/6/2016.
 */
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassesFragment extends Fragment {

    FloatingActionButton floatingActionButton;
    EditText test;
    ListView list;
    //TextView result;

    String[] dummyClasses={"Intermediate Programming",
            "Data Structures",
            "Computer System Fundamentals",
            "User Interfaces and Mobile Applications",
            "Database Systems",
            "Video Game Design Project",
            "Automata & Computation Theory",
            "Introduction to Algorithms",
            "Natural Language Processing",
            "Underwater Basket Weaving"};

    //ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dummyClasses);

    ArrayList<String> classes = new ArrayList<String>();

    public ClassesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_classes, container, false);

        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab);
        setupFloatingActionButton();

        //String[] dummyClasses={"Math", "Science", "History", "Intro Java", "Underwater Basket Weaving"};
        //List<String> dummyClasses = Arrays.asList("id", "beth");

        ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dummyClasses);

        test = (EditText) rootView.findViewById(R.id.editText);
        list = (ListView) rootView.findViewById(R.id.listView);

        list.setAdapter(myAdapter);

        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent,
                                    View v,
                                    int position,
                                    long id) {
                if (!classes.contains(dummyClasses[position])) {
                    classes.add(dummyClasses[position]);
                    String text = test.getText().toString();

                    if (text.equals("")) {
                        test.setText(dummyClasses[position]);
                    } else {
                        test.setText(text + ", " + dummyClasses[position]);
                    }
                }
            }
        };

        list.setOnItemClickListener(mMessageClickedHandler);
        //result = (TextView) rootView.findViewById(R.id.textView);

        // Inflate the layout for this fragment
        return rootView;
    }

    public void setupFloatingActionButton() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // HANDLE FLOATING ACTION BUTTON
                new RetrieveFeedTask().execute();

                // add classes to parse
                ParseUser user = ParseUser.getCurrentUser();
                user.put("classes", classes);
                user.saveInBackground();

                Log.d("classes", classes.toString());

                Toast.makeText(getContext(), "Added classes!", Toast.LENGTH_LONG).show();
                test.setText("");
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            //progressBar.setVisibility(View.VISIBLE);
            //responseView.setText("");
        }

        protected String doInBackground(Void... urls) {
            String thing = test.getText().toString();
            // Do some validation here

            try {
                URL url = new URL("https://isis.jhu.edu/api/classes/" + thing + "/Spring%202016?key=x8DelW0fnBEfgsCIVG4V1SjAHpaiyAa8");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            //progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            Log.i("INFO", classes.toString());
            //result.setText(response);
        }
    }
}

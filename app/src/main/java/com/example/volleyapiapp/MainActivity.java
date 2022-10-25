package com.example.volleyapiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {


    // creating variables for our edittext,
    // button, textview and
    private EditText edtName, edtJob;
    private Button btnPost;
    private TextView tVResponse;

    private String name, job;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing our views
        edtName = findViewById(R.id.idEdtName);
        edtJob= findViewById(R.id.idEdtJob);
        btnPost = findViewById(R.id.idBtnPost);
        tVResponse = findViewById(R.id.idTVResponse);

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Sending data to api.")
                .setCancelable(false)
                .build();


        // adding on click listener to our button.
        btnPost.setOnClickListener( e -> {
            validateData();
        });


    }

    private void validateData() {

        name = edtName.getText().toString();
        job = edtJob.getText().toString();

        // validating if the text field is empty or not.
        if (TextUtils.isEmpty(name)){
            edtName.setError("Name field is empty. Enter Name.");
            return;
        }
        if (TextUtils.isEmpty(job)){
            edtName.setError("Job field is empty. Enter Job.");
            return;
        }

        // calling a method to post the data and passing our name and job.
        postDataUsingVolley(name, job);

    }

    private void postDataUsingVolley(String name, String job) {
        dialog.setTitle("Please wait...");
        dialog.setMessage("Sending data to api.");
        dialog.show();
        // url to post our data
        String url = "https://reqres.in/api/users";

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        //post data
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //hide dialog
                dialog.dismiss();

                //clear input
                edtName.setText("");
                edtJob.setText("");
                Toast.makeText(MainActivity.this, "Data Added to Api.", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);

                    // below are the strings which we
                    // extract from our json object.
                    String name = respObj.getString("name");
                    String job = respObj.getString("job");

                    // on below line we are setting this string s to our text view.
                    tVResponse.setText("Name: "+name+"\nJob: "+job);


                }
                catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error: An error occurred.\nHint: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Handle errors
                Toast.makeText(MainActivity.this, "Error: Failed to get response. \nHint: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("name", name);
                params.put("job", job);

                //return params
                return params;
            }
        };

        //make a json request
        queue.add(request);

    }
}
package com.example.nus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nus.ui.Event.EventFragment;
import com.example.nus.ui.building.buildingFragment;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class Show_Event extends AppCompatActivity {
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private EditText Abstract;
    private EditText detail;
    private EditText time;
    private String[] information;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_event);
        Abstract = (EditText) findViewById(R.id.show_Abstract);
        detail = (EditText) findViewById(R.id.show_Detail);
        time = (EditText) findViewById(R.id.show_Time);
        Intent intent = getIntent();
        information = intent.getStringArrayExtra(EventFragment.information);
        Abstract.setText(information[0]);
        detail.setText(information[1]);
        time.setText(information[2].substring(0,10));
        setTitle(information[0]);
    }
    public void goBack(View view){
        finish();
    }
    public void delete(View view){
        new Thread(){
            public void run(){
                try {
                    URL url = new URL("http://ec2-18-232-62-169.compute-1.amazonaws.com:8004/user/1/event/"+information[3]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("DELETE");
                    conn.setDoOutput(true);
                    conn.setRequestProperty(
                            "Content-Type", "application/x-www-form-urlencoded" );
                    conn.setConnectTimeout(5000);
                    String res;
                    try(BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine = null;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        res = response.toString();
                    }

                    if(conn.getResponseCode()!=200){
                        Message msg = new Message();
                        msg.what = FAILURE;
                        msg.obj = res;
                        handler.sendMessage(msg);
                    }
                    else{
                        Message msg = new Message();
                        msg.obj = null;
                        msg.what = SUCCESS;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = FAILURE;
                    msg.obj = e.getMessage();
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    public void update(View view){
        new Thread(){
            public void run(){
                try {
                    URL url = new URL("http://ec2-18-232-62-169.compute-1.amazonaws.com:8004/user/1/event/"+information[3]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("PUT");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("abstracts", Abstract.getText());
                    jsonParam.put("details", detail.getText());
                    jsonParam.put("id", Integer.parseInt(information[3]));
                    jsonParam.put("time", time.getText()+"T04:02:26.293Z");
                    jsonParam.put("user_id", 1);

                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

//                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
//                    Log.i("MSG" , conn.getResponseMessage());
                    String res;
                    try(BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine = null;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        res = response.toString();
                    }

                    if(conn.getResponseCode()!=200){
                        Message msg = new Message();
                        msg.what = FAILURE;
                        msg.obj = res;
                        handler.sendMessage(msg);
                    }
                    else{
                        Message msg = new Message();
                        msg.obj = "TEST";
                        msg.what = SUCCESS;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = FAILURE;
                    msg.obj = e.getMessage();
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    if(msg.obj.equals("Deleted"))
                        finish();
                    else{
                        Toast.makeText(Show_Event.this, "Updated", Toast.LENGTH_SHORT)
                                .show();
                    }
                    break;

                case FAILURE:
                    Toast.makeText(Show_Event.this, msg.obj.toString() , Toast.LENGTH_SHORT)
                            .show();
                    break;

                default:
                    break;
            }
        };
    };
}
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

import com.example.nus.ui.building.buildingFragment;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class Add_Event extends AppCompatActivity {
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private EditText Abstract, detail, time;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        setTitle("Add new Event");
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(Add_Event.this, "Unknown Error", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    }
                    break;

                case FAILURE:
                    Toast.makeText(Add_Event.this, msg.obj.toString() , Toast.LENGTH_SHORT)
                            .show();
                    break;

                default:
                    break;
            }
        };
    };
    public void cancle(View view){
        finish();
    }
    public void save(View view) throws IOException {
        EditText Abstract = (EditText) findViewById(R.id.Abstract);
        EditText detail = (EditText) findViewById(R.id.Detail);
        EditText time = (EditText) findViewById(R.id.Time);
        new Thread(){
            public void run(){
                try {
                    URL url = new URL("http://ec2-18-232-62-169.compute-1.amazonaws.com:8004/user/1/event");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("abstracts", Abstract.getText());
                    jsonParam.put("details", detail.getText());
                    jsonParam.put("id", 1);
                    jsonParam.put("time", time.getText()+"T04:02:26.293Z");
                    jsonParam.put("user_id", 1);

                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
//                    os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
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
}

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nus.ui.building.buildingFragment;

import java.io.BufferedReader;
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


public class building_information extends AppCompatActivity {
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.building_information);
        TextView text_view = findViewById(R.id.description);
        Intent intent = getIntent();
        String[] information = intent.getStringArrayExtra(buildingFragment.information);
        text_view.setText(information[0]);
        getinformation(information[1], "image");
        getinformation(information[2], "location");
        setTitle(information[3]);
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ImageView img = findViewById(R.id.buildingphoto);
            ImageView location = findViewById(R.id.buildinglocation);
            switch (msg.what) {
                case SUCCESS:
                    try {
                        if(msg.arg1==0)
                            img.setImageBitmap((Bitmap) msg.obj);
                        else
                            location.setImageBitmap((Bitmap) msg.obj);

                    } catch (Exception e) {
                        Toast.makeText(building_information.this, "Decode failed", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    }
                    break;

                case FAILURE:
                    Toast.makeText(building_information.this, msg.obj.toString() , Toast.LENGTH_SHORT)
                            .show();
                    break;

                default:
                    break;
            }
        };
    };

    protected void getinformation(String url, String _which){
        String path = url;
        new Thread(){
            public void run(){
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    InputStream is = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    Message msg = new Message();
                    msg.obj = bitmap;
                    msg.what = SUCCESS;
                    if(_which.equals("image"))
                        msg.arg1=0;
                    else
                        msg.arg1=1;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    Message msg = new Message();
                    msg.what = FAILURE;
                    msg.obj = e.getMessage();
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
}
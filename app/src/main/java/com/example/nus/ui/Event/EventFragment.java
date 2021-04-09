package com.example.nus.ui.Event;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.nus.Add_Event;
import com.example.nus.MainActivity_Navigation;
import com.example.nus.R;
import com.example.nus.Show_Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;

public class EventFragment extends Fragment {
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private ListView mListView;
    public static final String information = "";
    MainActivity_Navigation main = (MainActivity_Navigation)getActivity();
    private Hashtable<String, Integer> event_id = new Hashtable<String, Integer>();
    private int user_id = 1;
    private Hashtable <String, String> details = new Hashtable<String, String>();
    private Hashtable <String, String> time = new Hashtable<String, String>();
    private ArrayList<String> abstracts = new ArrayList<String>();
    View v;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_events, container, false);
        v = root;
        getinformation();
        Button button = (Button) root.findViewById(R.id.add_event);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mIntent = new Intent(getActivity(), Add_Event.class);
                startActivity(mIntent);
            }
        });
        mListView = (ListView) v.findViewById(R.id.events);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String info = (String)((TextView) view).getText();
                Intent intent = new Intent(getActivity(), Show_Event.class);
                intent.putExtra(information, new String[] {info, details.get(info), time.get(info), Objects.requireNonNull(event_id.get(info)).toString()});
                startActivity(intent);
            }
        });
        return root;
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONAnalysis(msg.obj.toString());
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Decode failed", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    }
//                    Toast.makeText(MainActivity_Navigation.this, "Data download succeed", Toast.LENGTH_SHORT)
//                            .show();
                    break;

                case FAILURE:
                    Toast.makeText(getActivity(), msg.obj.toString() , Toast.LENGTH_SHORT)
                            .show();
                    break;

                default:
                    break;
            }
        };
    };

    protected void JSONAnalysis(String string) throws JSONException, Exception {
        JSONArray array = null;
        array = new JSONArray(string);
        for(int i=0;i<array.length();i++){
            JSONObject ObjectInfo = array.getJSONObject(i);
            int id = ObjectInfo.optInt("id");
            String abstract_ = ObjectInfo.optString("abstracts");
            String detail = ObjectInfo.optString("details");
            String time_ = ObjectInfo.optString("time");
            abstracts.add(abstract_);
            details.put(abstract_,detail);
            time.put(abstract_,time_);
            event_id.put(abstract_,id);
        }
        mListView = (ListView) v.findViewById(R.id.events);
        mListView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, abstracts));

    }



    protected void getinformation(){
        String path = "http://ec2-18-232-62-169.compute-1.amazonaws.com:8004/user/1/events";
        JSONObject json;
        new Thread(){
            public void run(){
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setConnectTimeout(5000);
                    InputStream is = conn.getInputStream();
                    String result = readMyInputStream(is);
                    Message msg = new Message();
                    msg.obj = result;
                    msg.what = SUCCESS;
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

    public static String readMyInputStream(InputStream is) {
        byte[] result;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            is.close();
            baos.close();
            result = baos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            return "Data download failed";
        }
        return new String(result);
    }

    @Override
    public void onResume() {
        super.onResume();
        onCreate(null);
    }
}
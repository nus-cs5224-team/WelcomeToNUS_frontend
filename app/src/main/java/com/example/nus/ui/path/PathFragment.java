package com.example.nus.ui.path;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.nus.MainActivity_Navigation;
import com.example.nus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PathFragment extends Fragment {
    private EditText from;
    private EditText to;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private View v;
//    private MainActivity_Navigation main = (MainActivity_Navigation) getActivity();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_path, container, false);
        v = root;
        from = (EditText) root.findViewById(R.id.from_input);
        to = (EditText) root.findViewById(R.id.to_input);
        Button button = (Button) root.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String _from = from.getText().toString().trim();
                String _to = to.getText().toString().trim();
                if(TextUtils.isEmpty(_from) || TextUtils.isEmpty(_to)){
                    Toast.makeText(getActivity(), "Empty input!", Toast.LENGTH_SHORT).show();
                }
                else{
                    MainActivity_Navigation main = (MainActivity_Navigation)getActivity();
                    if(main.location_dic.containsKey(_from) && main.location_dic.containsKey(_to)){
                        getinformation("http://ec2-18-232-62-169.compute-1.amazonaws.com/api/v1/navigation?from="+main.id_dic.get(_from)+"&to="+main.id_dic.get(_to));
                    }
                    else{
                        Toast.makeText(getActivity(), "No such destination!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return root;
    }

//    public void query_path(View view){
//        String _from = from.getText().toString().trim();
//        String _to = to.getText().toString().trim();
//        if(TextUtils.isEmpty(_from) || TextUtils.isEmpty(_to)){
//            Toast.makeText(getActivity(), "Empty input!", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            if(main.location_dic.contains(_from) && main.location_dic.contains(_to)){
//                getinformation("http://ec2-18-232-62-169.compute-1.amazonaws.com/api/v1/navigation?from="+main.id_dic.get(_from)+"&to="+main.id_dic.get(_to));
//            }
//            else{
//                Toast.makeText(getActivity(), "No such destination!", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

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

    protected void getinformation(String url){
        String path = url;
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

    protected void JSONAnalysis(String string) throws JSONException, Exception {
        JSONArray array = null;
        array = new JSONArray(string);
//        StringBuilder show = new StringBuilder(String.format("%10s", "Bus") + String.format("%25s", "From") + String.format("%25s", "To") + String.format("%10s", "priority") + "\n");
        TableLayout table = (TableLayout) v.findViewById(R.id.table);
        int count = table.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = table.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
        table.setStretchAllColumns(true);
        TableRow row = new TableRow(getActivity());
        TextView tv1 = new TextView(getActivity());
        TextView tv2 = new TextView(getActivity());
        TextView tv3 = new TextView(getActivity());
        TextView tv4 = new TextView(getActivity());
        tv1.setText("Bus");
        tv2.setText("From");
        tv3.setText("To");
        tv4.setText("Priority");
        row.addView(tv1);
        row.addView(tv2);
        row.addView(tv3);
        row.addView(tv4);
        table.addView(row, new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.FILL_PARENT));
        for(int i=0;i<array.length();i++){
            JSONObject ObjectInfo = array.getJSONObject(i);
            String busName = ObjectInfo.optString("busName");
            String fromStopName = ObjectInfo.optString("fromStopName");
            String toStopName = ObjectInfo.optString("toStopName");
            String priority = ObjectInfo.optString("priority");
            String interval = ObjectInfo.optString("interval");
            row = new TableRow(getActivity());
            tv1 = new TextView(getActivity());
            tv2 = new TextView(getActivity());
            tv3 = new TextView(getActivity());
            tv4 = new TextView(getActivity());
            tv1.setText(busName);
            tv2.setText(fromStopName);
            tv3.setText(toStopName);
            tv4.setText(priority);
            row.addView(tv1);
            row.addView(tv2);
            row.addView(tv3);
            row.addView(tv4);
            table.addView(row, new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.FILL_PARENT));
        }
    }
}
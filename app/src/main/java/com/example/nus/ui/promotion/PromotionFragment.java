package com.example.nus.ui.promotion;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.nus.R;
import com.example.nus.ui.promotion.classUtil.ConcreteBuildingForAdapter;
import com.example.nus.ui.promotion.classUtil.ConcreteEventForAdapter;

import org.w3c.dom.Text;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PromotionFragment extends Fragment {

//    private PromotionViewModel promotionsViewModel;
    private static final String PROMOTION_ADDRESS = "http://ec2-18-232-62-169.compute-1.amazonaws.com/api/v1/promotion/";
    PromotionRestUtil promotionRestUtil;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        promotionsViewModel =
//                new ViewModelProvider(this).get(PromotionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_promotion, container, false);
//        final TextView textView = root.findViewById(R.id.text_promotion);
//        promotionsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        // generate the sample events and buildings
//        ArrayList<ConcreteEventForAdapter> sampleList = generateSampleEvents();

        // get the list
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        promotionRestUtil = new PromotionRestUtil();
        List<ConcreteEventForAdapter> sampleList = null;
        try {
            sampleList = promotionRestUtil.getPromotions(PROMOTION_ADDRESS);
        } catch (Exception e) {
            e.printStackTrace();
            sampleList = generateSampleEvents();
        }

        // find the view
        ListView listView = (ListView) root.findViewById(R.id.promotion_list_view);
        // set the adapter
        PromotionArrayAdapter promotionAdapter = new PromotionArrayAdapter(getContext(), sampleList);
        listView.setAdapter(promotionAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // inflate the layout of the popup window
                View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_window, null);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                final TextView popTextView = (TextView) popupView.findViewById(R.id.pop_text);
                final ConcreteEventForAdapter currentEvent = (ConcreteEventForAdapter) parent.getAdapter().getItem(position);
                popTextView.setText(currentEvent.getContent());

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    private HashMap<String, ConcreteBuildingForAdapter> generateSampleBuildings(){
        HashMap<String, ConcreteBuildingForAdapter> sampleMap = new HashMap<>();

        sampleMap.put("Utown", new ConcreteBuildingForAdapter(1, "Utown", ConcreteBuildingForAdapter.GENERAL_BUILDING, 0.0, 0.0));
        sampleMap.put("CentralLib", new ConcreteBuildingForAdapter(2, "Central Library", ConcreteBuildingForAdapter.LIBRARY_BUILDING, 0.0, 0.0));
        sampleMap.put("LT37", new ConcreteBuildingForAdapter(3, "LT37", ConcreteBuildingForAdapter.ACADEMIC_BUILDING, 0.0, 0.0));
        sampleMap.put("Deck", new ConcreteBuildingForAdapter(4, "The Deck", ConcreteBuildingForAdapter.CANTEEN_BUILDING, 0.0, 0.0));
        sampleMap.put("KTHall", new ConcreteBuildingForAdapter(5, "Kent Ridge Hall", ConcreteBuildingForAdapter.ACTIVITY_BUILDING, 0.0, 0.0));

        return sampleMap;
    }

    private ArrayList<ConcreteEventForAdapter> generateSampleEvents(){
        ArrayList<ConcreteEventForAdapter> sampleEventList = new ArrayList<>();

        HashMap<String, ConcreteBuildingForAdapter> sampleBuildingMap = generateSampleBuildings();

        // public ConcreteEventForAdapter(int id, String name, int buildingId, ConcreteBuildingForAdapter building, String content, Date time, int type)
        sampleEventList.add(new ConcreteEventForAdapter(1, "Sports in Utown", 1, sampleBuildingMap.get("Utown"),
                "Sports activity in Utown", new Date(2021, 4,  15, 16, 0, 0), ConcreteEventForAdapter.TYPE_SPORT));
        sampleEventList.add(new ConcreteEventForAdapter(2, "Canteen Promotion", 1, sampleBuildingMap.get("Deck"),
                "Discount in Canteen", new Date(2021, 4,  15, 16, 0, 0), ConcreteEventForAdapter.TYPE_PROMOTION));
        sampleEventList.add(new ConcreteEventForAdapter(3, "Math Lecture", 1, sampleBuildingMap.get("LT37"),
                "Math Lecture in LT37", new Date(2021, 4,  15, 16, 0, 0), ConcreteEventForAdapter.TYPE_LECTURE));

        return sampleEventList;
    }





}
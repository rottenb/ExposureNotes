package com.brianmk.exposurenotes;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private volatile List<FrameData> frameDataList;
    private FrameArrayAdapter frameArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a list of blank exposure information
        frameDataList = new LinkedList<>();
        int exposureCount = 8; // 120 6x9
        for (int i = 0; i < exposureCount; i++) {
            frameDataList.add(new FrameData());
        }

        // Test data
/*        frameDataList.add(new FrameData(1, 3, "dogs"));
        frameDataList.add(new FrameData(2, 2, "poop"));
        frameDataList.add(new FrameData(3, 6, "plane"));
        frameDataList.add(new FrameData(5, 4, "lake"));
        frameDataList.add(new FrameData(6, 5, "mountains"));
        frameDataList.add(new FrameData(3, 4, "Davey"));
        frameDataList.add(new FrameData(2, 1, "Missy"));
        frameDataList.add(new FrameData(1, 4, "more dogs"));
*/
        // Create an adapter with the list data, attach that to the list view
        frameArrayAdapter = new FrameArrayAdapter(this, frameDataList);
        ListView frameListView = findViewById(R.id.frame_list);
        frameListView.setAdapter(frameArrayAdapter);

        // Allow list touching
        frameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                DialogFragment frameDialog = new FrameInfoDialog();
                Bundle args = new Bundle();
                args.putInt("position", pos);
                args.putInt("shutter", frameDataList.get(pos).getShutterIdx());
                args.putInt("aperture", frameDataList.get(pos).getApertureIdx());
                args.putString("notes", frameDataList.get(pos).getNotes());
                frameDialog.setArguments(args);
                frameDialog.show(getFragmentManager(), null);
            }
        });
    }


    public void setSingleFrameData(int pos, int t, int a, String n) {
        frameDataList.get(pos).setShutter(t);
        frameDataList.get(pos).setAperture(a);
        frameDataList.get(pos).setNotes(n);

        frameArrayAdapter.notifyDataSetChanged();
    }

}

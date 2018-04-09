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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<FrameData> frameDataList;
        FrameArrayAdapter frameArrayAdapter;

        // Create a list of blank exposure information
        frameDataList = new LinkedList<>();
 /*       int exposureCount = 8; // 120 6x9
        for (int i = 0; i < exposureCount; i++) {
            frameDataList.add(new FrameData());
        }
*/
      // Dummy data
        frameDataList.add(new FrameData("1/125", "f/5.6", "pipeline"));
        frameDataList.add(new FrameData("1/250", "f/4.0", "dog"));
        frameDataList.add(new FrameData("1/60", "f/8.0", "flower"));
        frameDataList.add(new FrameData("1/60", "f/8.0", "bridge"));
        frameDataList.add(new FrameData("1/500", "f/3.5", "trees"));
        frameDataList.add(new FrameData("1/250", "f/8.0", "boat"));
        frameDataList.add(new FrameData("90s", "f/16", "mountains"));
        frameDataList.add(new FrameData("1/500", "f/4.0", "Davey"));

        // Create an adapter with the list data, attach that to the list view
        frameArrayAdapter = new FrameArrayAdapter(this, frameDataList);
        ListView frameListView = findViewById(R.id.frame_list);
        frameListView.setAdapter(frameArrayAdapter);


        // Allow list touching
        frameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                DialogFragment newFrame = new FrameInfoDialog();
                newFrame.show(getFragmentManager(), null);
            }
        });
    }
}

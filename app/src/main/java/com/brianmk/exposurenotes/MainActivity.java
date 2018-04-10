package com.brianmk.exposurenotes;

import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private volatile List<FrameData> frameDataList;
    private FrameArrayAdapter frameArrayAdapter;

    private int frameCount = 8; // 120 6x9


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check that we have permission to read/write on the filesystem
        verifyStoragePermissions(this);

        // Create a list of blank exposure information
        frameDataList = new LinkedList<>();
//        for (int i = 0; i < frameCount; i++) {
//            frameDataList.add(new FrameData());
//        }

        // Test data
        frameDataList.add(new FrameData(1, 3, "dogs"));
        frameDataList.add(new FrameData(2, 2, "poop"));
        frameDataList.add(new FrameData(3, 6, "plane"));
        frameDataList.add(new FrameData(5, 4, "lake"));
        frameDataList.add(new FrameData(6, 5, "mountains"));
        frameDataList.add(new FrameData(3, 4, "Davey"));
        frameDataList.add(new FrameData(2, 1, "Missy"));
        frameDataList.add(new FrameData(1, 4, "more dogs"));

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

        // Export button
        Button exportButton = findViewById(R.id.export_button);
        exportButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                OutputJSON outJ = new OutputJSON();
                JSONObject obj = outJ.createJSONobj("Fuji GW690ii", "EBC Fujinon 90mm f/3.5",
                                                        "HP5+", 400, 0,
                                                        frameCount, frameDataList);
                try {
                    File file = new File("/storage/emulated/0/ExposureNotes/frame_info.json");
                    Writer output = new BufferedWriter(new FileWriter(file));
                    output.write(obj.toString());
                    output.close();
                    Toast.makeText(getApplicationContext(), "JSON saved", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Log.d(LOG_TAG, "FILE WRITE!!");
                    e.printStackTrace();
                }
            }
        });
    }

    public void setSingleFrameData(int pos, int t, int a, String n) {
        frameDataList.get(pos).setShutter(t);
        frameDataList.get(pos).setAperture(a);
        frameDataList.get(pos).setNotes(n);

        frameArrayAdapter.notifyDataSetChanged();
    }


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}

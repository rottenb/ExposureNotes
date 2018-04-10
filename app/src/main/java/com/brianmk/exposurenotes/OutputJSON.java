package com.brianmk.exposurenotes;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OutputJSON extends JSONObject {
    private static final String LOG_TAG = OutputJSON.class.getSimpleName();

    public JSONObject createJSONobj(String camera, String lens,
                                    String film, int iso, int dev,
                                    int frames, List<FrameData> frameList) {

        JSONObject obj = new JSONObject();
        JSONArray filmArray = new JSONArray();
        for (int i = 0; i < frames; i++) {
            JSONArray frameArray = new JSONArray();

            frameArray.put(frameList.get(i).getShutter());
            frameArray.put(frameList.get(i).getAperture());
            frameArray.put(frameList.get(i).getNotes());

            filmArray.put(frameArray);
        }

        try {
            obj.put("camera", camera);
            obj.put("lens", lens);
            obj.put("film", film);
            obj.put("iso", iso);
            obj.put("dev", dev);
            obj.put("frames", filmArray);
        } catch (JSONException e) {
            Log.d(LOG_TAG, "JSON exception!!");
            e.printStackTrace();
        }

        return obj;
    }
}

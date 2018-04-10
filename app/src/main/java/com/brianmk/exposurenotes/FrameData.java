package com.brianmk.exposurenotes;

public class FrameData {
    private static final String LOG_TAG = FrameData.class.getSimpleName();


    private String[] shutters = {    "---",
                                    "1/500s",
                                    "1/250s",
                                    "1/125s",
                                    "1/60s",
                                    "1/30s",
                                    "1/15s",
                                    "1/8s",
                                    "1/4s",
                                    "1/2s",
                                    "1s",
                                    "T",
                                    "B" };

    private String[] apertures = {   "---",
                                    "f/3.5",
                                    "f/4.0",
                                    "f/4.8",
                                    "f/5.6",
                                    "f/6.7",
                                    "f/8.0",
                                    "f/9.5",
                                    "f/11.0",
                                    "f/13.0",
                                    "f/16.0",
                                    "f/19.0",
                                    "f/22.0",
                                    "f/27.0",
                                    "f/32.0" };

    private int shutterIdx;
    private int apertureIdx;
    private String  notes;

    FrameData() {
        this.shutterIdx = 0;
        this.apertureIdx = 0;
        this.notes = "";
    }

    FrameData(int t, int a, String n) {
        this.shutterIdx = t;
        this.apertureIdx = a;
        this.notes = n;
    }
    public void setShutter(int idx) { this.shutterIdx = idx; }
    public String getShutter() { return shutters[this.shutterIdx]; }
    public int getShutterIdx() { return this.shutterIdx; }

    public void setAperture(int idx) { this.apertureIdx = idx; }
    public String getAperture() { return apertures[this.apertureIdx]; }
    public int getApertureIdx() { return this.apertureIdx; }

    public void setNotes(String n) { this.notes = n; }
    public String getNotes() {return this.notes; }
}

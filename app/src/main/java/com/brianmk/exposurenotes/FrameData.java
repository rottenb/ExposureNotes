package com.brianmk.exposurenotes;

public class FrameData {
    private static final String LOG_TAG = FrameData.class.getSimpleName();

    private String  shutter;
    private String  aperture;
    private String  notes;

    public FrameData() {
        this.shutter = "";
        this.aperture = "";
        this.notes = "";
    }

    public FrameData(String t, String a, String n) {
        this.shutter = t;
        this.aperture = a;
        this.notes = n;
    }
    public void setShutter(String t) { this.shutter = t; }
    public String getShutter() { return this.shutter; }

    public void setAperture(String a) { this.aperture = a; }
    public String getAperture() { return this.aperture; }

    public void setNotes(String n) { this.notes = n; }
    public String getNotes() {return this.notes; }
}

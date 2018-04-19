package com.brianmk.exposurenotes;

class CameraData {
    private static final String LOG_D = CameraData.class.getSimpleName();

    private String camera;
    private String lens;

    public CameraData() {
        this.camera = "Fuji GW690ii";
        this.lens = "EBC Fujinon 90mm f/3.5";
    }

    public CameraData(String c, String l) {
        this.camera = c;
        this.lens = l;
    }

    public void setCamera(String c) { this.camera = c; }
    public String getCamera() { return this.camera; }

    public void setLens(String l) { this.lens = l; }
    public String getLens() { return this.lens; }
}

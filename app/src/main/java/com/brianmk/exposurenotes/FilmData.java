package com.brianmk.exposurenotes;

public class FilmData {
    private static final String LOG_TAG = FilmData.class.getSimpleName();


    private String film;
    private String iso;
    private String dev;

    public FilmData() {
        this.film = "";
        this.iso = "";
        this.dev = "0";
    }


    public FilmData(String c, String l, String f, String i, String d) {
        this.film = f;
        this.iso = i;
        this.dev = d;
    }

    public void setFilm(String f) { this.film = f; }
    public String getFilm() { return this.film; }

    public void setIso(String i) { this.iso = i; }
    public String getIso() { return this.iso; }

    public void setDev(String d) { this.dev = d; }
    public String getDev() { return this.dev; }
}

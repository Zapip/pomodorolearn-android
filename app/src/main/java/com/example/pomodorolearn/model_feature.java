package com.example.pomodorolearn;

public class model_feature {
    private String tv_judul_feature;
    private String tv_description_feature;

    // Constructor
    public model_feature(String tv_judul_feature, String tv_description_feature) {
        this.tv_judul_feature = tv_judul_feature;
        this.tv_description_feature = tv_description_feature;
    }

    // Getter untuk tv_judul_feature
    public String gettv_judul_feature() {
        return tv_judul_feature;
    }

    // Setter untuk tv_judul_feature
    public void settv_judul_feature(String tv_judul_feature) {
        this.tv_judul_feature = tv_judul_feature;
    }

    // Getter untuk tv_description_feature
    public String gettv_description_feature() {
        return tv_description_feature;
    }

    // Setter untuk tv_description_feature
    public void settv_description_feature(String tv_description_feature) {
        this.tv_description_feature = tv_description_feature;
    }

    // toString untuk debugging
    @Override
    public String toString() {
        return "model_feature{" +
                "tv_judul_feature='" + tv_judul_feature + '\'' +
                ", tv_description_feature='" + tv_description_feature + '\'' +
                '}';
    }
}

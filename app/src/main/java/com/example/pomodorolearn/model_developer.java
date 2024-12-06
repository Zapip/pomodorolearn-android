package com.example.pomodorolearn;

public class model_developer {
    private String developers_name;
    private String role_dev;
    private String qoutes_dev;
    private int developers_photo;


    public model_developer(String developers_name, String role_dev, String qoutes_dev, int developers_photo) {
        this.developers_name = developers_name;
        this.role_dev = role_dev;
        this.qoutes_dev = qoutes_dev;
        this.developers_photo = developers_photo;

    }

    //setter
    public void setdevelopers_name(String developers_name) {
        this.developers_name = developers_name;
    }public void setrole_dev(String role_dev) {
        this.role_dev = role_dev;
    }public void setqoutes_dev(String qoutes_dev) {
        this.qoutes_dev = qoutes_dev;
    }public void setdevelopers_photo(String developers_photo) {
        this.developers_photo = Integer.parseInt(developers_photo);
    }

    //getter
    public String getdevelopers_name() {
        return developers_name;
    }

    public String getrole_dev() {
        return role_dev;
    }

    public String getqoutes_dev() {
        return qoutes_dev;
    }

    public int getdevelopers_photo() {
        return developers_photo;
    }

    @Override
    public String toString() {
        return "model_developer{" +
                "developers_name='" + developers_name + '\'' +
                ", trole_dev='" + role_dev + '\'' +
                ", qoutes_dev='" + qoutes_dev + '\'' +
                ", developers_photo=" + developers_photo +
                '}';
    }

}

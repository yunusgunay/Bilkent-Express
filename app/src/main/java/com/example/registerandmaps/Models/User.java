package com.example.registerandmaps.Models;

public class User {
    private String name;
    private String number;
    private String email;
    private int points ;
    private String info;
    private String uid;

    User(){}

    public User(String name, String number, String email, int points, String info,String uid) {
        this.uid = uid;
        this.name = name;
        this.number = number;
        this.email = email;
        this.points = points;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return  " \n"
                + "name: " + name + "\n"
                + " \n"
                + "phone number: " + "\n"
                + number + "\n"
                + " \n"
                + "email: " + "\n"
                + email + "\n"
                + " \n"
                + "points: " + points + "\n"
                + " \n"
                + "biography: " + "\n"
                + info + "\n"
                + " \n";
    }
}

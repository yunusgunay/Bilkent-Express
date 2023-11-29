package com.example.registerandmaps.Models;

public interface UserCallback {
    void onCallback(User user);
    void onError(Exception e);
}

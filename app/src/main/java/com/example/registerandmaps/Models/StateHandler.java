package com.example.registerandmaps.Models;

public interface StateHandler<T> {
    abstract void stateUpdated(int stateCode,T snapshot);
    abstract void locationUpdated();
}

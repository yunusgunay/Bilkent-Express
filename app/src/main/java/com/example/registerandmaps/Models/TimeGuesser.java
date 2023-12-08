package com.example.registerandmaps.Models;

import android.os.Build;

import androidx.annotation.RequiresApi;

public class TimeGuesser {

    public static final int NUM_BUSES = 16;
    public static final int STOPS = 7;

    public TimeGuesser() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String dmb(int stopIndex) {
        // Initialize bus start times
        int[] busStartTime = new int[NUM_BUSES];
        for (int i = 840, k = 0; k < NUM_BUSES; i += 100, k++) {
            busStartTime[k] = i;
        }

        // Define bus stops and distances
        String[] stops = {"East Campus", "Main Campus", "Nizamiye", "ODTU", "Armada", "Asti", "Bahcelievler"};
        double[] distances = {2.5, 2.6, 5.0, 2.8, 1.0, 4.4};

        // Define bus speed
        double speed = 30.0;

        // Rest of the dmb method logic...
        if (stopIndex < 1 || stopIndex > stops.length) {
            return "Invalid stop index. Please choose a number between 1 and " + stops.length + ".";
        }

        // Get current time
        int hour = java.time.LocalTime.now().getHour();
        int minute = java.time.LocalTime.now().getMinute();
        int currentTime = hour * 100 + minute;

        String stopDemanded = stops[stopIndex - 1];

        // Calculate arrival time and next bus
        double arrivalTime = arrivalToStation(speed, distances, stops, stopDemanded);
        int nextBus = findNextBus(busStartTime, currentTime, NUM_BUSES, (int) arrivalTime);
        if (nextBus != -1) {
            return createTimeDifferenceMessage(currentTime, nextBus);
        } else {
            return "There are no more buses for today.";
        }
    }


    private String createTimeDifferenceMessage(int currentTime, int nextBus) {
        int currentHour = currentTime / 100;
        int currentMinute = currentTime % 100;
        int busHour = nextBus / 100;
        int busMinute = nextBus % 100;

        int diffMinute = (busHour * 60 - currentHour * 60) + (busMinute - currentMinute);
        int restHour = diffMinute / 60;
        int restMin = diffMinute % 60;
        if (restHour == 0) {
            return "The bus will arrive in " + restMin + " minutes.";
        } else if (restHour == 1) {
            return "The bus will arrive in " + restHour + " hour and " + restMin + " minutes.";
        } else {
            return "The bus will arrive in " + restHour + " hours and " + restMin + " minutes.";
        }
    }
    private int findNextBus(int[] schedule, int currentTime, int numBuses, int arrivalTime) {
        for (int i = 0; i < numBuses; i++) {
            if (schedule[i] > currentTime) {
                if (i > 0 && schedule[i - 1] + arrivalTime > currentTime) {
                    return schedule[i - 1];
                } else {
                    return schedule[i];
                }
            }
        }
        return -1;
    }

    private double arrivalToStation(double speed, double[] distances, String[] stops, String stopDemanded) {
        double totalTime = 0;

        for (int i = 0; i < STOPS; ++i) {
            if (i > 0) {
                totalTime += distances[i - 1] / speed * 60;
                totalTime += 1;
            }
            if (stops[i].equals(stopDemanded)) {
                return totalTime;
            }
        }
        return 0;
    }

}

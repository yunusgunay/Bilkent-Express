
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

        // Rest of the dmb method logic...
        if (stopIndex < 1 || stopIndex > stops.length) {
            return "Invalid stop index. Please choose a number between 1 and " + stops.length + ".";
        }

        // Get current time
        int hour = java.time.LocalTime.now().getHour();
        int minute = java.time.LocalTime.now().getMinute();
        int currentTime = hour * 60 + minute;

        if (stopIndex == 1) {
            if (30-minute>=0) {
                return "The bus will arrive in " + (30-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (90-minute) + " minutes.";
            }
        } else if (stopIndex == 2) {
            if (40-minute>=0) {
                return "The bus will arrive in " + (40-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (100-minute) + " minutes.";
            }
        } else if (stopIndex == 3) {
            if (45-minute>=0) {
                return "The bus will arrive in " + (45-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (105-minute) + " minutes.";
            }
        } else if (stopIndex == 4) {
            if (55-minute>=0) {
                return "The bus will arrive in " + (55-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (115-minute) + " minutes.";
            }
        } else if (stopIndex == 5) {
            if (58-minute>=0) {
                return "The bus will arrive in " + (58-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (118-minute) + " minutes.";
            }
        } else if (stopIndex == 6) {
            if (6-minute>=0) {
                return "The bus will arrive in " + (6-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (66-minute) + " minutes.";
            }
        } else if (stopIndex == 7) {
            if (10-minute>=0) {
                return "The bus will arrive in " + (10-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (70-minute) + " minutes.";
            }
        }
        else {
            return "";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String bmd(int stopIndex) {
        // Initialize bus start times
        int[] busStartTime = new int[NUM_BUSES];
        for (int i = 840, k = 0; k < NUM_BUSES; i += 100, k++) {
            busStartTime[k] = i;
        }

        // Define bus stops and distances
        String[] stops = { "Bahcelievler", "Asti",  "Armada", "ODTU", "Nizamiye", "Main Campus", "East Campus"};

        // Rest of the dmb method logic...
        if (stopIndex < 1 || stopIndex > stops.length) {
            return "Invalid stop index. Please choose a number between 1 and " + stops.length + ".  " + stopIndex;
        }

        // Get current time
        int hour = java.time.LocalTime.now().getHour();
        int minute = java.time.LocalTime.now().getMinute();
        int currentTime = hour * 60 + minute;

        if (stopIndex == 1) {
            if (50-minute>=0) {
                return "The bus will arrive in " + (50-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (110-minute) + " minutes.";
            }
        } else if (stopIndex == 2) {
            if (54-minute>=0) {
                return "The bus will arrive in " + (54-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (114-minute) + " minutes.";
            }
        } else if (stopIndex == 3) {
            if (2-minute>=0) {
                return "The bus will arrive in " + (2-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (62-minute) + " minutes.";
            }
        } else if (stopIndex == 4) {
            if (5-minute>=0) {
                return "The bus will arrive in " + (5-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (65-minute) + " minutes.";
            }
        } else if (stopIndex == 5) {
            if (15-minute>=0) {
                return "The bus will arrive in " + (15-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (75-minute) + " minutes.";
            }
        } else if (stopIndex == 6) {
            if (20-minute>=0) {
                return "The bus will arrive in " + (20-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (80-minute) + " minutes.";
            }
        } else if (stopIndex == 7) {
            if (30-minute>=0) {
                return "The bus will arrive in " + (30-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (90-minute) + " minutes.";
            }
        }
        else {
            return "";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String tmd(int stopIndex) {
        // Initialize bus start times
        int[] busStartTime = new int[NUM_BUSES];
        for (int i = 840, k = 0; k < NUM_BUSES; i += 100, k++) {
            busStartTime[k] = i;
        }

        // Define bus stops and distances
        String[] stops = { "Tunus", "Armada", "ODTU", "Nizamiye", "Main Campus", "East Campus"};

        // Rest of the dmb method logic...
        if (stopIndex < 1 || stopIndex > stops.length) {
            return "Invalid stop index. Please choose a number between 1 and " + stops.length + ".  " + stopIndex;
        }

        // Get current time
        int hour = java.time.LocalTime.now().getHour();
        int minute = java.time.LocalTime.now().getMinute();
        int currentTime = hour * 60 + minute;

        if (stopIndex == 1) {
            if (40-minute>=0) {
                return "The bus will arrive in " + (40-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (100-minute) + " minutes.";
            }
        } else if (stopIndex == 2) {
            if (54-minute>=0) {
                return "The bus will arrive in " + (54-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (114-minute) + " minutes.";
            }
        } else if (stopIndex == 3) {
            if (2-minute>=0) {
                return "The bus will arrive in " + (2-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (62-minute) + " minutes.";
            }
        } else if (stopIndex == 4) {
            if (5-minute>=0) {
                return "The bus will arrive in " + (5-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (65-minute) + " minutes.";
            }
        } else if (stopIndex == 5) {
            if (15-minute>=0) {
                return "The bus will arrive in " + (15-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (75-minute) + " minutes.";
            }
        } else if (stopIndex == 6) {
            if (20-minute>=0) {
                return "The bus will arrive in " + (20-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (80-minute) + " minutes.";
            }
        }
        else {
            return "";
        }
    }

    public String dmt(int stopIndex) {
        // Initialize bus start times
        int[] busStartTime = new int[NUM_BUSES];
        for (int i = 840, k = 0; k < NUM_BUSES; i += 100, k++) {
            busStartTime[k] = i;
        }

        // Define bus stops and distances
        String[] stops = { "East Campus", "Main Campus", "Nizamiye",  "ODTU", "Armada", "Tunus"};

        // Rest of the dmb method logic...
        if (stopIndex < 1 || stopIndex > stops.length) {
            return "Invalid stop index. Please choose a number between 1 and " + stops.length + ".  " + stopIndex;
        }

        // Get current time
        int hour = java.time.LocalTime.now().getHour();
        int minute = java.time.LocalTime.now().getMinute();
        int currentTime = hour * 60 + minute;

        if (stopIndex == 1) {
            if (30-minute>=0) {
                return "The bus will arrive in " + (30-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (90-minute) + " minutes.";
            }
        } else if (stopIndex == 2) {
            if (40-minute>=0) {
                return "The bus will arrive in " + (40-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (100-minute) + " minutes.";
            }
        } else if (stopIndex == 3) {
            if (45-minute>=0) {
                return "The bus will arrive in " + (45-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (105-minute) + " minutes.";
            }
        } else if (stopIndex == 4) {
            if (55-minute>=0) {
                return "The bus will arrive in " + (55-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (115-minute) + " minutes.";
            }
        } else if (stopIndex == 5) {
            if (58-minute>=0) {
                return "The bus will arrive in " + (58-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (118-minute) + " minutes.";
            }
        } else if (stopIndex == 6) {
            if (16-minute>=0) {
                return "The bus will arrive in " + (16-minute) + " minutes.";
            } else {
                return "The bus will arrive in " + (76-minute) + " minutes.";
            }
        }
        else {
            return "";
        }
    }


}

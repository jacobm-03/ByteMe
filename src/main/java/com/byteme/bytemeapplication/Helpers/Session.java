package com.byteme.bytemeapplication.Helpers;

import com.byteme.bytemeapplication.Models.User;

public class Session {
    private static User currentUser;
    private static String title;
    private static String year;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void clear(){currentUser = null;}

    public static void setTitle(String t) {
        title = t;
    }

    public static String getTitle() {
        return title;
    }

    public static void setYear(String y) {
        year = y;
    }

    public static String getYear() {
        return year;
    }
}
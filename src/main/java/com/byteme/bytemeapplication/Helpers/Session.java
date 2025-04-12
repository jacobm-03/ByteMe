package com.byteme.bytemeapplication.Helpers;

import com.byteme.bytemeapplication.Models.User;

public class Session {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
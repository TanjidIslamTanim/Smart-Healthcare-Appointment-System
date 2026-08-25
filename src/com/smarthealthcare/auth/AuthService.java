package com.smarthealthcare.auth;

import com.smarthealthcare.factory.IUser;
import com.smarthealthcare.model.User;

import java.util.HashMap;
import java.util.Map;

// ============================================================
// SERVICE: AuthService
//
// Manages authentication for all user types.
// Validates credentials and tracks the active session.
// Referenced in the document's "next steps" section as the
// layer that decides WHETHER a user is allowed in, while
// the Factory Pattern decides WHICH object is created.
// ============================================================
public class AuthService {

    // Simple in-memory user store: userId → IUser
    private final Map<String, IUser> userStore = new HashMap<>();

    // Currently logged-in user (one session for this demo)
    private IUser currentUser = null;

    /** Register a user into the auth store. */
    public void register(IUser user) {
        userStore.put(user.getUserId(), user);
        System.out.println("[AUTH] Registered: " + user.getRole()
                           + " '" + user.getName() + "' (ID: " + user.getUserId() + ")");
    }

    /**
     * Validate email + password and log the user in.
     * Returns the logged-in IUser, or null on failure.
     */
    public IUser login(String email, String password) {
        for (IUser u : userStore.values()) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                User userImpl = (User) u;
                if (userImpl.checkPassword(password)) {
                    currentUser = u;
                    String msg = u.login();   // triggers role-specific portal redirect
                    System.out.println("[AUTH] " + msg);
                    return u;
                } else {
                    System.out.println("[AUTH] Login failed: incorrect password for " + email);
                    return null;
                }
            }
        }
        System.out.println("[AUTH] Login failed: no account found for " + email);
        return null;
    }

    /** Log out the current user. */
    public void logout() {
        if (currentUser != null) {
            User userImpl = (User) currentUser;
            System.out.println("[AUTH] " + userImpl.logout());
            currentUser = null;
        }
    }

    public IUser getCurrentUser()              { return currentUser; }
    public boolean isLoggedIn()                { return currentUser != null; }
    public IUser findById(String userId)       { return userStore.get(userId); }
    public Map<String, IUser> getAllUsers()    { return new HashMap<>(userStore); }
}

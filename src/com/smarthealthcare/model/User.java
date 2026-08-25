package com.smarthealthcare.model;

import com.smarthealthcare.factory.IUser;
import com.smarthealthcare.observer.NotificationObserver;

// ============================================================
// BASE CLASS: User (Abstract)
//
// Implements IUser and holds shared attributes common to all
// user types (userID, name, email, password).
//
// Also implements NotificationObserver so that every user can
// receive notifications — used by the Observer Pattern.
// ============================================================
public abstract class User implements IUser, NotificationObserver {

    // ── Shared attributes from the class diagram ──────────────
    protected final String userId;
    protected final String name;
    protected final String email;
    private String password;        // stored hashed in production
    private boolean loggedIn;

    protected User(String userId, String name, String email, String password) {
        this.userId   = userId;
        this.name     = name;
        this.email    = email;
        this.password = password;
        this.loggedIn = false;
    }

    // ── IUser implementation ──────────────────────────────────
    @Override public String getUserId() { return userId; }
    @Override public String getName()   { return name;   }
    @Override public String getEmail()  { return email;  }

    /** Shared logout — same for all roles. */
    @Override
    public String logout() {
        loggedIn = false;
        return name + " has logged out.";
    }

    public boolean isLoggedIn() { return loggedIn; }

    /** Called internally after credential validation. */
    public void setLoggedIn(boolean loggedIn) { this.loggedIn = loggedIn; }

    /**
     * Password check used by AuthService.
     * In production this would compare a bcrypt hash.
     */
    public boolean checkPassword(String input) {
        return password.equals(input);
    }

    // ── NotificationObserver default implementation ───────────
    // Concrete subclasses can override this to customise how
    // notifications are displayed for each role.
    @Override
    public void update(String message) {
        System.out.println("[NOTIFICATION → " + getRole() + " " + name + "] " + message);
    }

    @Override
    public String toString() {
        return getRole() + "{id='" + userId + "', name='" + name + "', email='" + email + "'}";
    }
}

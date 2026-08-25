package com.smarthealthcare.factory;

// ============================================================
// DESIGN PATTERN: FACTORY PATTERN
// Role: Product Interface
//
// IUser defines the common contract for all user types in the
// system. The Factory Pattern requires a shared interface so
// that client code never needs to reference concrete classes
// (Patient, Doctor, Admin) directly — only IUser and
// UserFactory are visible to the caller.
// ============================================================
public interface IUser {

    /** Returns the unique identifier for this user. */
    String getUserId();

    /** Returns this user's display name. */
    String getName();

    /** Returns this user's email address. */
    String getEmail();

    /** Returns this user's role as a string (PATIENT / DOCTOR / ADMIN). */
    String getRole();

    /**
     * Performs a role-specific login action and returns a
     * confirmation message that includes a redirect destination.
     * Each concrete subclass overrides this with its own portal target.
     */
    String login();

    /** Logs the user out and invalidates the current session. */
    String logout();
}

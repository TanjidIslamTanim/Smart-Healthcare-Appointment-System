package com.smarthealthcare.observer;

// ============================================================
// DESIGN PATTERN: OBSERVER PATTERN
// Role: Subject Interface
//
// Defines the registration and notification contract for any
// object that acts as a Subject.  Appointment implements this.
// ============================================================
public interface AppointmentSubject {

    /** Register an observer to receive future notifications. */
    void addObserver(NotificationObserver observer);

    /** Deregister an observer — they will no longer receive updates. */
    void removeObserver(NotificationObserver observer);

    /**
     * Push a notification message to all currently registered
     * observers.  Called internally whenever appointment status changes.
     */
    void notifyObservers(String message);
}

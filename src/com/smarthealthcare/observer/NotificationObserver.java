package com.smarthealthcare.observer;

// ============================================================
// DESIGN PATTERN: OBSERVER PATTERN
// Role: Observer Interface
//
// NotificationObserver defines the contract for any object
// that wants to receive appointment-related notifications.
//
// In this system, Patient and Doctor both implement this
// interface (via the User base class), so whenever an
// Appointment changes status, both parties are automatically
// notified without the Appointment class needing any direct
// reference to Patient or Doctor.
//
// This achieves loose coupling between the appointment logic
// and the notification mechanism.
// ============================================================
public interface NotificationObserver {

    /**
     * Called by the Subject (Appointment) whenever a
     * state change occurs that observers should know about.
     *
     * @param message  Human-readable description of the event,
     *                 e.g. "Your appointment on 2026-04-20 has been confirmed."
     */
    void update(String message);
}

package com.smarthealthcare.model;

import com.smarthealthcare.observer.AppointmentSubject;
import com.smarthealthcare.observer.NotificationObserver;

import java.util.ArrayList;
import java.util.List;

// ============================================================
// DESIGN PATTERN: OBSERVER PATTERN
// Role: Subject (Concrete)
//
// Appointment is the central business entity AND the Observer
// Pattern Subject.  It maintains a list of NotificationObserver
// objects (Patient + Doctor) and calls notifyObservers() every
// time its status changes.
//
// This decouples appointment state changes from notification
// delivery — Appointment never imports Patient or Doctor.
// ============================================================
public class Appointment implements AppointmentSubject {

    // ── Appointment status values ─────────────────────────────
    public enum Status { PENDING, CONFIRMED, CANCELLED, COMPLETED }

    // ── Core attributes from the class diagram ────────────────
    private final String appointmentId;
    private final String patientId;
    private final String doctorId;
    private final String dateTime;       // e.g. "2026-04-20 10:00"
    private Status status;

    // ── OBSERVER PATTERN: observer registry ──────────────────
    private final List<NotificationObserver> observers = new ArrayList<>();

    public Appointment(String appointmentId, String patientId,
                       String doctorId, String dateTime) {
        this.appointmentId = appointmentId;
        this.patientId     = patientId;
        this.doctorId      = doctorId;
        this.dateTime      = dateTime;
        this.status        = Status.PENDING;
    }

    // ── OBSERVER PATTERN: Subject methods ────────────────────
    /**
     * OBSERVER PATTERN — addObserver()
     * Registers a Patient, Doctor, or any other observer.
     */
    @Override
    public void addObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    /**
     * OBSERVER PATTERN — removeObserver()
     * Deregisters an observer (e.g., if patient cancels notification prefs).
     */
    @Override
    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }

    /**
     * OBSERVER PATTERN — notifyObservers()
     * Pushes a message to every registered observer.
     * Called automatically on every status change.
     */
    @Override
    public void notifyObservers(String message) {
        for (NotificationObserver observer : observers) {
            observer.update(message);      // polymorphic call — each role handles differently
        }
    }

    // ── Status change methods (trigger notifications) ─────────
    /**
     * Confirms the appointment and notifies all observers.
     */
    public void confirm() {
        this.status = Status.CONFIRMED;
        notifyObservers("Appointment " + appointmentId
                        + " on " + dateTime + " has been CONFIRMED.");
    }

    /**
     * Cancels the appointment and notifies all observers.
     */
    public void cancel() {
        this.status = Status.CANCELLED;
        notifyObservers("Appointment " + appointmentId
                        + " on " + dateTime + " has been CANCELLED.");
    }

    /**
     * Marks the appointment as completed and notifies all observers.
     */
    public void complete() {
        this.status = Status.COMPLETED;
        notifyObservers("Appointment " + appointmentId
                        + " on " + dateTime + " is now COMPLETED.");
    }

    // ── Getters ───────────────────────────────────────────────
    public String getAppointmentId() { return appointmentId; }
    public String getPatientId()     { return patientId;     }
    public String getDoctorId()      { return doctorId;      }
    public String getDateTime()      { return dateTime;      }
    public Status getStatus()        { return status;        }

    @Override
    public String toString() {
        return "Appointment{id='" + appointmentId + "', patient='" + patientId
               + "', doctor='" + doctorId + "', time='" + dateTime
               + "', status=" + status + '}';
    }
}

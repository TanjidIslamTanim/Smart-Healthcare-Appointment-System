package com.smarthealthcare.model;

import java.util.ArrayList;
import java.util.List;

// ============================================================
// DESIGN PATTERN: FACTORY PATTERN
// Role: Concrete Product
//
// Patient is one of three concrete user types created by
// UserFactory.  Client code never calls "new Patient()" —
// it always calls UserFactory.createUser("patient", ...).
// ============================================================
public class Patient extends User {

    // Patient-specific attributes
    private final String patientId;
    private final List<String> appointmentIds;  // tracks booked appointment IDs

    public Patient(String userId, String name, String email, String password) {
        super(userId, name, email, password);
        this.patientId      = userId;
        this.appointmentIds = new ArrayList<>();
    }

    // ── IUser contract ────────────────────────────────────────
    @Override
    public String getRole() { return "PATIENT"; }

    /**
     * FACTORY PATTERN — Concrete Product login()
     * Each concrete product provides its own portal redirect.
     */
    @Override
    public String login() {
        setLoggedIn(true);
        return "Patient " + name + " logged in. Redirecting to appointment portal.";
    }

    // ── Patient-specific behaviour ────────────────────────────
    public void addAppointmentId(String appointmentId) {
        appointmentIds.add(appointmentId);
    }

    public void removeAppointmentId(String appointmentId) {
        appointmentIds.remove(appointmentId);
    }

    public List<String> getAppointmentIds() {
        return new ArrayList<>(appointmentIds);
    }

    public String getPatientId() { return patientId; }

    /** Patient receives a notification with patient-specific formatting. */
    @Override
    public void update(String message) {
        System.out.println("[PATIENT NOTIFICATION → " + name + "] " + message);
    }
}

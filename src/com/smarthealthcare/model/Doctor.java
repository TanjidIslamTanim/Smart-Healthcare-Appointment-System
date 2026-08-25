package com.smarthealthcare.model;

import java.util.ArrayList;
import java.util.List;

// ============================================================
// DESIGN PATTERN: FACTORY PATTERN
// Role: Concrete Product
//
// Doctor is the second concrete user type created by
// UserFactory.  It extends User and adds schedule management.
// ============================================================
public class Doctor extends User {

    private final String doctorId;
    private String specialisation;
    private final List<String> availableSlots;   // e.g. "2026-04-20 10:00"

    public Doctor(String userId, String name, String email, String password, String specialisation) {
        super(userId, name, email, password);
        this.doctorId        = userId;
        this.specialisation  = specialisation;
        this.availableSlots  = new ArrayList<>();
    }

    // Convenience constructor without specialisation
    public Doctor(String userId, String name, String email, String password) {
        this(userId, name, email, password, "General Practice");
    }

    // ── IUser contract ────────────────────────────────────────
    @Override
    public String getRole() { return "DOCTOR"; }

    /**
     * FACTORY PATTERN — Concrete Product login()
     */
    @Override
    public String login() {
        setLoggedIn(true);
        return "Doctor " + name + " logged in. Redirecting to schedule dashboard.";
    }

    // ── Doctor-specific behaviour ─────────────────────────────
    public void addSlot(String slot) {
        if (!availableSlots.contains(slot)) {
            availableSlots.add(slot);
            System.out.println("[SCHEDULE] Dr. " + name + " added slot: " + slot);
        }
    }

    public boolean removeSlot(String slot) {
        boolean removed = availableSlots.remove(slot);
        if (removed) {
            System.out.println("[SCHEDULE] Dr. " + name + " removed slot: " + slot);
        }
        return removed;
    }

    public List<String> getAvailableSlots() {
        return new ArrayList<>(availableSlots);
    }

    public boolean isSlotAvailable(String slot) {
        return availableSlots.contains(slot);
    }

    public String getDoctorId()      { return doctorId; }
    public String getSpecialisation(){ return specialisation; }
    public void setSpecialisation(String s) { this.specialisation = s; }

    /** Doctor receives a notification with doctor-specific formatting. */
    @Override
    public void update(String message) {
        System.out.println("[DOCTOR NOTIFICATION → Dr. " + name + "] " + message);
    }
}

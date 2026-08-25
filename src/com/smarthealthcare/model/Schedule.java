package com.smarthealthcare.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// ============================================================
// MODEL: Schedule
//
// Manages the available time slots for a single Doctor.
// Separated from Appointment intentionally (Design Decision 2
// in the document) — scheduling logic is independent of
// confirmed booking management.
// ============================================================
public class Schedule {

    private final String scheduleId;
    private final String doctorId;
    private final List<String> availableSlots;  // e.g. "2026-04-20 10:00"

    public Schedule(String scheduleId, String doctorId) {
        this.scheduleId     = scheduleId;
        this.doctorId       = doctorId;
        this.availableSlots = new ArrayList<>();
    }

    /** Add a new available time slot. */
    public boolean addSlot(String slot) {
        if (availableSlots.contains(slot)) {
            System.out.println("[SCHEDULE] Slot already exists: " + slot);
            return false;
        }
        availableSlots.add(slot);
        return true;
    }

    /** Remove a slot (e.g., after it has been booked or doctor removed it). */
    public boolean removeSlot(String slot) {
        return availableSlots.remove(slot);
    }

    /** Returns true if the given slot is still available. */
    public boolean isAvailable(String slot) {
        return availableSlots.contains(slot);
    }

    public List<String> getAvailableSlots() {
        return Collections.unmodifiableList(availableSlots);
    }

    public String getScheduleId() { return scheduleId; }
    public String getDoctorId()   { return doctorId;   }

    @Override
    public String toString() {
        return "Schedule{doctorId='" + doctorId + "', slots=" + availableSlots + '}';
    }
}

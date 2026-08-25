package com.smarthealthcare.service;

import com.smarthealthcare.model.Doctor;
import com.smarthealthcare.model.Schedule;

import java.util.HashMap;
import java.util.Map;

// ============================================================
// SERVICE: ScheduleService
//
// Creates and manages Schedule objects for each Doctor.
// Kept intentionally simple for the university demo.
// ============================================================
public class ScheduleService {

    // doctorId → Schedule
    private final Map<String, Schedule> schedules = new HashMap<>();
    private int idCounter = 1;

    /** Creates a blank schedule for a doctor if one doesn't exist. */
    public Schedule createSchedule(Doctor doctor) {
        if (schedules.containsKey(doctor.getDoctorId())) {
            return schedules.get(doctor.getDoctorId());
        }
        String schedId = "SCH" + String.format("%03d", idCounter++);
        Schedule schedule = new Schedule(schedId, doctor.getDoctorId());
        schedules.put(doctor.getDoctorId(), schedule);
        System.out.println("[SCHEDULE] Created schedule " + schedId
                           + " for Dr. " + doctor.getName());
        return schedule;
    }

    /** Add a time slot to a doctor's schedule. */
    public void addSlot(Doctor doctor, String slot) {
        Schedule s = getOrCreate(doctor);
        if (s.addSlot(slot)) {
            System.out.println("[SCHEDULE] Dr. " + doctor.getName()
                               + " added slot: " + slot);
        }
    }

    /** Remove a time slot from a doctor's schedule. */
    public void removeSlot(Doctor doctor, String slot) {
        Schedule s = getOrCreate(doctor);
        if (s.removeSlot(slot)) {
            System.out.println("[SCHEDULE] Dr. " + doctor.getName()
                               + " removed slot: " + slot);
        }
    }

    /** Print all available slots for a doctor. */
    public void viewSchedule(Doctor doctor) {
        Schedule s = schedules.get(doctor.getDoctorId());
        if (s == null || s.getAvailableSlots().isEmpty()) {
            System.out.println("[SCHEDULE] Dr. " + doctor.getName()
                               + " has no available slots.");
            return;
        }
        System.out.println("[SCHEDULE] Dr. " + doctor.getName() + "'s available slots:");
        for (String slot : s.getAvailableSlots()) {
            System.out.println("   • " + slot);
        }
    }

    public Schedule getSchedule(String doctorId) {
        return schedules.get(doctorId);
    }

    private Schedule getOrCreate(Doctor doctor) {
        if (!schedules.containsKey(doctor.getDoctorId())) {
            return createSchedule(doctor);
        }
        return schedules.get(doctor.getDoctorId());
    }
}

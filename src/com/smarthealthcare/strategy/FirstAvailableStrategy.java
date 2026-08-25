package com.smarthealthcare.strategy;

import com.smarthealthcare.model.Doctor;
import com.smarthealthcare.model.Schedule;

import java.util.List;

// ============================================================
// DESIGN PATTERN: STRATEGY PATTERN
// Role: Concrete Strategy 1 — "First Available"
//
// Selects the earliest slot in the doctor's availability list,
// ignoring any caller-specified preference.  This is the
// default strategy used for standard patient bookings.
// ============================================================
public class FirstAvailableStrategy implements SchedulingStrategy {

    /**
     * STRATEGY PATTERN — selectSlot() implementation
     * Returns the first slot in the doctor's available list.
     */
    @Override
    public String selectSlot(Doctor doctor, Schedule schedule, String requestedSlot) {
        List<String> slots = schedule.getAvailableSlots();

        if (slots.isEmpty()) {
            System.out.println("[STRATEGY: FirstAvailable] No slots available for Dr. "
                               + doctor.getName());
            return null;
        }

        String chosen = slots.get(0);
        System.out.println("[STRATEGY: FirstAvailable] Selected slot: " + chosen
                           + " for Dr. " + doctor.getName());
        return chosen;
    }

    @Override
    public String getStrategyName() {
        return "First Available";
    }
}

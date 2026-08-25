package com.smarthealthcare.strategy;

import com.smarthealthcare.model.Doctor;
import com.smarthealthcare.model.Schedule;

// ============================================================
// DESIGN PATTERN: STRATEGY PATTERN
// Role: Concrete Strategy 2 — "Priority Booking"
//
// Attempts to book the caller's specifically requested slot.
// Falls back to the first available slot only if the requested
// slot is not free, printing a clear log message either way.
//
// Used for urgent / priority appointments.
// ============================================================
public class PriorityBookingStrategy implements SchedulingStrategy {

    /**
     * STRATEGY PATTERN — selectSlot() implementation
     * Honours the requested slot if available; falls back otherwise.
     */
    @Override
    public String selectSlot(Doctor doctor, Schedule schedule, String requestedSlot) {

        if (requestedSlot != null && schedule.isAvailable(requestedSlot)) {
            System.out.println("[STRATEGY: PriorityBooking] Secured requested slot: "
                               + requestedSlot + " [URGENT] with Dr. " + doctor.getName());
            return requestedSlot;
        }

        // Requested slot unavailable — fall back to first available
        if (!schedule.getAvailableSlots().isEmpty()) {
            String fallback = schedule.getAvailableSlots().get(0);
            System.out.println("[STRATEGY: PriorityBooking] Requested slot '"
                               + requestedSlot + "' unavailable. "
                               + "Falling back to: " + fallback);
            return fallback;
        }

        System.out.println("[STRATEGY: PriorityBooking] No slots available for Dr. "
                           + doctor.getName());
        return null;
    }

    @Override
    public String getStrategyName() {
        return "Priority Booking";
    }
}

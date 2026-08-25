package com.smarthealthcare.strategy;

import com.smarthealthcare.model.Doctor;
import com.smarthealthcare.model.Schedule;

// ============================================================
// DESIGN PATTERN: STRATEGY PATTERN
// Role: Strategy Interface
//
// SchedulingStrategy defines a family of interchangeable
// scheduling algorithms.  The AppointmentScheduler holds
// a reference to this interface and delegates all booking
// decisions to it — so the algorithm can be swapped at
// runtime without changing any AppointmentScheduler code.
//
// Currently implemented by:
//   • FirstAvailableStrategy  — books the earliest open slot
//   • PriorityBookingStrategy — books a specific requested slot
// ============================================================
public interface SchedulingStrategy {

    /**
     * STRATEGY PATTERN — algorithm entry point
     *
     * Selects and returns the time slot to use for this booking.
     * Returns null if no suitable slot is available.
     *
     * @param doctor        The doctor whose schedule is being queried
     * @param schedule      The doctor's Schedule object
     * @param requestedSlot A caller-specified preferred slot (may be null
     *                      if the strategy should choose automatically)
     * @return              The chosen slot string, or null if unavailable
     */
    String selectSlot(Doctor doctor, Schedule schedule, String requestedSlot);

    /** Human-readable name of this strategy, shown in logs. */
    String getStrategyName();
}

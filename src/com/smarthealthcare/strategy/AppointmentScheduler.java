package com.smarthealthcare.strategy;

import com.smarthealthcare.model.Doctor;
import com.smarthealthcare.model.Schedule;

// ============================================================
// DESIGN PATTERN: STRATEGY PATTERN
// Role: Context
//
// AppointmentScheduler holds a reference to a SchedulingStrategy
// and delegates all slot-selection logic to it.  The strategy
// can be swapped at runtime (setStrategy) without modifying
// any code inside this class.
//
// Client code calls book() — it never calls selectSlot()
// directly on a strategy, keeping the pattern encapsulated.
// ============================================================
public class AppointmentScheduler {

    // ── STRATEGY PATTERN: held strategy reference ─────────────
    private SchedulingStrategy strategy;

    public AppointmentScheduler(SchedulingStrategy strategy) {
        this.strategy = strategy;
        System.out.println("[SCHEDULER] Initialised with strategy: "
                           + strategy.getStrategyName());
    }

    /**
     * STRATEGY PATTERN — setStrategy()
     * Swap the algorithm at runtime without touching any other code.
     */
    public void setStrategy(SchedulingStrategy strategy) {
        System.out.println("[SCHEDULER] Strategy changed to: "
                           + strategy.getStrategyName());
        this.strategy = strategy;
    }

    /**
     * STRATEGY PATTERN — book()
     * Delegates slot selection entirely to the current strategy.
     * Returns the chosen slot, or null if none available.
     */
    public String book(Doctor doctor, Schedule schedule, String requestedSlot) {
        System.out.println("[SCHEDULER] Booking appointment for Dr. "
                           + doctor.getName()
                           + " using strategy: " + strategy.getStrategyName());
        return strategy.selectSlot(doctor, schedule, requestedSlot);
    }

    public String getCurrentStrategyName() {
        return strategy.getStrategyName();
    }
}

package com.smarthealthcare.main;

import com.smarthealthcare.auth.AuthService;
import com.smarthealthcare.factory.IUser;
import com.smarthealthcare.model.Admin;
import com.smarthealthcare.model.Appointment;
import com.smarthealthcare.model.Doctor;
import com.smarthealthcare.model.Patient;
import com.smarthealthcare.service.AppointmentService;
import com.smarthealthcare.service.ScheduleService;
import com.smarthealthcare.service.UserService;
import com.smarthealthcare.strategy.PriorityBookingStrategy;

// ============================================================
// MAIN ENTRY POINT
//
// Demonstrates all three design patterns in sequence:
//
//   1. FACTORY PATTERN   — UserFactory creates Patient/Doctor/Admin
//   2. OBSERVER PATTERN  — Appointment notifies Patient + Doctor
//   3. STRATEGY PATTERN  — Scheduler swaps booking algorithms
//
// Run this class to see the full system demo in the console.
// ============================================================
public class Main {

    public static void main(String[] args) {

        // ── Setup shared services ─────────────────────────────
        AuthService       authService       = new AuthService();
        ScheduleService   scheduleService   = new ScheduleService();
        UserService       userService       = new UserService(authService);
        AppointmentService appointmentService = new AppointmentService(scheduleService);

        printBanner("SMART HEALTHCARE APPOINTMENT SYSTEM — DEMO");

        // ══════════════════════════════════════════════════════
        // DEMO 1: FACTORY PATTERN
        // UserFactory.createUser() is the only way users are
        // created.  The caller receives an IUser reference and
        // never touches Patient/Doctor/Admin directly.
        // ══════════════════════════════════════════════════════
        printSection("DEMO 1: FACTORY PATTERN — User Creation");

        IUser patientUser = userService.createUser(
                "patient", "Alice Tan", "alice@example.com", "alice123");

        IUser doctorUser  = userService.createUser(
                "doctor", "Dr. Amir", "amir@example.com", "amir123");

        IUser adminUser   = userService.createUser(
                "admin", "Admin Bob", "bob@example.com", "bob123");

        // login() shows role-specific portal redirect from each concrete class
        System.out.println();
        System.out.println("Login messages (from the document's code):");
        System.out.println("  " + patientUser.login());
        System.out.println("  " + doctorUser.login());
        System.out.println("  " + adminUser.login());

        // Cast to concrete types only in the demo layer — services use IUser
        Patient patient = (Patient) patientUser;
        Doctor  doctor  = (Doctor)  doctorUser;
        Admin   admin   = (Admin)   adminUser;

        // ── AuthService login demo ─────────────────────────────
        System.out.println();
        printSection("DEMO 1b: AuthService — Credential Validation");
        authService.login("alice@example.com", "wrong");     // should fail
        authService.login("alice@example.com", "alice123");  // should succeed
        authService.logout();

        // ══════════════════════════════════════════════════════
        // DEMO 2: STRATEGY PATTERN
        // AppointmentScheduler selects a booking slot using the
        // currently set SchedulingStrategy.
        // The strategy is swapped mid-demo to show runtime switching.
        // ══════════════════════════════════════════════════════
        printSection("DEMO 2: STRATEGY PATTERN — Schedule Management");

        // Doctor adds available slots
        scheduleService.addSlot(doctor, "2026-04-20 09:00");
        scheduleService.addSlot(doctor, "2026-04-20 10:00");
        scheduleService.addSlot(doctor, "2026-04-20 11:00");
        scheduleService.viewSchedule(doctor);

        // ══════════════════════════════════════════════════════
        // DEMO 3: OBSERVER + STRATEGY PATTERN together
        // bookAppointment() uses Strategy to pick a slot, then
        // registers Patient + Doctor as Observers on the new
        // Appointment.  Calling confirm() / cancel() automatically
        // notifies both parties.
        // ══════════════════════════════════════════════════════
        printSection("DEMO 3: OBSERVER PATTERN — Book Appointment (FirstAvailable)");

        // Strategy = FirstAvailable (default)
        Appointment appt1 = appointmentService.bookAppointment(
                patient, doctor, null);   // null = let strategy choose

        System.out.println();
        System.out.println("Remaining slots after booking:");
        scheduleService.viewSchedule(doctor);

        // ── Swap strategy at runtime ───────────────────────────
        printSection("DEMO 3b: STRATEGY PATTERN — Swap to PriorityBooking");

        appointmentService.setSchedulingStrategy(new PriorityBookingStrategy());

        // Book a second appointment requesting a specific slot
        IUser patient2User = userService.createUser(
                "patient", "Ben Lim", "ben@example.com", "ben123");
        Patient patient2 = (Patient) patient2User;

        Appointment appt2 = appointmentService.bookAppointment(
                patient2, doctor, "2026-04-20 11:00");  // request specific slot

        // ── Cancellation triggers Observer notifications ───────
        printSection("DEMO 3c: OBSERVER PATTERN — Cancel Appointment");

        if (appt1 != null) {
            appointmentService.cancelAppointment(
                    appt1.getAppointmentId(), patient, scheduleService);
            // cancel() internally calls notifyObservers() → Patient + Doctor both notified
        }

        // ══════════════════════════════════════════════════════
        // DEMO 4: ADMIN OPERATIONS
        // ══════════════════════════════════════════════════════
        printSection("DEMO 4: ADMIN — User Management & Reporting");

        userService.viewAllUsers(admin);
        System.out.println();
        userService.generateReport(admin, "Appointment Summary",
                                   "2026-04-01 to 2026-04-30",
                                   appointmentService);

        // ── Final appointment list ─────────────────────────────
        printSection("FINAL STATE — All Appointments");
        for (Appointment a : appointmentService.getAllAppointments()) {
            System.out.println("  " + a);
        }

        printBanner("END OF DEMO");
    }

    // ── Console formatting helpers ────────────────────────────
    private static void printBanner(String text) {
        String line = "=".repeat(60);
        System.out.println("\n" + line);
        System.out.println("  " + text);
        System.out.println(line + "\n");
    }

    private static void printSection(String text) {
        System.out.println("\n──────────────────────────────────────────────────────");
        System.out.println("  " + text);
        System.out.println("──────────────────────────────────────────────────────");
    }
}

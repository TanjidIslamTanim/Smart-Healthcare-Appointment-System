package com.smarthealthcare.service;

import com.smarthealthcare.model.Appointment;
import com.smarthealthcare.model.Doctor;
import com.smarthealthcare.model.Patient;
import com.smarthealthcare.model.Schedule;
import com.smarthealthcare.strategy.AppointmentScheduler;
import com.smarthealthcare.strategy.FirstAvailableStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ============================================================
// SERVICE: AppointmentService
//
// Handles the full appointment lifecycle:
//   Book → Confirm → Cancel
//
// This is where all three design patterns come together:
//   • FACTORY PATTERN  — Patient and Doctor objects were created
//                        by UserFactory before reaching here
//   • OBSERVER PATTERN — Appointment registers Patient + Doctor
//                        as observers; status changes auto-notify
//   • STRATEGY PATTERN — AppointmentScheduler selects the slot
// ============================================================
public class AppointmentService {

    private final Map<String, Appointment> appointments = new HashMap<>();
    private final ScheduleService scheduleService;

    // STRATEGY PATTERN: default scheduler uses FirstAvailable
    private final AppointmentScheduler scheduler =
            new AppointmentScheduler(new FirstAvailableStrategy());

    private int idCounter = 1;

    public AppointmentService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * Books an appointment for a Patient with a Doctor.
     *
     * Step-by-step pattern involvement:
     *  1. STRATEGY — scheduler.book() picks the slot
     *  2. OBSERVER  — appointment.addObserver(patient/doctor) registers both
     *  3. OBSERVER  — appointment.confirm() triggers notifyObservers()
     */
    public Appointment bookAppointment(Patient patient, Doctor doctor,
                                       String requestedSlot) {
        Schedule schedule = scheduleService.getSchedule(doctor.getDoctorId());
        if (schedule == null) {
            System.out.println("[BOOKING] No schedule found for Dr. " + doctor.getName());
            return null;
        }

        // ── STRATEGY PATTERN: delegate slot selection ──────────
        String chosenSlot = scheduler.book(doctor, schedule, requestedSlot);
        if (chosenSlot == null) {
            System.out.println("[BOOKING] No available slots — booking failed.");
            return null;
        }

        // Create appointment
        String apptId = "APPT" + String.format("%03d", idCounter++);
        Appointment appt = new Appointment(apptId, patient.getUserId(),
                                           doctor.getUserId(), chosenSlot);

        // ── OBSERVER PATTERN: register both parties as observers ─
        appt.addObserver(patient);   // Patient will receive notifications
        appt.addObserver(doctor);    // Doctor will receive notifications

        // Remove slot from schedule so it can't be double-booked
        schedule.removeSlot(chosenSlot);
        patient.addAppointmentId(apptId);

        // ── OBSERVER PATTERN: confirm() → notifyObservers() ──────
        appt.confirm();   // internally calls notifyObservers("...CONFIRMED...")

        appointments.put(apptId, appt);
        System.out.println("[BOOKING] Created: " + appt);
        return appt;
    }

    /**
     * Cancels an existing appointment.
     * OBSERVER PATTERN: cancel() triggers notifyObservers() automatically.
     */
    public boolean cancelAppointment(String appointmentId, Patient patient,
                                     ScheduleService scheduleService) {
        Appointment appt = appointments.get(appointmentId);
        if (appt == null) {
            System.out.println("[CANCEL] Appointment not found: " + appointmentId);
            return false;
        }
        if (appt.getStatus() == Appointment.Status.CANCELLED) {
            System.out.println("[CANCEL] Already cancelled: " + appointmentId);
            return false;
        }

        // ── OBSERVER PATTERN: cancel() → notifyObservers() ───────
        appt.cancel();   // internally calls notifyObservers("...CANCELLED...")
        patient.removeAppointmentId(appointmentId);

        // Free the slot back into the doctor's schedule
        Schedule schedule = scheduleService.getSchedule(appt.getDoctorId());
        if (schedule != null) {
            schedule.addSlot(appt.getDateTime());
        }
        return true;
    }

    /** Swap the scheduling strategy at runtime — demonstrates Strategy Pattern. */
    public void setSchedulingStrategy(
            com.smarthealthcare.strategy.SchedulingStrategy strategy) {
        scheduler.setStrategy(strategy);
    }

    public Appointment getAppointment(String id)   { return appointments.get(id); }
    public List<Appointment> getAllAppointments()   { return new ArrayList<>(appointments.values()); }

    public List<Appointment> getAppointmentsForPatient(String patientId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments.values()) {
            if (a.getPatientId().equals(patientId)) result.add(a);
        }
        return result;
    }

    public List<Appointment> getAppointmentsForDoctor(String doctorId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments.values()) {
            if (a.getDoctorId().equals(doctorId)) result.add(a);
        }
        return result;
    }
}

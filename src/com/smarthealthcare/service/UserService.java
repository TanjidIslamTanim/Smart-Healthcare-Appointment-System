package com.smarthealthcare.service;

import com.smarthealthcare.auth.AuthService;
import com.smarthealthcare.factory.IUser;
import com.smarthealthcare.factory.UserFactory;
import com.smarthealthcare.model.Admin;

import java.util.Map;

// ============================================================
// SERVICE: UserService
//
// Wraps the FACTORY PATTERN for user creation and exposes
// admin-level operations: create account, deactivate user,
// view all users, generate reports.
// ============================================================
public class UserService {

    private final AuthService authService;
    private int userCounter = 1;

    public UserService(AuthService authService) {
        this.authService = authService;
    }

    /**
     * FACTORY PATTERN — createUser()
     * Delegates to UserFactory; registers the result with AuthService.
     */
    public IUser createUser(String role, String name, String email, String password) {
        String userId = role.toUpperCase().charAt(0)
                        + String.format("%03d", userCounter++);

        // ── FACTORY PATTERN: single call creates the right type ──
        IUser user = UserFactory.createUser(role, userId, name, email, password);
        authService.register(user);
        return user;
    }

    /** Admin operation: deactivate a user account. */
    public void deactivateUser(Admin admin, String targetUserId) {
        IUser target = authService.findById(targetUserId);
        if (target == null) {
            System.out.println("[USER MGT] User not found: " + targetUserId);
            return;
        }
        admin.deactivateUser(targetUserId);
        System.out.println("[USER MGT] " + target.getRole()
                           + " '" + target.getName() + "' has been deactivated.");
    }

    /** Admin operation: print all registered users. */
    public void viewAllUsers(Admin admin) {
        Map<String, IUser> users = authService.getAllUsers();
        System.out.println("[USER MGT] All registered users (" + users.size() + "):");
        for (IUser u : users.values()) {
            System.out.println("   • [" + u.getRole() + "] "
                               + u.getName() + " — " + u.getEmail());
        }
    }

    /** Admin operation: generate a simple appointment report. */
    public void generateReport(Admin admin, String reportType, String dateRange,
                               AppointmentService appointmentService) {
        admin.generateReport(reportType, dateRange);
        int total      = appointmentService.getAllAppointments().size();
        long confirmed = appointmentService.getAllAppointments().stream()
                .filter(a -> a.getStatus() == com.smarthealthcare.model.Appointment.Status.CONFIRMED)
                .count();
        long cancelled = appointmentService.getAllAppointments().stream()
                .filter(a -> a.getStatus() == com.smarthealthcare.model.Appointment.Status.CANCELLED)
                .count();
        System.out.println("[REPORT] Total: " + total
                           + " | Confirmed: " + confirmed
                           + " | Cancelled: " + cancelled);
    }
}

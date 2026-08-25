package com.smarthealthcare.model;

// ============================================================
// DESIGN PATTERN: FACTORY PATTERN
// Role: Concrete Product
//
// Admin is the third concrete user type created by
// UserFactory.  It extends User and adds system management
// capabilities: user management and report generation.
// ============================================================
public class Admin extends User {

    private final String adminId;

    public Admin(String userId, String name, String email, String password) {
        super(userId, name, email, password);
        this.adminId = userId;
    }

    // ── IUser contract ────────────────────────────────────────
    @Override
    public String getRole() { return "ADMIN"; }

    /**
     * FACTORY PATTERN — Concrete Product login()
     */
    @Override
    public String login() {
        setLoggedIn(true);
        return "Admin " + name + " logged in. Redirecting to admin panel.";
    }

    // ── Admin-specific behaviour ──────────────────────────────
    public void deactivateUser(String userId) {
        System.out.println("[ADMIN] " + name + " deactivated user: " + userId);
    }

    public void generateReport(String reportType, String dateRange) {
        System.out.println("[ADMIN] " + name + " generating '" + reportType
                           + "' report for range: " + dateRange);
    }

    public void viewAllAppointments() {
        System.out.println("[ADMIN] " + name + " viewing all system appointments.");
    }

    public String getAdminId() { return adminId; }

    /** Admins receive system-level notifications. */
    @Override
    public void update(String message) {
        System.out.println("[SYSTEM ALERT → Admin " + name + "] " + message);
    }
}

package com.smarthealthcare.factory;

import com.smarthealthcare.model.Admin;
import com.smarthealthcare.model.Doctor;
import com.smarthealthcare.model.Patient;

// ============================================================
// DESIGN PATTERN: FACTORY PATTERN
// Role: Factory / Creator
//
// UserFactory is the single point of user object creation in
// the system.  Client code passes a role string and the
// factory decides which concrete class to instantiate.
//
// Benefits demonstrated here:
//   • Client code is decoupled from Patient, Doctor, Admin
//   • Adding a new role (e.g. "nurse") requires only a new
//     class + one extra case below — nothing else changes
//   • Follows the Open/Closed Principle
//
// Direct usage of "new Patient()" / "new Doctor()" anywhere
// else in the codebase is intentionally avoided.
// ============================================================
public class UserFactory {

    // Prevent instantiation — all methods are static
    private UserFactory() {}

    /**
     * FACTORY PATTERN — createUser()
     *
     * Creates and returns the correct IUser subclass based on
     * the userType string.  The caller only ever receives an
     * IUser reference, never a concrete type.
     *
     * @param userType  "patient", "doctor", or "admin" (case-insensitive)
     * @param userId    Unique identifier for the user
     * @param name      Display name
     * @param email     Email address
     * @param password  Plain-text password (hashed in AuthService before storage)
     * @return          The appropriate IUser implementation
     * @throws IllegalArgumentException for unknown userType values
     */
    public static IUser createUser(String userType,
                                   String userId,
                                   String name,
                                   String email,
                                   String password) {
        switch (userType.toLowerCase()) {
            case "patient":
                return new Patient(userId, name, email, password);

            case "doctor":
                return new Doctor(userId, name, email, password);

            case "admin":
                return new Admin(userId, name, email, password);

            // ── Extend here for future roles ──────────────────
            // case "nurse":
            //     return new Nurse(userId, name, email, password);
            // ─────────────────────────────────────────────────

            default:
                throw new IllegalArgumentException(
                    "Unknown user type: '" + userType + "'. " +
                    "Valid values: patient, doctor, admin."
                );
        }
    }

    /**
     * Convenience overload used in the document's demo code.
     * Generates a simple userId from the name when no explicit ID is provided.
     */
    public static IUser createUser(String userType, String name) {
        String autoId = userType.toUpperCase().charAt(0)
                        + name.replaceAll("\\s+", "").toUpperCase();
        return createUser(userType, autoId, name, name.toLowerCase() + "@example.com", "pass123");
    }
}

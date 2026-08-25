# Smart Healthcare Appointment System
### Group 10 — Software Design (XBSE2034N)

A Java console application demonstrating three design patterns
from the Group 10 project document.

---

## How to Open & Run

### Option A — VS Code (recommended)
1. Install the **Extension Pack for Java** from the VS Code marketplace
2. Open the `SmartHealthcare` folder in VS Code (`File → Open Folder`)
3. Wait for Java to index the project (bottom status bar goes green)
4. Open `src/com/smarthealthcare/main/Main.java`
5. Click **Run** above the `main` method, or press **F5**

### Option B — IntelliJ IDEA
1. `File → Open` → select the `SmartHealthcare` folder
2. Mark `src` as Sources Root (right-click → Mark Directory As)
3. Run `Main.java`

### Option C — Command line (JDK 11+)
```bash
cd SmartHealthcare
find src -name "*.java" > sources.txt
javac -d bin @sources.txt
java -cp bin com.smarthealthcare.main.Main
```

---

## Project Structure

```
SmartHealthcare/
├── src/com/smarthealthcare/
│   ├── factory/
│   │   ├── IUser.java                ← FACTORY: Product interface
│   │   └── UserFactory.java          ← FACTORY: Creator (switch statement)
│   ├── model/
│   │   ├── User.java                 ← Abstract base (implements IUser + Observer)
│   │   ├── Patient.java              ← FACTORY: Concrete Product
│   │   ├── Doctor.java               ← FACTORY: Concrete Product
│   │   ├── Admin.java                ← FACTORY: Concrete Product
│   │   ├── Appointment.java          ← OBSERVER: Subject (calls notifyObservers)
│   │   ├── Schedule.java             ← Manages doctor time slots
│   │   └── Notification.java         ← Notification data model
│   ├── observer/
│   │   ├── NotificationObserver.java ← OBSERVER: Observer interface
│   │   └── AppointmentSubject.java   ← OBSERVER: Subject interface
│   ├── strategy/
│   │   ├── SchedulingStrategy.java   ← STRATEGY: Strategy interface
│   │   ├── FirstAvailableStrategy.java  ← STRATEGY: Concrete Strategy 1
│   │   ├── PriorityBookingStrategy.java ← STRATEGY: Concrete Strategy 2
│   │   └── AppointmentScheduler.java ← STRATEGY: Context class
│   ├── auth/
│   │   └── AuthService.java          ← Credential validation & session
│   ├── service/
│   │   ├── AppointmentService.java   ← Booking/cancellation (patterns meet here)
│   │   ├── ScheduleService.java      ← Doctor schedule management
│   │   └── UserService.java          ← Admin user management
│   └── main/
│       └── Main.java                 ← Demo runner — run this
└── .vscode/
    ├── settings.json
    └── launch.json
```

---

## Design Patterns Implemented

### 1. Factory Pattern
**Files:** `IUser.java`, `UserFactory.java`, `Patient.java`, `Doctor.java`, `Admin.java`

`UserFactory.createUser(role, ...)` is the single point of user creation.
Client code never calls `new Patient()` directly — only `IUser` and
`UserFactory` are referenced externally.

```java
IUser patient = UserFactory.createUser("patient", "P001", "Alice", "alice@x.com", "pass");
IUser doctor  = UserFactory.createUser("doctor",  "D001", "Dr. Tan", "tan@x.com", "pass");
IUser admin   = UserFactory.createUser("admin",   "A001", "Bob",   "bob@x.com",  "pass");
```

### 2. Observer Pattern
**Files:** `NotificationObserver.java`, `AppointmentSubject.java`, `Appointment.java`

When an `Appointment` changes status, both `Patient` and `Doctor` are
notified automatically without `Appointment` importing either class.

```java
appt.addObserver(patient);  // register
appt.addObserver(doctor);
appt.confirm();             // → notifyObservers() → patient.update() + doctor.update()
appt.cancel();              // → notifyObservers() → both notified again
```

### 3. Strategy Pattern
**Files:** `SchedulingStrategy.java`, `FirstAvailableStrategy.java`, `PriorityBookingStrategy.java`, `AppointmentScheduler.java`

`AppointmentScheduler` delegates slot selection to the current strategy.
The algorithm can be swapped at runtime without changing the scheduler.

```java
AppointmentScheduler scheduler = new AppointmentScheduler(new FirstAvailableStrategy());
scheduler.book(doctor, schedule, null);          // picks first open slot

scheduler.setStrategy(new PriorityBookingStrategy());
scheduler.book(doctor, schedule, "2026-04-20 11:00");  // honours specific request
```

---

## Expected Console Output (abbreviated)

```
============================================================
  SMART HEALTHCARE APPOINTMENT SYSTEM — DEMO
============================================================

──────────────────────────────────────────────────────
  DEMO 1: FACTORY PATTERN — User Creation
──────────────────────────────────────────────────────
[AUTH] Registered: PATIENT 'Alice Tan' (ID: P001)
[AUTH] Registered: DOCTOR 'Dr. Amir' (ID: D001)
[AUTH] Registered: ADMIN 'Admin Bob' (ID: A001)

Login messages (from the document's code):
  Patient Alice Tan logged in. Redirecting to appointment portal.
  Doctor Dr. Amir logged in. Redirecting to schedule dashboard.
  Admin Admin Bob logged in. Redirecting to admin panel.

──────────────────────────────────────────────────────
  DEMO 3: OBSERVER PATTERN — Book Appointment
──────────────────────────────────────────────────────
[STRATEGY: FirstAvailable] Selected slot: 2026-04-20 09:00
[PATIENT NOTIFICATION → Alice Tan] Appointment APPT001 ... CONFIRMED.
[DOCTOR NOTIFICATION → Dr. Amir]  Appointment APPT001 ... CONFIRMED.

──────────────────────────────────────────────────────
  DEMO 3c: OBSERVER PATTERN — Cancel Appointment
──────────────────────────────────────────────────────
[PATIENT NOTIFICATION → Alice Tan] Appointment APPT001 ... CANCELLED.
[DOCTOR NOTIFICATION → Dr. Amir]  Appointment APPT001 ... CANCELLED.
```

---

*Requires JDK 11 or later. No external libraries needed.*

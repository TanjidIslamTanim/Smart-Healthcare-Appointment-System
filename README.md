# 🏥 Smart Healthcare Appointment System

A **Java-based healthcare appointment management system** designed using Object-Oriented Programming (OOP) principles and established **software design patterns**.

The project demonstrates the practical implementation of the **Factory, Observer, and Strategy Design Patterns** to create a modular, maintainable, flexible, and extensible healthcare appointment system.

---

## 📌 Project Overview

The **Smart Healthcare Appointment System** is designed to simplify the process of managing healthcare appointments between **patients, doctors, and administrators**.

The system provides structured functionality for:

* 👤 Patient, Doctor, and Administrator roles
* 🔐 User authentication
* 📅 Appointment booking and cancellation
* 🩺 Doctor schedule management
* 🔔 Automatic appointment notifications
* 🧠 Multiple appointment scheduling strategies
* ⚙️ Administrative user management

The main focus of this project is not only implementing system functionality, but also demonstrating how **good software architecture and design patterns** can be applied to a realistic healthcare scenario.

---

## 🎯 Project Objectives

The key objectives of the project are to:

* Apply Object-Oriented Programming principles in a practical application
* Implement software design patterns in a real-world scenario
* Reduce tight coupling between system components
* Improve maintainability and scalability
* Separate responsibilities between different modules
* Allow system behaviour to be extended with minimal modification to existing code
* Demonstrate clean and structured Java application architecture

---

# 🧩 Design Patterns

Three major software design patterns are implemented in this project:

1. **Factory Pattern** — User object creation
2. **Observer Pattern** — Appointment notifications
3. **Strategy Pattern** — Appointment scheduling algorithms

---

## 🏭 1. Factory Design Pattern

### Problem

The system supports multiple user types:

* Patient
* Doctor
* Administrator

Creating these objects directly throughout the application using `new Patient()`, `new Doctor()`, or `new Admin()` would tightly couple the application logic to specific classes.

This would make the system more difficult to maintain and extend.

### Solution

The **Factory Pattern** centralises user creation through the `UserFactory` class.

Instead of directly creating a specific user object, other components request an object through the factory.

### Main Components

```text
IUser
   ↑
   |
UserFactory
   |
   ├── Patient
   ├── Doctor
   └── Admin
```

The main classes involved are:

* `IUser`
* `UserFactory`
* `Patient`
* `Doctor`
* `Admin`

### Example

```java
IUser patient = UserFactory.createUser(
    "patient",
    "P001",
    "Alice",
    "alice@example.com",
    "password"
);
```

### Benefits

* Reduces tight coupling
* Centralises object creation
* Hides object instantiation logic
* Makes the code easier to maintain
* Makes new user roles easier to introduce

For example, a future version of the system could introduce a **Nurse** or **Receptionist** role with minimal impact on existing components.

---

## 🔔 2. Observer Design Pattern

### Problem

Whenever an appointment status changes, multiple users may need to be informed.

For example, when an appointment is:

* Confirmed
* Cancelled
* Updated

both the **Patient** and **Doctor** may need to receive the new information.

Hardcoding notification calls inside the Appointment class would create unnecessary dependencies between components.

### Solution

The system implements the **Observer Pattern**.

The `Appointment` acts as the **Subject**, while Patient and Doctor can act as **Observers**.

When the appointment status changes, all registered observers are automatically notified.

### Main Components

```text
              Appointment
                 │
                 │ Subject
                 ▼
          notifyObservers()
                 │
          ┌──────┴──────┐
          ▼             ▼
       Patient        Doctor
      Observer        Observer
```

The main components include:

* `AppointmentSubject`
* `NotificationObserver`
* `Appointment`
* `Patient`
* `Doctor`

### Example

```java
appointment.addObserver(patient);
appointment.addObserver(doctor);

appointment.confirm();
```

After the appointment status changes, the registered observers receive the update automatically.

### Benefits

* Loose coupling
* Automatic notifications
* Supports one-to-many communication
* Easy addition of new observers
* Improves scalability of the notification system

For example, an **Administrator**, notification service, or another system component could later become an observer without redesigning the Appointment class.

---

## 🧠 3. Strategy Design Pattern

### Problem

The healthcare appointment system may require different methods for selecting appointment slots.

For example:

**Standard Appointment**

The system may simply select the earliest available appointment.

**Priority Appointment**

An urgent case may require a preferred slot or different scheduling rules.

Placing every scheduling algorithm inside the `AppointmentScheduler` using multiple `if-else` conditions would make the class increasingly complicated.

### Solution

The **Strategy Pattern** separates each scheduling algorithm into its own implementation.

A common `SchedulingStrategy` interface defines the expected scheduling behaviour.

Different strategies implement this interface.

### Implemented Strategies

* `FirstAvailableStrategy`
* `PriorityBookingStrategy`

### Architecture

```text
                SchedulingStrategy
                       ↑
              ┌────────┴────────┐
              │                 │
 FirstAvailableStrategy   PriorityBookingStrategy
              │                 │
              └────────┬────────┘
                       │
              AppointmentScheduler
```

### Example

```java
AppointmentScheduler scheduler =
    new AppointmentScheduler(new FirstAvailableStrategy());

scheduler.book(doctor, schedule, null);

scheduler.setStrategy(new PriorityBookingStrategy());
```

The scheduling behaviour can therefore be changed **at runtime** without modifying the core scheduler.

### Benefits

* Flexible algorithm selection
* Runtime behaviour changes
* Cleaner scheduling logic
* Reduces large conditional statements
* Makes additional scheduling algorithms easier to implement
* Supports the **Open-Closed Principle**

---

# 🏗️ Project Architecture

The project follows a modular package structure with responsibilities separated into dedicated components.

```text
SmartHealthcare/
│
├── src/
│   └── com/
│       └── smarthealthcare/
│           │
│           ├── auth/
│           │   └── AuthService.java
│           │
│           ├── factory/
│           │   ├── IUser.java
│           │   └── UserFactory.java
│           │
│           ├── main/
│           │   └── Main.java
│           │
│           ├── model/
│           │   ├── Admin.java
│           │   ├── Appointment.java
│           │   ├── Doctor.java
│           │   ├── Notification.java
│           │   ├── Patient.java
│           │   ├── Schedule.java
│           │   └── User.java
│           │
│           ├── observer/
│           │   ├── AppointmentSubject.java
│           │   └── NotificationObserver.java
│           │
│           ├── service/
│           │   ├── AppointmentService.java
│           │   ├── ScheduleService.java
│           │   └── UserService.java
│           │
│           └── strategy/
│               ├── AppointmentScheduler.java
│               ├── FirstAvailableStrategy.java
│               ├── PriorityBookingStrategy.java
│               └── SchedulingStrategy.java
│
├── .vscode/
│   ├── launch.json
│   └── settings.json
│
└── README.md
```

This structure separates **authentication, object creation, domain models, notifications, business services, and scheduling behaviour** into different packages.

---

# ⚙️ Core Features

## 👤 User Management

The system supports three primary user roles.

### Patient

Patients can interact with appointment-related functionality and receive appointment notifications.

### Doctor

Doctors can manage schedules and receive updates regarding their appointments.

### Administrator

Administrators support overall system and user management operations.

---

## 🔐 Authentication

The project contains a dedicated authentication component:

```text
AuthService.java
```

Separating authentication from other business logic helps maintain **Separation of Concerns**.

---

## 📅 Appointment Management

The appointment module handles the central appointment workflow.

It supports operations such as:

* Creating appointments
* Confirming appointments
* Cancelling appointments
* Checking doctor schedules
* Registering observers
* Sending appointment status notifications
* Applying different scheduling strategies

Multiple design patterns work together during this workflow.

---

# 🔄 How the Patterns Work Together

A simplified system flow is:

```text
User Registration / Login
          │
          ▼
      UserFactory
          │
          ▼
 Patient / Doctor / Admin
          │
          ▼
  Appointment Booking
          │
          ▼
 AppointmentScheduler
          │
          ▼
  Scheduling Strategy
          │
          ▼
 Appointment Created
          │
          ▼
  Appointment Status
        Changes
          │
          ▼
   Observer Pattern
          │
     ┌────┴────┐
     ▼         ▼
  Patient    Doctor
  Notified   Notified
```

This demonstrates how the patterns are used as part of the overall system architecture rather than as isolated programming examples.

---

# 💻 Technologies & Concepts

| Technology / Concept        | Purpose                          |
| --------------------------- | -------------------------------- |
| Java                        | Core application development     |
| Object-Oriented Programming | Application architecture         |
| Factory Pattern             | Dynamic user creation            |
| Observer Pattern            | Appointment notifications        |
| Strategy Pattern            | Appointment scheduling           |
| Git                         | Version control                  |
| GitHub                      | Repository hosting and portfolio |
| VS Code                     | Development environment          |
| UML                         | Software architecture modelling  |

---

# 🧱 Software Engineering Principles

The project demonstrates several important software engineering concepts.

### Encapsulation

Data and related behaviours are grouped inside appropriate classes.

### Abstraction

Interfaces are used to define common behaviours without requiring other components to understand implementation details.

### Polymorphism

Different implementations can be accessed through common interfaces.

### Separation of Concerns

Authentication, scheduling, notifications, services, and models have separate responsibilities.

### Loose Coupling

Components depend on interfaces and abstractions where appropriate rather than being tightly connected to specific implementations.

### High Cohesion

Classes and packages are designed around focused responsibilities.

### Open-Closed Principle

The architecture allows new behaviours, particularly scheduling strategies, to be introduced without heavily modifying existing code.

---

# ▶️ Running the Project

## Prerequisites

Before running the application, make sure you have:

* Java JDK installed
* VS Code, IntelliJ IDEA, or another Java IDE
* Git if you want to clone the repository

No external libraries are required for the core application.

---

## Clone the Repository

```bash
git clone https://github.com/TanjidIslamTanim/Smart-Healthcare-Appointment-System.git
```

Move into the project directory:

```bash
cd Smart-Healthcare-Appointment-System
```

---

## Run with VS Code

1. Open the project folder in **Visual Studio Code**.
2. Install the **Extension Pack for Java** if required.
3. Navigate to:

```text
src/com/smarthealthcare/main/Main.java
```

4. Open `Main.java`.
5. Click **Run** above the `main()` method.

---

# 🚀 Future Improvements

The current project provides a foundation that can be extended into a more complete healthcare platform.

Potential improvements include:

* 🌐 Web-based user interface
* 🗄️ Database integration
* 🔗 REST API development
* 🔐 Secure password hashing
* 🛡️ Role-Based Access Control (RBAC)
* ✉️ Email appointment notifications
* 📱 SMS or mobile notifications
* 🩺 Doctor specialty search
* 🔄 Appointment rescheduling
* 📋 Appointment history
* 🧑‍⚕️ Patient profile management
* 🧪 Unit and integration testing
* ☁️ Cloud deployment
* 📊 Administrative dashboard
* 🔒 Additional application security controls

---

# 🎓 Academic Context

This project was developed as part of a **Software Design** project to demonstrate the practical application of software design patterns in a realistic system.

The architecture focuses particularly on:

| Design Pattern | Application               |
| -------------- | ------------------------- |
| 🏭 Factory     | User object creation      |
| 🔔 Observer    | Appointment notifications |
| 🧠 Strategy    | Appointment scheduling    |

The project provided practical experience in translating software design concepts and UML-based architecture into working Java code.

---

# 👥 Contributors

### Tanjid Islam Tanim

**Bachelor of Software Engineering (Hons)**
University of Wollongong Malaysia

### Hadif

**Project Member**

**Group 10 — Smart Healthcare Appointment System**

---

# 📄 Project Purpose

This repository is maintained for **educational, demonstration, and portfolio purposes**.

---

# ⭐ Summary

The **Smart Healthcare Appointment System** demonstrates how software design patterns can be applied to solve common architectural problems within a healthcare appointment environment.

Rather than placing all responsibilities inside a single class, the system separates:

* **Object creation** using the Factory Pattern
* **Notifications** using the Observer Pattern
* **Scheduling algorithms** using the Strategy Pattern
* **Authentication and business operations** through dedicated services

This results in a cleaner and more modular architecture that is easier to **understand, maintain, test, and extend**.

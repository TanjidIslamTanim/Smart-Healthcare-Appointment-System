package com.smarthealthcare.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// ============================================================
// MODEL: Notification
//
// Represents a notification message sent to a user.
// Created and persisted by the NotificationService whenever
// the Observer Pattern triggers an update().
// ============================================================
public class Notification {

    public enum NotifStatus { UNREAD, READ }

    private final String notifId;
    private final String recipientId;
    private final String message;
    private final String sentAt;
    private NotifStatus status;

    public Notification(String notifId, String recipientId, String message) {
        this.notifId      = notifId;
        this.recipientId  = recipientId;
        this.message      = message;
        this.sentAt       = LocalDateTime.now()
                               .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.status       = NotifStatus.UNREAD;
    }

    public void markRead() { this.status = NotifStatus.READ; }

    public String getNotifId()     { return notifId;     }
    public String getRecipientId() { return recipientId; }
    public String getMessage()     { return message;     }
    public String getSentAt()      { return sentAt;      }
    public NotifStatus getStatus() { return status;      }

    @Override
    public String toString() {
        return "[" + status + "] " + sentAt + " → " + message;
    }
}

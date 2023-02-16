package com.team05.codebotiics.mopi_webapp.model.beans;

import com.team05.codebotiics.mopi_webapp.model.enums.Event;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Represents the "Audit Trail" table
 */
@Entity
@Table(name="Audit_Trail")
public class AuditTrail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private int eventId;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="Badge_Number", referencedColumnName = "Badge_Number")
    private Police police;

    @Basic
    @Column(name="date_and_time")
    @NotNull( message = "Please enter date/time")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateAndTime;

    @Basic
    @Enumerated(EnumType.STRING)
    @NotNull(message= "You must define the operation")
    private Event event;

    @Basic
    @Column(name="description")
    private String description;

    public AuditTrail(Police police, @NotNull(message = "Please enter date/time") LocalDateTime dateAndTime, @NotNull(message = "You must define the operation") Event event, String description) {
        this.police = police;
        this.dateAndTime = dateAndTime;
        this.event = event;
        this.description = description;
    }
    public AuditTrail(){}

    public Police getPolice() {
        return police;
    }

    public void setPolice(Police police) {
        this.police = police;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

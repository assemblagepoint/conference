package com.example.conference.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    private Talk talk;

    @Column(nullable = false)
    private LocalDateTime time_start;

    @Column(nullable = false)
    private LocalDateTime time_end;

    @Column(updatable = false)
    private LocalDateTime createDate;

    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
    }

    public Schedule(Room room, Talk talk, LocalDateTime time_start, LocalDateTime time_end) {
        this.room = room;
        this.talk = talk;
        this.time_start = time_start;
        this.time_end = time_end;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "time_start = " + time_start + ", " +
                "time_end = " + time_end + ", " +
                "createDate = " + createDate + ")";
    }
}
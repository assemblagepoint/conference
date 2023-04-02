package com.example.conference.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Talk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer duration;

    @ManyToMany
    @JoinTable(name = "user_talks",
            joinColumns = @JoinColumn(name = "talk_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "talk", orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    public Talk(String title, Integer duration, Set<User> users) {
        this.title = title;
        this.duration = duration;
        this.users = users;
    }

    @Column(updatable = false)
    private LocalDateTime createDate;

    @PrePersist
    protected void onCreate() {                 // установка времени создания объекта
        this.createDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "title = " + title + ", " +
                "duration = " + duration + ", " +
                "createDate = " + createDate + ")";
    }
}
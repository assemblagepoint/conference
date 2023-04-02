package com.example.conference.entity;

import com.example.conference.entity.enums.ERole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, updatable = false)
    private String username;

    @Column(nullable = false)
    private String lastname;

    @Column(unique = true)
    private String email;

    @Column(length = 3000)
    private String password;

    @ElementCollection(targetClass = ERole.class)
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"))
    private Set<ERole> roles = new HashSet<>();    // роль

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_talks",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "talk_id")
    )
    private Set<Talk> talks = new HashSet<>();

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createDate;

    public User(Long id,
                String username,
                String email,
                String password,
                Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public User(String name, String username, String lastname, String email, String password, Set<ERole> roles) {
        this.name = name;
        this.username = username;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    @PrePersist
    protected void onCreate() {                     // установка времени создания объекта
        this.createDate = LocalDateTime.now();
    }


    /**
     * SECURITY
     */

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "username = " + username + ", " +
                "lastname = " + lastname + ", " +
                "email = " + email + ", " +
                "password = " + password + ", " +
                "roles = " + roles + ", " +
                "authorities = " + authorities + ", " +
                "createDate = " + createDate + ")";
    }
}
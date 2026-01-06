package com.RadixLogos.DsCatalog.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

@Entity
public class PasswordRecover {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private Instant tokenDuration;

    public PasswordRecover() {
    }

    public PasswordRecover(String email, String token, Instant tokenDuration) {
        this.email = email;
        this.token = token;
        this.tokenDuration = tokenDuration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getTokenDuration() {
        return tokenDuration;
    }

    public void setTokenDuration(Instant tokenDuration) {
        this.tokenDuration = tokenDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PasswordRecover that = (PasswordRecover) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

package com.liseh.auth.persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "CLIENT")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_seq_gen")
    @SequenceGenerator(name = "client_seq_gen", allocationSize = 25, sequenceName = "client_seq")
    private Long id;

    @Column(nullable = false, unique = true)
    private String clientId;

    @Column(nullable = false, unique = true)
    private String clientSecret;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private Boolean isVerified;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @PrePersist
    public void onPrePersist() {
        created = new Date();
    }

    @PreUpdate
    public void onPreUpdate() {
        updated = new Date();
    }
}

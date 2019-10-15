package com.liseh.auth.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
    @SequenceGenerator(name = "user_seq_gen", allocationSize = 25, sequenceName = "user_seq")
    private Long id;

    private String userId;

    @Column(unique = true)
    private String username;

    @Column(length = 60)
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @PostPersist
    public void onPersist() {
        created = new Date();
    }

    @PostUpdate
    public void onUpdate() {
        updated = new Date();
    }
}

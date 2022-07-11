package com.fedebonel.recipemvc.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;

    @ManyToOne
    private User user;
}

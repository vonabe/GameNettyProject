package ru.vonabe.entitys;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Admin")
public class EntityAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;


}

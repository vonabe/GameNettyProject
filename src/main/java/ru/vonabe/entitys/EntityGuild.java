package ru.vonabe.entitys;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Guild")
public class EntityGuild {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;


}

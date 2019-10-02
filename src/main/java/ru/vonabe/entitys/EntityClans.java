package ru.vonabe.entitys;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Clans")
public class EntityClans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "tag")
    private String tag;

    @Column(name = "strength")
    private Integer strength;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "coffers")
    private Integer coffers;

    @Column(name = "cost")
    private Integer cost;

}

package ru.vonabe.entitys

import lombok.Data

import javax.persistence.*

@Entity
@Data
@Table(name = "Units")
class EntityUnits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0

    @Column(name = "attack")
    var attack: Int = 0

    @Column(name = "protection")
    var protection: Int = 0

    @Column(name = "health")
    var health: Int = 0

    @Column(name = "attack_techno")
    var attack_techno: Int = 0

    @Column(name = "protection_techno")
    var protection_techno: Int = 0

    @Column(name = "health_techno")
    var health_techno: Int = 0

    @Column(name = "attack_techno_all")
    var attack_techno_all: Boolean = false

    @Column(name = "protection_techno_all")
    var protection_techno_all: Boolean = false

    @Column(name = "health_techno_all")
    var health_techno_all: Boolean = false

    @Column(name = "ready")
    var ready: Boolean = false

    @Column(name = "orders")
    var orders: Int = 0

    constructor(attack: Int, protection: Int, health: Int, orders: Int) {
        this.attack = attack
        this.protection = protection
        this.health = health
        this.orders = orders
    }

    constructor()

}

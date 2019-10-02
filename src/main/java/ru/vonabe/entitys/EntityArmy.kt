package ru.vonabe.entitys

import lombok.Data

import javax.persistence.*

@Entity
@Data
@Table(name = "Army")
class EntityArmy {

    constructor()

    constructor(sniper: Int, desantnic: Int, robot: Int, unit1: Int, unit2: Int, unit3: Int) {
        this.sniper = sniper
        this.desantnic = desantnic
        this.robot = robot
        this.unit1 = unit1
        this.unit2 = unit2
        this.unit3 = unit3
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0
        set(id) {
            field = this.id
        }

    @Column(name = "sniper")
    var sniper: Int = 0
        set(sniper) {
            field = this.sniper
        }

    @Column(name = "desantnic")
    var desantnic: Int = 0
        set(desantnic) {
            field = this.desantnic
        }

    @Column(name = "robot")
    var robot: Int = 0
        set(robot) {
            field = this.robot
        }

    @Column(name = "unit1")
    var unit1: Int = 0
        set(unit1) {
            field = this.unit1
        }

    @Column(name = "unit2")
    var unit2: Int = 0
        set(unit2) {
            field = this.unit2
        }

    @Column(name = "unit3")
    var unit3: Int = 0
        set(unit3) {
            field = this.unit3
        }

}

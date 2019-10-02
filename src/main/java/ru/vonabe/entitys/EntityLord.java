package ru.vonabe.entitys;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Lords")
@Data
public class EntityLord {
//    ogin, password, email, id_army, date_utc, date_time,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String login;
    private String password;
    private String email;
    private boolean verify;
    private Integer energy;
    private Integer slot;
    private Integer experience;
    private Integer victories;
    private Integer defeats;
    private Integer draw;
    @Column(name = "\"escape\"")
    private Integer escape;
    private Integer victorycount;
    private Integer lvl;
    private Integer map;
    private float x;
    private float y;
    private Integer gildia_id;
    private Integer clan_id;
    private Integer army_id;
    private String date;
    private String time;
    private String ip;

    public EntityLord(String login, String password, String email, Integer army_id, String date, String time, String ip) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.army_id = army_id;
        this.date = date;
        this.time = time;
        this.ip = ip;
    }

    public EntityLord() { }

}

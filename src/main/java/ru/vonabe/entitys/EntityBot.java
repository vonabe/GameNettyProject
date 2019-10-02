package ru.vonabe.entitys;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Bots")
public class EntityBot {

    public EntityBot() { }

    public EntityBot(String login, Integer lvl, Integer map, float x, float y, Integer id_army) {
        this.login = login;
        this.lvl = lvl;
        this.map = map;
        this.x = x;
        this.y = y;
        this.army_id = id_army;
    }

    public EntityLord toEntityLord(){
        EntityLord lord = new EntityLord();
        lord.setId(id);
        lord.setLogin(login);
        lord.setExperience(experience);
        lord.setVictories(victories);
        lord.setDefeats(defeats);
        lord.setDraw(draw);
        lord.setEscape(escape);
        lord.setLvl(lvl);
        lord.setMap(map);
        lord.setX(x);
        lord.setY(y);
        lord.setVictorycount(victorycount);
        lord.setArmy_id(army_id);
        return lord;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "login")
    private String login;

    @Column(name = "experience")
    private Integer experience;

    @Column(name = "victories")
    private Integer victories;

    @Column(name = "defeats")
    private Integer defeats;

    @Column(name = "draw")
    private Integer draw;

    @Column(name = "\"escape\"")
    private Integer escape;

    @Column(name = "lvl")
    private Integer lvl;

    @Column(name = "map")
    private Integer map;

    @Column(name = "x")
    private float x;

    @Column(name = "y")
    private float y;

    @Column(name = "army_id")
    private Integer army_id;

    @Column(name = "victorycount")
    private Integer victorycount;

}


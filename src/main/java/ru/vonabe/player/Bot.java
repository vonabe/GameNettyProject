package ru.vonabe.player;

import ru.vonabe.entitys.EntityBot;

public class Bot extends Player {

    public Bot(EntityBot lord) {
        super(lord.toEntityLord());
    }

    @Override
    public Player init() {
        return super.init();
    }
}

package ru.vonabe.packet;

import lombok.Data;

@Data
public class Attack {

    String login;

    public Attack() {}

    public Attack(String login) {
        this.login = login;
    }

}

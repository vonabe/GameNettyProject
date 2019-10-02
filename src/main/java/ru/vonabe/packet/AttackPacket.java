package ru.vonabe.packet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import ru.vonabe.manager.BattleManager;
import ru.vonabe.manager.Client;
import ru.vonabe.manager.ClientManager;
import ru.vonabe.manager.MapManager;

import java.io.IOException;

public class AttackPacket implements Packet {

    private FastData data = null;

    @Override
    public void handler() {
        if (!ClientManager.getClient(data.getUuid()).auto)
            return;

        JSONObject object = this.data.getData();
        String login = object.get("login").toString();

        ObjectMapper mapper = new ObjectMapper();
        try {
            Attack attack = mapper.readValue(object.toJSONString(), Attack.class);
            System.out.println("Jackson Mapper: "+attack.login);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Client client_attacked = ClientManager.getClientName(login);
        Client client_attack = ClientManager.getClient(data.getUuid());

        // System.out.println("attack " + client_attack + " " +
        // client_attacked);

        if (client_attacked != null) {
            if (!client_attack.player.isBattle() && !client_attacked.player.isBattle()
                    && client_attacked.player.getMap() == client_attack.player.getMap()) {

                client_attacked.player.attack(client_attack.uuid, client_attacked.uuid);
                client_attack.player.attack(client_attacked.uuid, client_attack.uuid);

                MapManager.getMap(String.valueOf(client_attack.player.getMap())).addBattle(client_attack.uuid, client_attacked.uuid);

                BattleManager.addBattle(client_attack.player);
                BattleManager.addBattle(client_attacked.player);

                PacketWriter writer = PoolPacketWriter.getWriter();

                JSONObject data = writer.getData();
                data.put("message", "Вы напали на " + client_attacked.player.getName());
                JSONObject obj = writer.getObject();
                obj.put("action", "attack");
                obj.put("data", data);
                client_attack.write(writer);

                writer = PoolPacketWriter.getWriter();
                data = writer.getData();
                data.put("message", "На вас напал " + client_attack.player.getName());
                obj = writer.getObject();
                obj.put("action", "attack");
                obj.put("data", data);
                client_attacked.write(writer);
            } else {
                PacketWriter writer = PoolPacketWriter.getWriter();
                JSONObject data = writer.getData();
                data.put("message", "Противник уже в бою.");
                JSONObject obj = writer.getObject();
                obj.put("action", "error");
                obj.put("data", data);
                ClientManager.getClient(this.data.getUuid()).write(writer);
            }
        } else {
            PacketWriter writer = PoolPacketWriter.getWriter();
            JSONObject data = writer.getData();
            data.put("message", "Противник не найден.");
            JSONObject obj = writer.getObject();
            obj.put("action", "attack");
            obj.put("data", data);
            ClientManager.getClient(this.data.getUuid()).write(writer);
        }
        // ClientManager.getClient(data.getUuid()).player.;
    }

    @Override
    public void setData(FastData data) {
        // TODO Auto-generated method stub
        this.data = data;
    }

    @Override
    public FastData getData() {
        // TODO Auto-generated method stub
        return this.data;
    }

}

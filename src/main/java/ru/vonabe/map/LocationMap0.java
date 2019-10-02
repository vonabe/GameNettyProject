package ru.vonabe.map;

import com.badlogic.gdx.math.Rectangle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.vonabe.entitys.EntityBot;
import ru.vonabe.manager.Client;
import ru.vonabe.manager.ClientManager;
import ru.vonabe.packet.PacketWriter;
import ru.vonabe.packet.PoolPacketWriter;
import ru.vonabe.player.Bot;
import ru.vonabe.player.Player;
import ru.vonabe.repository.BotsRepository;

import java.util.*;

public class LocationMap0 extends Location {

    private HashMap<String, Player> hashbot = new HashMap<>();
    private HashMap<String, Client> hashplayer = new HashMap<>();
    private HashMap<String, Rectangle> hashmapBlocks = new HashMap<>();
    private HashMap<Integer, BattleFlag> hashmapBattle = new HashMap<>();

    private String name = "Location 0";
    private int id_location = 0;
    private PacketWriter writer_map_state = new PacketWriter();

    public LocationMap0() {
        Rectangle rectangle = new Rectangle(0.0f, -10.0f, 1060.0f, 10.0f);
        Rectangle rectangle2 = new Rectangle(-15.0f, 0.0f, 10.0f, 984.0f);
        Rectangle rectangle3 = new Rectangle(0.0f, 984.0f, 1060.0f, 10.0f);
        Rectangle rectangle4 = new Rectangle(1060.0f, 0.0f, 10.0f, 984.0f);
        hashmapBlocks.put("top", rectangle3);
        hashmapBlocks.put("right", rectangle4);
        hashmapBlocks.put("left", rectangle2);
        hashmapBlocks.put("bot", rectangle);

        Rectangle rectBarrack = new Rectangle(170.0f, 95.0f, 128.0f, 128.0f);
        hashmapBlocks.put("barrack", rectBarrack);
        Rectangle rectPortal = new Rectangle(800.0f, 460.0f, 128.0f, 128.0f);
        hashmapBlocks.put("portal", rectPortal);
        Rectangle rectLab = new Rectangle(700.0f, 90.0f, 128.0f, 128.0f);
        hashmapBlocks.put("lab", rectLab);
        Rectangle rectShuttle = new Rectangle(170.0f + 8f, 355.0f - 8f, 114.0f, 114.0f);
        hashmapBlocks.put("shuttle", rectShuttle);
        Rectangle rectCommand = new Rectangle(450.0f, 330.0f, 128.0f, 128.0f);
        hashmapBlocks.put("cc", rectCommand);

        List<EntityBot> byLocation = BotsRepository.Companion.getInstance().findByLocation(id_location);
        for (EntityBot entityBot : byLocation) {
            hashbot.put(entityBot.getLogin(), new Bot(entityBot).init());
        }

        this.reloadData();
    }

    @Override
    public boolean addPlayer(String uuid) {
        boolean add = (hashplayer.put(uuid, ClientManager.getClient(uuid)) == null) ? true : false;
        if (add)
            this.reloadData();
        return add;
    }

    @Override
    public boolean removePlayer(String uuid) {
        boolean add = (hashplayer.remove(uuid) == null) ? true : false;
        if (add)
            this.reloadData();
        return add;
    }

    @Override
    public void render(float delta) {
        Iterator<Client> it_players = hashplayer.values().iterator();
        while (it_players.hasNext()) {
            Client client = it_players.next();
            if (client.auto) {
                Player player = client.player;
                if (!player.isBattle() && player.isMove()) {
                    if(player.move()) {
                        PacketWriter writer = PoolPacketWriter.getWriter();
                        JSONObject object = writer.getObject();
                        object.put("action", "move");
                        object.put("data", player.getCoordinate());

                        notifyPlayers(writer);
                    }
                }
            }
        }
    }

    private void notifyPlayers(PacketWriter writer) {
        Collection<Client> clients = getClients();
        Iterator<Client> it_clients = clients.iterator();
        try {
            PacketWriter clone = writer.clone();
            while (it_clients.hasNext()) {
                Client client = it_clients.next();
                client.write(clone);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getIdLocation() {
        return id_location;
    }

    @Override
    public Collection<Client> getClients() {
        return hashplayer.values();
    }

    @Override
    public HashMap<String, Rectangle> getHashmapBlocks() {
        return hashmapBlocks;
    }

    @Override
    public PacketWriter getMapInfo() {
        // System.out.println("getMapInfo: " + writer_map_state.toString());
        return writer_map_state;
    }

    @Override
    public void addBattle(String uuid_attack, String uuid_attacked) {
        if (hashmapBattle.containsKey((uuid_attack + uuid_attacked).hashCode()))
            return;
        BattleFlag flag = new BattleFlag();
        flag.name_attack = ClientManager.getClient(uuid_attack).player.getName();
        flag.name_attacked = ClientManager.getClient(uuid_attacked).player.getName();
        flag.time = Calendar.getInstance().getTime().toLocaleString();
        flag.x = ClientManager.getClient(uuid_attack).player.getPosition().x;
        flag.y = ClientManager.getClient(uuid_attack).player.getPosition().y;
        hashmapBattle.put((uuid_attack + uuid_attacked).hashCode(), flag);
        reloadData();

        PacketWriter writer = PoolPacketWriter.getWriter();
        writer.getObject().put("action", "map_add_flag");
        writer.getObject().put("data", flag.toObject());
        notifyPlayers(writer);
    }

    @Override
    public void removeBattle(String uuid_attack, String uuid_attacked) {
        BattleFlag flag = hashmapBattle.get((uuid_attack + uuid_attacked).hashCode());
        hashmapBattle.remove(flag);
        reloadData();
        PacketWriter writer = PoolPacketWriter.getWriter();
        writer.getObject().put("action", "map_remove_flag");
        writer.getObject().put("data", flag.toObject());
        notifyPlayers(writer);
    }

    private void reloadData() {
        this.writer_map_state.clear();

        JSONObject data = writer_map_state.getData();
        data.put("map_id", String.valueOf(this.id_location));
        data.put("name", this.name);

        JSONArray array_blocks = new JSONArray();
        Iterator<String> it_blocks = this.hashmapBlocks.keySet().iterator();
        while (it_blocks.hasNext()) {
            String name = it_blocks.next();
            Rectangle rect = this.hashmapBlocks.get(name);
            JSONObject block_json = new JSONObject();
            block_json.put(name, rect.toString());
            array_blocks.add(block_json);
        }
        data.put("blocks", array_blocks);

        JSONArray array_flags = new JSONArray();
        Iterator<Integer> flags_battle = this.hashmapBattle.keySet().iterator();
        while (flags_battle.hasNext()) {
            int hash = flags_battle.next();
            BattleFlag flag = this.hashmapBattle.get(hash);
            JSONObject flag_json = new JSONObject();
            flag_json.put("attack", flag.name_attack);
            flag_json.put("attacked", flag.name_attacked);
            flag_json.put("time", flag.time);
            flag_json.put("x", flag.x);
            flag_json.put("y", flag.y);
            array_flags.add(flag_json);
        }
        data.put("flags", array_flags);

        JSONArray array_bots = new JSONArray();
        Iterator<String> it_bot = hashbot.keySet().iterator();
        while (it_bot.hasNext()) {
            String id_bot = it_bot.next();
            Player bot = hashbot.get(id_bot);
            JSONObject obj_bot = new JSONObject();
            obj_bot.put(id_bot, bot.getPacketPublic());
            array_bots.add(obj_bot);
        }
        data.put("bots", array_bots);

        JSONArray array_players = new JSONArray();
        Iterator<String> it_player = hashplayer.keySet().iterator();
        while (it_player.hasNext()) {
            String id_client = it_player.next();
            Client client = hashplayer.get(id_client);
            JSONObject obj_bot = new JSONObject();
            obj_bot.put(id_client, client.player.getPacketPublic());
            array_players.add(obj_bot);
        }
        data.put("players", array_players);

        JSONObject object = this.writer_map_state.getObject();
        object.put("action", "map_state");
        object.put("data", data);
    }

}

package ru.vonabe.player;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.json.simple.JSONObject;
import ru.vonabe.entitys.EntityArmy;
import ru.vonabe.entitys.EntityLord;
import ru.vonabe.entitys.EntityUnits;
import ru.vonabe.manager.Client;
import ru.vonabe.manager.MapManager;
import ru.vonabe.map.Location;
import ru.vonabe.packet.MovePacket.MoveType;
import ru.vonabe.packet.PacketWriter;
import ru.vonabe.packet.PoolPacketWriter;
import ru.vonabe.repository.ArmyRepository;
import ru.vonabe.repository.LordRepository;
import ru.vonabe.repository.UnitsRepository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class Player {

//    private static final String SQL_GET_PLAYER = "select Lord.* from Lord where login = '%1$s';";
//    private static final String SQL_UPDATE_PLAYER = "update Lord set x='%1$s', y='%2$s' where Lord.login = '%3$s';";

    private Client client = null;
    private MoveType move = MoveType.STOP;
    private String name = null;
    public String uuid = null;

    //    private int lvl = 1, victories = 0, defeats = 0, draw = 0, escape = 0, victorycount = 0, map_id = 0;
//    private int energy = 0, slot = 0, experience = 0, gildia_id = 0, clan_id = 0, army_id = 0;
    private boolean visible = true;

    private EntityLord entityBot;
    private Army army_ = null;

    private Vector2 position = new Vector2(), tmp_position = new Vector2();
    private Rectangle rectangle = new Rectangle(0, 0, 64, 64 / 2);
    private float speed = 5.0f;
    private String collision = null;
    private JSONObject object_myinfo = new JSONObject(), object_coordinate = new JSONObject(), object_packet = new JSONObject();

    public Player(String name, Client cl) {
        this.client = cl;
        this.name = name;
        this.army_ = new Army(this);
        if (this.client != null) {
            this.uuid = this.client.uuid;
        } else {
            this.uuid = UUID.randomUUID().toString();
        }
    }

    public Client getClient() {
        return client;
    }

    public Player(EntityLord bot) {
        this.army_ = new Army(this);
        if (this.client != null) {
            this.uuid = this.client.uuid;
        } else {
            this.uuid = UUID.randomUUID().toString();
        }
        this.entityBot = bot;
        this.name = bot.getLogin();
        this.position.set(bot.getX(), bot.getY());
        this.tmp_position.set(this.position);
    }

    public Army getArmy() {
        return army_;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void attack(String uuid_attack, String uuid_attacked) {
        this.army_.battle(uuid);
        this.visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isBattle() {
        return this.army_.isBattle();
    }

    public Player init() {
        if(entityBot == null){
            this.entityBot = LordRepository.Companion.getInstance().findByLogin(name);
        }

        EntityArmy army = ArmyRepository.Companion.getInstance().findById(entityBot.getArmy_id());

        EntityUnits sniper = UnitsRepository.Companion.getInstance().findById(army.getSniper());
        EntityUnits desantnic = UnitsRepository.Companion.getInstance().findById(army.getDesantnic());
        EntityUnits robot = UnitsRepository.Companion.getInstance().findById(army.getRobot());

        army_.init(
                convertUnit(army.getUnit1(), sniper, desantnic, robot),
                convertUnit(army.getUnit2(), sniper, desantnic, robot),
                convertUnit(army.getUnit3(), sniper, desantnic, robot)
        );

        this.setPosition(this.entityBot.getX(), this.entityBot.getY());
        this.tmp_position.set(this.position);
        System.out.println("CreateLord - " + this.name);
        return this;
    }

    private Unit convertUnit(int type, EntityUnits sniper, EntityUnits des, EntityUnits robot) {
        Unit converUnit = null;
        switch (type) {
            case 1:
                converUnit = new Sniper(sniper);
                break;
            case 2:
                converUnit = new Desant(des);
                break;
            case 3:
                converUnit = new Robot(robot);
                break;
        }
        return converUnit;
    }

    public boolean move() {
        switch (this.move) {
            case STOP:
                break;
            case TOP:
                this.tmp_position.add(0, speed);
                if (step())
                    this.position.add(0, speed);
                else {
                    sendCollision();
                    return false;
                }
                break;
            case BOT:
                this.tmp_position.add(0, -speed);
                if (step())
                    this.position.add(0, -speed);
                else {
                    sendCollision();
                    return false;
                }
                break;
            case RIGHT:
                this.tmp_position.add(speed, 0);
                if (step())
                    this.position.add(speed, 0);
                else {
                    sendCollision();
                    return false;
                }
                break;
            case LEFT:
                this.tmp_position.add(-speed, 0);
                if (step())
                    this.position.add(-speed, 0);
                else {
                    sendCollision();
                    return false;
                }
                break;
            default:
                break;
        }
        return true;
        // System.out.println(
        // this.position + " : " +
        // MapManager.getMap(this.map_id).getHashmapBlocks().values().toArray()[0]);
    }

    private void sendCollision() {
        PacketWriter writer = PoolPacketWriter.getWriter();
        JSONObject data = writer.getData();
        data.put("block", collision);
        JSONObject object = writer.getObject();
        object.put("action", "collision");
        object.put("data", data);
        client.write(writer);
    }

    private boolean step() {
        this.rectangle.setPosition(this.tmp_position);
        Location location = MapManager.getMap(String.valueOf(this.entityBot.getMap()));
        HashMap<String, Rectangle> blocks = location.getHashmapBlocks();
        Iterator<String> it_block = blocks.keySet().iterator();
        while (it_block.hasNext()) {
            String name_block = it_block.next();
            Rectangle rect_block = blocks.get(name_block);
            if (this.rectangle.overlaps(rect_block)) {
                this.rectangle.setPosition(this.position);
                this.tmp_position.set(this.position);
                this.collision = name_block;
                this.move = MoveType.STOP;
                return false;
            }
        }
        this.collision = null;
        return true;
    }

    public JSONObject getMyinfo() {
        object_myinfo.clear();
        object_myinfo.put("lvl", entityBot.getLvl());
        object_myinfo.put("units", army_.getUnits());
        object_myinfo.put("victories", entityBot.getVictories());
        object_myinfo.put("defeats", entityBot.getDefeats());
        object_myinfo.put("draw", entityBot.getDraw());
        object_myinfo.put("escape", entityBot.getEscape());
        object_myinfo.put("victorycount", entityBot.getVictorycount());
        object_myinfo.put("energy", entityBot.getEnergy());
        object_myinfo.put("slot", entityBot.getSlot());
        object_myinfo.put("experience", entityBot.getExperience());
        object_myinfo.put("clan_id", entityBot.getClan_id());
        object_myinfo.put("gildia_id", entityBot.getGildia_id());
        object_myinfo.put("army", army_.getData());
        return object_myinfo;
    }

    public JSONObject getCoordinate() {
        object_coordinate.clear();
        object_coordinate.put("login", getName());
        object_coordinate.put("x", getPosition().x);
        object_coordinate.put("y", getPosition().y);
        object_coordinate.put("v", move.toString());
        object_coordinate.put("id_map", entityBot.getMap());
        return object_coordinate;
    }

    public JSONObject getPacketPublic() {
        object_packet.clear();
        object_packet.put("login", getName());
        object_packet.put("x", getPosition().x);
        object_packet.put("y", getPosition().y);
        object_packet.put("v", move.toString());
        object_packet.put("id_map", entityBot.getMap());
        object_packet.put("lvl", entityBot.getLvl());
        object_packet.put("units", army_.getUnits());
        object_packet.put("victories", entityBot.getVictories());
        object_packet.put("defeats", entityBot.getDefeats());
        object_packet.put("draw", entityBot.getDraw());
        object_packet.put("escape", entityBot.getEscape());
        object_packet.put("victorycount", entityBot.getVictorycount());
        object_packet.put("clan_id", entityBot.getClan_id());
        object_packet.put("gildia_id", entityBot.getGildia_id());
        object_packet.put("visible", visible);
        return object_packet;
    }

    public void setMap(int map) {
        this.entityBot.setMap(map);
    }

    public int getMap() {
        return entityBot.getMap();
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public MoveType getMove() {
        return move;
    }

    public boolean isMove() {
        return this.move != MoveType.STOP;
    }

    public void setMove(MoveType move) {
        this.move = move;
    }

    public String getName() {
        return this.name;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public void save() {
        EntityLord user = LordRepository.Companion.getInstance().findByLogin(this.name);
        user.setX(this.getPosition().x);
        user.setY(this.getPosition().y);
        LordRepository.Companion.getInstance().update(user);
    }

}

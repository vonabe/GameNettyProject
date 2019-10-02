package ru.vonabe.manager;

import ru.vonabe.entitys.EntityArmy;
import ru.vonabe.entitys.EntityLord;
import ru.vonabe.entitys.EntityUnits;
import ru.vonabe.repository.ArmyRepository;
import ru.vonabe.repository.LordRepository;
import ru.vonabe.repository.UnitsRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistrationManager {

    private final static String SQL_INSERT_ADD_UNITS = "insert into Units(attack, protection, health, orders) values ('%1$s','%2$s','%3$s','%4$s');\n";
    private final static String SQL_INSERT_ADD_ID_UNITS = "insert into Army(sniper, desantnic, robot, unit1, unit2, unit3)"
            + " values ('%1$s','%2$s','%3$s','%4$s','%5$s','%6$s');\n";
    private final static String SQL_INSERT_ADD_LORDY = "insert into Lord(login,password,email,army_id,date,time,ip)"
            + " values ('%1$s','%2$s','%3$s','%4$s','%5$s','%6$s','%7$s');\n";

    final private static String SQL_LOGIN = "select Lord.[id] from Lord where login = '%1$s';";
    final private static String SQL_EMAIL = "select Lord.[id] from Lord where email = '%1$s';";

    private static DateFormat format_date = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat format_time = new SimpleDateFormat("HH:mm:ss");

    public static String registration(String login, String password, String email, String uuid) {

        if (isLogin(login)) {
            return "���� ����� ��� �����.";
        } else if (isEmail(email)) {
            return "���� Email ��� �����.";
        } else {

            EntityUnits sniper = UnitsRepository.Companion.getInstance().save(new EntityUnits(4, 5, 6, 1));
            EntityUnits desantnic = UnitsRepository.Companion.getInstance().save(new EntityUnits(5, 4, 6, 2));
            EntityUnits robot = UnitsRepository.Companion.getInstance().save(new EntityUnits(10, 9, 11, 3));

            EntityArmy army = ArmyRepository.Companion.getInstance().save(new EntityArmy(sniper.getId(), desantnic.getId(), robot.getId(), 2, 1, 2));

            Date now = new Date();
            String date_utc = format_date.format(now);
            String date_time = format_time.format(now);

            LordRepository.Companion.getInstance().save(new EntityLord(login, password, email, army.getId(), date_utc, date_time, ClientManager.getClient(uuid).getIP()));

            return null;
        }
    }

    private static boolean isLogin(String login) {
        if (LordRepository.Companion.getInstance().isExistsLogin(login)) {
            return true;
        }
        return false;
    }

    private static boolean isEmail(String email) {
        if (LordRepository.Companion.getInstance().isExistsEmail(email)) {
            return true;
        }
        return false;
    }

}

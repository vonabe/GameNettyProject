package ru.vonabe.manager;

import ru.vonabe.entitys.EntityLord;
import ru.vonabe.repository.LordRepository;

public class AuthorizationManager {

    final private static String SQL_AUTHO = "select Lord.login, password from Lord where Lord.login = '%1$s'";

    public static boolean authorization(String login, String password) {
        EntityLord auth = LordRepository.Companion.getInstance().findByLoginAndPassword(login, password);
        return auth != null;
//        DataBaseManager db = DataBaseManager.getDB();
//        ResultSet result = db.query(String.format(SQL_AUTHO, login));
//        try {
//            if (!result.next())
//                return false;
//            String r_login = result.getString("login");
//            String r_password = result.getString("password");
//            if (login.equals(r_login) && password.equals(r_password)) {
//                return true;
//            }
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            // System.out.println(result.);
//            e.printStackTrace();
//        }
//        // if (login.equals("vonabe") && password.equals("qwerty")) {
//        // return true;
//        // } else {
//        // return false;
//        // }
//        return false;
    }

}

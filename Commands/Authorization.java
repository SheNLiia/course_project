package Commands;

import Server.PackageData;
import DataBase.ConnectionDB;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;

public class Authorization implements Command {

    @Override
    public PackageData work(PackageData elem) {
        try {
            ResultSet resultSet = ConnectionDB.getINSTANCE().select("SELECT * FROM Users WHERE login = '" + elem.getLogin() + "';", elem.getLogin(), elem.getElem());
            if (resultSet.next()) {
                MessageDigest md = MessageDigest.getInstance("MD2");
                String pepper = "qwerty";
                int salt = resultSet.getInt("salt");
                String password = Arrays.toString(md.digest((pepper + elem.getPassword() + salt).getBytes(StandardCharsets.UTF_8)));

                if (!password.equals(resultSet.getString("password"))) {
                    elem.setResult("Неверный логин или пароль");
                    return elem;
                }
                int session = new Random().nextInt();
                elem.setSession(session);
                Commander.getINSTANCE().getSessions().put(session, elem.getLogin());
                elem.setAdmin(resultSet.getBoolean("admin"));
                elem.setResult("Успешная авторизация");
            } else {
                elem.setResult("Неверный логин или пароль");
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            elem.setResult("Неверный логин или пароль");
        }
        return elem;
    }
}

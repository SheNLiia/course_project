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

public class Registration implements Command {

    @Override
    public PackageData work(PackageData elem) {
        try {
//            System.out.println(elem.getSex());
            ResultSet resultSet = ConnectionDB.getINSTANCE().selectRegistration("SELECT * FROM Users WHERE login = '" + elem.getLogin() + "';");
            if (resultSet.next()) {
                elem.setResult("Такой логин уже занят");
            } else {
                MessageDigest md = MessageDigest.getInstance("MD2");
                String pepper = "qwerty";
                int salt = new Random().nextInt();
                String password = Arrays.toString(md.digest((pepper + elem.getPassword() + salt).getBytes(StandardCharsets.UTF_8)));

                ConnectionDB.getINSTANCE().execute("INSERT INTO Users(login, password, salt, sex) VALUES ('"
                        + elem.getLogin() + "', '"
                        + password + "', '"
                        + salt + "', '"
                        + elem.getSex() + "');"
                        , elem.getLogin(), elem.getElem());
                int session = new Random().nextInt();
                elem.setSession(session);
                Commander.getINSTANCE().getSessions().put(session, elem.getLogin());
                elem.setResult("Пользователь успешно создан");
            }
        } catch (NoSuchAlgorithmException | SQLException e) {
            throw new RuntimeException(e);
        }
        return elem;
    }
}

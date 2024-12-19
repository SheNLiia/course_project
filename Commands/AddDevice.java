package Commands;

import Server.PackageData;
import DataBase.ConnectionDB;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddDevice implements Command {

    @Override
    public PackageData work(PackageData elem) {
        ResultSet rs;
        try {
            rs = ConnectionDB.getINSTANCE().select("SELECT * FROM Devices WHERE NAME = '" + elem.getDevice() + "';", elem.getLogin(), elem.getElem());
            if (rs.next()) {
                ConnectionDB.getINSTANCE().execute("INSERT INTO users_devices VALUES ('" + elem.getLogin() + "', '" + rs.getInt("ID") + "');", elem.getLogin(), elem.getElem());
                elem.setResult("Новое устройство успешно добавлено");
            } else {
                elem.setResult("Устройство не найдено");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return elem;
    }
}

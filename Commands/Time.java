package Commands;

import Server.PackageData;
import DataBase.ConnectionDB;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Time implements Command {
    @Override
    public PackageData work(PackageData elem) {
        try {
            ResultSet resultSet = ConnectionDB.getINSTANCE().select("SELECT * FROM users_devices WHERE login_u = '" + elem.getLogin() + "' and id_d = ANY(" +
                    "SELECT id FROM devices WHERE time_can_show = True);", elem.getLogin(), elem.getElem());
            if (resultSet.next()) {
                elem.setResponse(Response.time);
                elem.setResult("Всё успешно");
            } else {
                elem.setResult("Не найдено устройств для выполнения");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return elem;
    }
}

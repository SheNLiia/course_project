package Commands;

import Server.PackageData;
import DataBase.ConnectionDB;
import java.sql.SQLException;

public class Admin implements Command {
    @Override
    public PackageData work(PackageData elem) {
        try {
            ConnectionDB.getINSTANCE().execute(elem.getQuery(), elem.getLogin(), elem.getElem());
        } catch (SQLException e) {
            System.out.println("Неверный запрос");
        }
        return elem;
    }
}

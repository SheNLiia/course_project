package Commands;

import Server.PackageData;

import java.sql.SQLException;

public interface Command {
    PackageData work(PackageData elem) throws SQLException;
}
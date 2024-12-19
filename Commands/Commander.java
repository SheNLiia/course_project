package Commands;

import Server.PackageData;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

public class Commander {
    private LinkedHashMap<Integer, String> sessions = new LinkedHashMap<>();
    private static Commander INSTANCE;

    public static Commander getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new Commander();
        return INSTANCE;
    }

    public LinkedHashMap<Integer, String> getSessions() {
        return sessions;
    }

    private final HashMap<Elem, Command> hashMap = new HashMap<>() {{
        put(Elem.thermohygrometer, new Thermohygrometer());
        put(Elem.heater, new Heater());
        put(Elem.conditioner, new Conditioner());
        put(Elem.registration, new Registration());
        put(Elem.authorization, new Authorization());
        put(Elem.humidifier, new Humidifier());
        put(Elem.fan, new Fan());
        put(Elem.robot_cleaner, new RobotCleaner());
        put(Elem.time, new Time());
        put(Elem.pollution, new Pollution());
        put(Elem.addDevice, new AddDevice());
        put(Elem.admin, new Admin());
    }};


    public PackageData runCommand(PackageData elem) {
        // Получаем команду из hashMap по ключу, которым является elem.getElem()
        Command command = hashMap.get(elem.getElem());

        // Проверяем, существует ли команда
        if (command != null) {
            // Проверяем, что команда не является регистрацией или авторизацией
            if (elem.getElem() != Elem.registration && elem.getElem() != Elem.authorization) {
                // Проверяем, что текущая сессия существует и соответствует логину пользователя
                if (!(sessions.containsKey(elem.getSession()) && Objects.equals(sessions.get(elem.getSession()), elem.getLogin()))) {
                    // Устанавливаем сообщение об ошибке сессии и возвращаем элемент
                    elem.setResult("Ошибка сессии");
                    return elem;
                }
            }
            try {
                // Выполняем команду и обновляем elem с результатом выполнения
                elem = command.work(elem);
            } catch (SQLException e) {
                // В случае исключения SQL выбрасываем RuntimeException
                System.out.println("Неверный запрос");
            }
        } else {
            // Если команда не найдена, устанавливаем результат как "Неизвестная команда"
            elem.setResult("Неизвестная команда");
        }
        // Возвращаем обработанный элемент
        return elem;
    }

}


package main.java.locale;

import java.util.ListResourceBundle;

public class Resource_ru extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {"gameArea", "Игровое поле"},
                {"logWindow", "Протокол работы"},
                {"startMessage", "Протокол работает"},
                {"accept", "Да"},
                {"dispose", "Нет"},
                {"exitMessage", "Закрыть окно?"},
                {"exit", "Выход"},
                {"exitButton", "Выход из приложения"},
                {"mode", "Режим отображения"},
                {"system", "Системная схема"},
                {"universal", "Универсальная схема"},
                {"lang", "Язык"},
                {"tests", "Тесты"},
                {"logMessage", "Сообщение в лог"},
                {"testMessage", "Новая строка"},
                {"robotCoordsWindow", "Координаты робота"},
                {"coordinates", "Координаты робота"}};
    }
}
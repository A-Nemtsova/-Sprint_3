package ru.yandex.practicum.scooter.api.model;

public class RequestCourierLoginData {
    private String login;
    private String password;

    public RequestCourierLoginData(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

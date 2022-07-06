package ru.yandex.practicum.scooter.api.model;

public class CourierId {
    private String courierId;

    public CourierId(String courierId) {
        this.courierId = courierId;
    }

    public String getCourierId() {
        return courierId;
    }

    public void setCourierId(String courierId) {
        this.courierId = courierId;
    }
}

package ru.yandex.practicum.scooter.api.model;

import java.util.List;

public class ResponseOrdersListData {
    private List<OrderInfoInList> orders;
    private PageInfo pageInfo;
    private List<AvailiblePlaceData> availableStations;

    public List<OrderInfoInList> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderInfoInList> orders) {
        this.orders = orders;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<AvailiblePlaceData> getAvailableStations() {
        return availableStations;
    }

    public void setAvailableStations(List<AvailiblePlaceData> availableStations) {
        this.availableStations = availableStations;
    }
}

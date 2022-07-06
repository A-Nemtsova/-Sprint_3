package ru.yandex.practicum.scooter.api;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import ru.yandex.practicum.scooter.api.client.OrderClient;
import ru.yandex.practicum.scooter.api.model.ResponseOrdersListData;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;

@Story("API для получения списка заказа")
public class GetOrdersListTest {
    OrderClient orderClient;

    @Test
    @DisplayName("Получение списка заказов при отправке запроса без параметров.")
    @Description("Проверка, что при отправке запроса без параметров, в ответ возвращается страница № 0 с 30 заказами (парамеры по умолчанию)")
    public void getOrderListWithoutParamsPositive () {
        orderClient = new OrderClient();
        Response responseGetOrdersList = orderClient.getOrderListWithoutParams(null, null, null, null);
        assertEquals(SC_OK, responseGetOrdersList.statusCode());
        //проверка, что возвращается не пустой объект
        assertNotNull(responseGetOrdersList);
        ResponseOrdersListData responseOrdersListData = responseGetOrdersList.as(ResponseOrdersListData.class);
        //проверка, что в возвращается страница по умолчанию (0)
        assertEquals(0, responseOrdersListData.getPageInfo().getPage());
        //проверка, что в возвращается количество заказов на странице по умолчанию (30)
        assertEquals(30, responseOrdersListData.getPageInfo().getLimit());
        //проверка, что в заказе заполнен id
        assertNotNull(responseOrdersListData.getOrders().get(0).getId());
        //проверка, что в ответе действительно 30 заказов
        assertEquals(30, responseOrdersListData.getOrders().size());
    }

}

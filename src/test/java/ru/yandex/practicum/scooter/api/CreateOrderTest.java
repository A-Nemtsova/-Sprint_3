package ru.yandex.practicum.scooter.api;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practicum.scooter.api.client.OrderClient;
import ru.yandex.practicum.scooter.api.model.RequestOrderData;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Story("API для создания заказа")
@RunWith(Parameterized.class)
public class CreateOrderTest {
    OrderClient orderClient;
    String trackId;

    private final String firstName = "Naruto";
    private final String lastName = "Uchiha";
    private final String address = "Konoha, 142 apt.";
    private final String metroStation = "4";
    private final String phone = "+7 800 355 35 35";
    private final int rentTime = 1;
    private final String deliveryDate = "2022-07-06";
    private final String comment = "Saske, come back to Konoha";
    private final String[] colors;

    public CreateOrderTest(String[] colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters // добавили аннотацию
    public static Object[][] getColorData() {
        return new Object[][]{
                {new String[]{"BLACK", "GREY"}},
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{}},
        };
    }

    @Before
    public void init() {
        orderClient = new OrderClient();
    }

    @After
    public void clear() {
        orderClient.deleteOrder(trackId);
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверка, что при ууспешном  создании заказа приходит 201 статус-код и в ответе приходит track")
    public void createOrderParametersPositiveResult () {
        RequestOrderData orderData = new RequestOrderData(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, colors);
        Response responseOrder = orderClient.createOrder(orderData);
        assertEquals(SC_CREATED, responseOrder.statusCode());
        assertNotNull(responseOrder.body().jsonPath().getString("track"));
        trackId = orderClient.getTrackFromResponseCreateOrder(responseOrder);
    }
}

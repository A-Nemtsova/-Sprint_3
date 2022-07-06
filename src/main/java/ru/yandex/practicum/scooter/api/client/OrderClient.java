package ru.yandex.practicum.scooter.api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.scooter.api.model.RequestOrderData;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseApiClient {

    @Step("Создание заказа с цветом самоката {orderData.colors}")
    public Response createOrder(RequestOrderData orderData) {
        return given()
                .spec(getRequestSpecForJson())
                .body(orderData)
                .when()
                .post(BASE_URL + "/api/v1/orders");
    }

    @Step("Получение номера заказа")
    public String getTrackFromResponseCreateOrder (Response responseCreateOrder) {
        return responseCreateOrder.body().jsonPath().getString("track");
    }

    /*Метод реализован согласно документации.
    При прверке метода в Postman ни разу не удалось получить успешный ответ.
    При отправке существующего track всегда приходил 400 статус-код: "message": "Недостаточно данных для поиска".
    Поэтому считаю, что данное api не работоспособно.
    Так как по условиям задания необходимо удалять данные, которые сгенерированы при прохождении тестов, поэтому и был создан этот метод.
     */

    @Step("Отмена заказа")
    public Response deleteOrder (String trackId) {
        return given()
                .spec(getRequestSpecForJson())
                .body("{\"track\": " + trackId + "}")
                .when()
                .delete( BASE_URL + "/api/v1/orders/cancel");
    }


    @Step("Получение списка заказов")
    public Response getOrderListWithoutParams(Integer courierId, String nearestStation, Integer limit, Integer page) {
        return given()
                .spec(getRequestSpecForQuery())
                .when()
                .queryParam("courierId", courierId)
                .queryParam("nearestStation", nearestStation)
                .queryParam("limit", limit)
                .queryParam("page", page)
                //В документаци и указан неправильное api "/v1/orders". Правильно будет "/api/v1/orders"
                .get(BASE_URL + "/api/v1/orders");
    }

}

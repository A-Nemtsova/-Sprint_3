package ru.yandex.practicum.scooter.api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.scooter.api.model.RequestCourierLoginData;
import ru.yandex.practicum.scooter.api.model.CourierId;
import ru.yandex.practicum.scooter.api.model.RequestCourierRegistrationData;

import static io.restassured.RestAssured.given;

public class CourierClient extends BaseApiClient {

    @Step("Добавление курьера")
    public Response addCourier (RequestCourierRegistrationData courierRegistrationData) {
        return given()
                .spec(getRequestSpecForJson())
                .body(courierRegistrationData)
                .when()
                .post(BASE_URL + "/api/v1/courier/");
    }

    @Step("Логин курьера")
    public Response loginCourier(RequestCourierLoginData requestCourierLoginData) {
        return given()
                .spec(getRequestSpecForJson())
                .body(requestCourierLoginData)
                .when()
                .post(BASE_URL + "/api/v1/courier/login");
    }

    @Step("Получение id курьера")
    public String getCourierIdFromResponseCourierData (RequestCourierLoginData requestCourierLoginData) {
        return loginCourier(requestCourierLoginData).body().jsonPath().getString("id");
    }

    @Step("Удаление курьера")
    public Response deleteCourier (CourierId courierId) {
        return given()
                .spec(getRequestSpecForJson())
                .body(courierId)
                .when()
                .delete( BASE_URL + "/api/v1/courier/" + courierId.getCourierId());
    }

}

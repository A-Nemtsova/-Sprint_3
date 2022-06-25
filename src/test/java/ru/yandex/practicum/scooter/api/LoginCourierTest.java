package ru.yandex.practicum.scooter.api;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.scooter.api.client.CourierClient;
import ru.yandex.practicum.scooter.api.model.RequestCourierLoginData;
import ru.yandex.practicum.scooter.api.model.CourierId;
import ru.yandex.practicum.scooter.api.model.RequestCourierRegistrationData;
import ru.yandex.practicum.scooter.api.model.ResponseError;

import static org.apache.http.HttpStatus.*;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.junit.Assert.*;

@Story("API для авторизации (входа) курьера")
public class LoginCourierTest {

    RequestCourierRegistrationData courierRegistrationData;
    CourierClient courierClient;
    RequestCourierLoginData requestCourierLoginData;
    CourierId courierId;

    @Before
    public void init() {
        courierRegistrationData = RequestCourierRegistrationData.getRandomCourier();
        courierClient = new CourierClient();
        courierClient.addCourier(courierRegistrationData);
    }

    @After
    public void clear() {
        requestCourierLoginData = new RequestCourierLoginData(courierRegistrationData.getLogin(), courierRegistrationData.getPassword());
        Response responseLoginCourier = courierClient.loginCourier(requestCourierLoginData);
        if (responseLoginCourier.statusCode() == 200) {
            courierId = new CourierId(courierClient.getCourierIdFromResponseCourierData(requestCourierLoginData));
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Логин курьера")
    @Description("Проверка, что курьер может авторизоваться")
    public void loginCourierAutorizateTrue() {
        requestCourierLoginData = new RequestCourierLoginData(courierRegistrationData.getLogin(), courierRegistrationData.getPassword());
        Response responseLoginCourier = courierClient.loginCourier(requestCourierLoginData);
        assertEquals(SC_OK, responseLoginCourier.statusCode());
    }

    @Test
    @DisplayName("Логин курьера. При успешной авторизации возвращается непустое id")
    @Description("Проверка, что при успешной авторизации возвращается непустое id")
    public void loginCourierCourierIdIsNotNull() {
        requestCourierLoginData = new RequestCourierLoginData(courierRegistrationData.getLogin(), courierRegistrationData.getPassword());
        assertNotNull(courierClient.getCourierIdFromResponseCourierData(requestCourierLoginData));
    }

    @Test
    @DisplayName("Логин курьера. Невозможно авторизоваться без логина и пароля")
    @Description("Чтобы курьер авторизовался, нужно передать в ручку все обязательные поля;")
    public void loginCourierWithoutLoginAndPasswordError() {
        requestCourierLoginData = new RequestCourierLoginData("", "");
        Response responseLoginCourier = courierClient.loginCourier(requestCourierLoginData);
        assertNotEquals(SC_OK, responseLoginCourier.statusCode());
    }

    @Test
    @DisplayName("Логин курьера. Получение ошибки при передаче в запрос данных без логина")
    @Description("Проверка, что если нет логина, то запрос возвращает ошибку")
    public void loginCourierWithoutLoginError() {
        requestCourierLoginData = new RequestCourierLoginData(courierRegistrationData.getLogin(), courierRegistrationData.getPassword());
        requestCourierLoginData.setLogin("");
        Response responseLoginCourier = courierClient.loginCourier(requestCourierLoginData);
        assertEquals(SC_BAD_REQUEST, responseLoginCourier.statusCode());
        ResponseError responseError = responseLoginCourier.as(ResponseError.class);
        assertEquals("Недостаточно данных для входа", responseError.getMessage());
    }

    @Test
    @DisplayName("Логин курьера. Получение ошибки при передаче в запрос данных без пароля")
    @Description("Проверка, что если нет пароля, то запрос возвращает ошибку")
    public void loginCourierWithoutPasswordError() {
        requestCourierLoginData = new RequestCourierLoginData(courierRegistrationData.getLogin(), courierRegistrationData.getPassword());
        requestCourierLoginData.setPassword("");
        Response responseLoginCourier = courierClient.loginCourier(requestCourierLoginData);
        assertEquals(SC_BAD_REQUEST, responseLoginCourier.statusCode());
        ResponseError responseError = responseLoginCourier.as(ResponseError.class);
        assertEquals("Недостаточно данных для входа", responseError.getMessage());
    }

    @Test
    @DisplayName("Логин курьера.  Получение ошибки при неверном логине")
    @Description("Проверка, что если неверный логин, то запрос возвращает ошибку")
    public void loginCourierWithWrongLoginError() {
        requestCourierLoginData = new RequestCourierLoginData(courierRegistrationData.getLogin() + "001", courierRegistrationData.getPassword());
        Response responseLoginCourier = courierClient.loginCourier(requestCourierLoginData);
        assertEquals(SC_NOT_FOUND, responseLoginCourier.statusCode());
        ResponseError responseError = responseLoginCourier.as(ResponseError.class);
        assertEquals("Учетная запись не найдена", responseError.getMessage());
    }

    @Test
    @DisplayName("Логин курьера. Получение ошибки при неверном пароле")
    @Description("Проверка, что если неверный пароль, то запрос возвращает ошибку")
    public void loginCourierWithWrongPasswordError() {
        requestCourierLoginData = new RequestCourierLoginData(courierRegistrationData.getLogin(), courierRegistrationData.getPassword() + "001");
        Response responseLoginCourier = courierClient.loginCourier(requestCourierLoginData);
        assertEquals(SC_NOT_FOUND, responseLoginCourier.statusCode());
        ResponseError responseError = responseLoginCourier.as(ResponseError.class);
        assertEquals("Учетная запись не найдена", responseError.getMessage());
    }

    @Test
    @DisplayName("Логин курьера. Получение ошибки при авторизации с несуществующими логином и паролем")
    @Description("Проверка, что если авторизоваться с несуществующим логином и паролем, то запрос возвращает ошибку. Реализовано через создание и удаление курьера, что бы искючить возможность существования курьера со сгенерированными данными")
    //реализовано через создание и удаление курьера, что бы искючить возможность существования курьера со сгенерированными данными
    public void loginCourierWithWrongCourierError() {
        requestCourierLoginData = new RequestCourierLoginData(courierRegistrationData.getLogin(), courierRegistrationData.getPassword());
        courierClient.loginCourier(requestCourierLoginData);
        courierId = new CourierId(courierClient.getCourierIdFromResponseCourierData(requestCourierLoginData));
        courierClient.deleteCourier(courierId);
        Response responseLoginCourier = courierClient.loginCourier(requestCourierLoginData);
        assertEquals(SC_NOT_FOUND, responseLoginCourier.statusCode());
        ResponseError responseError = responseLoginCourier.as(ResponseError.class);
        assertEquals("Учетная запись не найдена", responseError.getMessage());
    }
}

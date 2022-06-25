package ru.yandex.practicum.scooter.api;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.scooter.api.client.CourierClient;
import ru.yandex.practicum.scooter.api.model.*;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

@Story("API для создания курьера")
public class CreateCourierTest {
    RequestCourierRegistrationData courierRegistrationData;
    CourierClient courierClient;
    RequestCourierLoginData requestCourierLoginData;
    CourierId courierId;

    @Before
    @Step("Подготовка тестовых данных курьера")
    public void init () {
        courierRegistrationData = RequestCourierRegistrationData.getRandomCourier();
        courierClient = new CourierClient();
    }

    @After
    @Step("Удаление созданного курьера")
    public void clear() {
        requestCourierLoginData = new RequestCourierLoginData(courierRegistrationData.getLogin(), courierRegistrationData.getPassword());
        Response responseLoginCourier = courierClient.loginCourier(requestCourierLoginData);
        if (responseLoginCourier.statusCode() == 200) {
            courierId = new CourierId(courierClient.getCourierIdFromResponseCourierData(requestCourierLoginData));
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Создание курьера. При успешном создании курьера приходит 201 статус-код")
    @Description("Проверка, что при успешном создании курьера приходит 201 статус-код")
    public void addCourierStatusCodePositiveTest() {
        Response responseAddCourier = courierClient.addCourier(courierRegistrationData);
        assertEquals(SC_CREATED, responseAddCourier.statusCode());
    }

    @Test
    @DisplayName("Создание курьера. При успешном создании курьера в ответе приходит true")
    @Description("Проверка, что при успешном создании курьера в ответе приходит true")
    public void addCourierCheckMessagePositiveTest() {
        Response responseAddCourier = courierClient.addCourier(courierRegistrationData);
        ResponseCreateCourierOk responseCreateCourierOk = responseAddCourier.as(ResponseCreateCourierOk.class);
        assertTrue(responseCreateCourierOk.isOk());
    }

    @Test
    @DisplayName("Создание курьера. Созданный курьер может залогиниться")
    @Description("Проверка, что курьер создался и он может залогиниться")
    public void addAndLoginCourier () {
        courierClient.addCourier(courierRegistrationData);
        requestCourierLoginData = new RequestCourierLoginData(courierRegistrationData.getLogin(), courierRegistrationData.getPassword());
        Response responseLoginCourier = courierClient.loginCourier(requestCourierLoginData);
        assertEquals(SC_OK, responseLoginCourier.statusCode());
    }

    @Test
    @DisplayName("Создание курьера. Нельзя создать двух одинаковых курьеров")
    @Description("Проверка, что нельзя создать двух одинаковых курьеров")
    public void addCourierCreatingTwoIdenticalCouriersError () {
        courierClient.addCourier(courierRegistrationData);
        Response responseAddCourier = courierClient.addCourier(courierRegistrationData);
        assertNotEquals(SC_CREATED, responseAddCourier.statusCode());
    }

    @Test
    @DisplayName("Создание курьера. Нельзя создать курьера с неуникальным логином")
    @Description("Проверка, что если создать курьера с логином, который уже есть, возвращается ошибка. Проверка реализована согласно документации. По документации текст ошибки \"Этот логин уже используется\", но при проверке через Postman текст ошибки \"Этот логин уже используется. Попробуйте другой.\"")
    //Проверка реализована согласно документации
    //По документации текст ошибки "Этот логин уже используется", но при проверке через Postman текст ошибки "Этот логин уже используется. Попробуйте другой."
    public void addCourierWithIdenticalLoginError () {
        RequestCourierRegistrationData courierRegistrationDataNew = RequestCourierRegistrationData.getRandomCourier();
        courierRegistrationDataNew.setLogin(courierRegistrationData.getLogin());
        courierClient.addCourier(courierRegistrationData);
        Response responseAddCourier = courierClient.addCourier(courierRegistrationDataNew);
        assertEquals(SC_CONFLICT, responseAddCourier.statusCode());
        ResponseError responseError = responseAddCourier.as(ResponseError.class);
        assertEquals("Этот логин уже используется", responseError.getMessage());
    }

    @Test
    @DisplayName("Создание курьера. Курьер создается, если переданы все обязательные поля")
    @Description("Чтобы создать курьера, нужно передать в ручку все обязательные поля")
    //В документации указана обязательность всех параметров, поэтому за корректное поведение принято поведение описаноое в документации
    public void addCourierWithoutLoginAndPasswordError () {
        courierRegistrationData.setLogin("");
        courierRegistrationData.setPassword("");
        courierRegistrationData.setFirstName("");
        Response responseAddCourier = courierClient.addCourier(courierRegistrationData);
        assertNotEquals(SC_CREATED, responseAddCourier.statusCode());
    }

    @Test
    @DisplayName("Создание курьера. Если не передается логин, то запрос возвращает ошибку")
    @Description("Проверка, что если нет логина, то запрос возвращает ошибку")
    public void addCourierWithoutLoginError () {
        courierRegistrationData.setLogin("");
        Response responseAddCourier = courierClient.addCourier(courierRegistrationData);
        assertEquals(SC_BAD_REQUEST, responseAddCourier.statusCode());
        ResponseError responseError = responseAddCourier.as(ResponseError.class);
        assertEquals("Недостаточно данных для создания учетной записи", responseError.getMessage());
    }

    @Test
    @DisplayName("Создание курьера. Если не передается пароль, то запрос возвращает ошибку")
    @Description("Проверка, что если нет пароля, то запрос возвращает ошибку")
    public void addCourierWithoutPasswordError () {
        courierRegistrationData.setPassword("");
        Response responseAddCourier = courierClient.addCourier(courierRegistrationData);
        assertEquals(SC_BAD_REQUEST, responseAddCourier.statusCode());
        ResponseError responseError = responseAddCourier.as(ResponseError.class);
        assertEquals("Недостаточно данных для создания учетной записи", responseError.getMessage());
    }


    @Test
    @DisplayName("Создание курьера. Если не передается Имя, то запрос возвращает ошибку")
    @Description("Проверка, что если нет firstName, то запрос возвращает ошибку. Проверка реализована согласно документации. При проверке метода через Postman без firstName курьер успешно создается, что не соответствует документации")
    //Проверка реализована согласно документации
    // При проверке метода через Postman курьер без firstName успешно создается, что не соответствует документации
    public void addCourierWithoutFirstNameOk () {
        courierRegistrationData.setFirstName("");
        Response responseAddCourier = courierClient.addCourier(courierRegistrationData);
        assertEquals(SC_BAD_REQUEST, responseAddCourier.statusCode());
        ResponseError responseError = responseAddCourier.as(ResponseError.class);
        assertEquals("Недостаточно данных для создания учетной записи", responseError.getMessage());
    }
}

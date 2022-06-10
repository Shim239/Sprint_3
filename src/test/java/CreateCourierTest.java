import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import ya.sprint3.api.ApiSettings;
import ya.sprint3.api.HttpStatus;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.junit.Before;
import org.junit.Test;
import ya.sprint3.stepsfortest.CourierSteps;
import ya.sprint3.objects.Courier;

import static org.junit.Assert.*;

public class CreateCourierTest {

    Courier courier;
    ApiSettings apiSettings = new ApiSettings();
    CourierSteps courierSteps = new CourierSteps();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        courier = courierSteps.createCourierWithRandomParameters();
        apiSettings.pingServer();
    }

    @Test
    @DisplayName("Тест корректности работы запроса создания нового курьера")
    @Description("Тест корректности работы запроса создания нового курьера")
    public void createNewCourier() {
        ValidatableResponse response = courierSteps.createNewCourier(courier);
        assertEquals(HttpStatus.CREATED.getValue(), response.extract().statusCode());
        assertTrue(response.extract().body().path("ok"));
    }

    @Test
    @DisplayName("Невозможно создать двух одинаковых курьеров")
    @Description("Невозможно создать двух одинаковых курьеров")
    public void checkWhatDoNotPossibleCreateTwoSameCouriers() {
        ValidatableResponse creatingFirstCourier = courierSteps.createNewCourier(courier);
        assertEquals(HttpStatus.CREATED.getValue(), creatingFirstCourier.extract().statusCode());
        ValidatableResponse creatingSecondCourier = courierSteps.createNewCourier(courier);
        assertEquals(HttpStatus.CONFLICT.getValue(), creatingSecondCourier.extract().statusCode());
    }

    @Test
    @DisplayName("Проверка невозможности создания курьера без логина")
    @Description("Проверка невозможности создания курьера без логина")
    public void checkPossibleCreateCourierWithoutLogin() {
        courier.setLogin(null);
        ValidatableResponse creatingCourierWithoutLogin = courierSteps.createNewCourier(courier);
        assertEquals(HttpStatus.BAD_REQUEST.getValue(), creatingCourierWithoutLogin.extract().statusCode());
        assertEquals("Недостаточно данных для создания учетной записи", creatingCourierWithoutLogin.extract().body().path("message"));
    }

    @Test
    @DisplayName("Проверка невозможности создания курьера без пароля")
    @Description("Проверка невозможности создания курьера без пароля")
    public void checkPossibleCreateCourierWithoutPassword() {
        courier.setPassword(null);
        ValidatableResponse creatingCourierWithoutPassword = courierSteps.createNewCourier(courier);
        assertEquals(HttpStatus.BAD_REQUEST.getValue(), creatingCourierWithoutPassword.extract().statusCode());
        assertEquals("Недостаточно данных для создания учетной записи", creatingCourierWithoutPassword.extract().body().path("message"));
    }

    @Test
    @DisplayName("Нельзя создать курьера с уже используемым логином")
    @Description("Нельзя создать курьера с уже используемым логином")
    public void doNotPossibleCreateCourierWithAlreadyExistingLogin() {
        ValidatableResponse createFirstCourier = courierSteps.createNewCourier(courier);
        assertEquals(HttpStatus.CREATED.getValue(), createFirstCourier.extract().statusCode());

        ValidatableResponse createCourierWithExistLogin = courierSteps.createNewCourier(courier);
        assertEquals(createCourierWithExistLogin.extract().statusCode(), HttpStatus.CONFLICT.getValue());
        assertEquals("Этот логин уже используется.", createCourierWithExistLogin.extract().body().path("message"));
    }

    @After
    @DisplayName("Очищаем БД, если были созданы данные")
    @Description("Очищаем БД, если были созданы данные")
    public void clearData() {
        if (courierSteps.checkCourierExists(courier.getLogin(), courier.getPassword())) {
            courierSteps.deleteCourier(courier);
        }
        assertFalse(courierSteps.checkCourierExists(courier.getLogin(), courier.getPassword()));
    }

}

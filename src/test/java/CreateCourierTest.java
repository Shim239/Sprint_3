import io.qameta.allure.junit4.DisplayName;
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

    ApiSettings apiSettings = new ApiSettings();
    CourierSteps courierSteps = new CourierSteps();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Тест корректности работы запроса создания нового курьера")
    @Description("Тест корректности работы запроса создания нового курьера")
    public void createNewCourier() {
        apiSettings.pingServer();
        Courier courier;
        courier = courierSteps.createCourierWithRandomParameters();
        ValidatableResponse response = courierSteps.createNewCourier(courier);
        assertEquals(response.extract().statusCode(), HttpStatus.CREATED.getValue());
        assertTrue(response.extract().body().path("ok"));
    }

    @Test
    @DisplayName("Невозможно создать двух одинаковых курьеров")
    @Description("Невозможно создать двух одинаковых курьеров")
    public void checkWhatDoNotPossibleCreateTwoSameCouriers() {
        apiSettings.pingServer();
        Courier courier;
        courier = courierSteps.createCourierWithRandomParameters();
        ValidatableResponse creatingFirstCourier = courierSteps.createNewCourier(courier);
        assertEquals(creatingFirstCourier.extract().statusCode(), HttpStatus.CREATED.getValue());
        ValidatableResponse creatingSecondCourier = courierSteps.createNewCourier(courier);
        assertEquals(creatingSecondCourier.extract().statusCode(), HttpStatus.CONFLICT.getValue());
    }

    @Test
    @DisplayName("Проверка невозможности создания курьера без логина")
    @Description("Проверка невозможности создания курьера без логина")
    public void checkPossibleCreateCourierWithoutLogin() {
        apiSettings.pingServer();
        Courier courierWithoutLogin;
        courierWithoutLogin = courierSteps.createCourierWithRandomParameters();
        courierWithoutLogin.setLogin(null);
        ValidatableResponse creatingCourierWithoutLogin = courierSteps.createNewCourier(courierWithoutLogin);
        assertEquals(creatingCourierWithoutLogin.extract().statusCode(), HttpStatus.BAD_REQUEST.getValue());
        assertEquals(creatingCourierWithoutLogin.extract().body().path("message"), "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Проверка невозможности создания курьера без пароля")
    @Description("Проверка невозможности создания курьера без пароля")
    public void checkPossibleCreateCourierWithoutPassword() {
        apiSettings.pingServer();
        Courier courierWithoutPassword;
        courierWithoutPassword = courierSteps.createCourierWithRandomParameters();
        courierWithoutPassword.setPassword(null);
        ValidatableResponse creatingCourierWithoutPassword = courierSteps.createNewCourier(courierWithoutPassword);
        assertEquals(creatingCourierWithoutPassword.extract().statusCode(), HttpStatus.BAD_REQUEST.getValue());
        assertEquals(creatingCourierWithoutPassword.extract().body().path("message"), "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Нельзя создать курьера с уже используемым логином")
    @Description("Нельзя создать курьера с уже используемым логином")
    public void doNotPossibleCreateCourierWithAlreadyExistingLogin() {
        apiSettings.pingServer();
        Courier courier;
        courier = courierSteps.createCourierWithRandomParameters();
        ValidatableResponse createFirstCourier = courierSteps.createNewCourier(courier);
        assertEquals(createFirstCourier.extract().statusCode(), HttpStatus.CREATED.getValue());

        ValidatableResponse createCourierWithExistLogin = courierSteps.createNewCourier(courier);
        assertEquals(createCourierWithExistLogin.extract().statusCode(), HttpStatus.CONFLICT.getValue());
        assertEquals(createCourierWithExistLogin.extract().body().path("message"), "Этот логин уже используется.");

        courierSteps.deleteCourier(courier);
    }

}

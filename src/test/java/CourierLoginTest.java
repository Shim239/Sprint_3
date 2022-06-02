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

public class CourierLoginTest {
    ApiSettings apiSettings = new ApiSettings();
    CourierSteps courierSteps = new CourierSteps();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    @Description("Курьер может авторизоваться")
    public void checkCourierCanLogIn() {
        apiSettings.pingServer();
        Courier courier;
        courier = courierSteps.createCourierWithRandomParameters();
        ValidatableResponse createNewCourierResponse = courierSteps.createNewCourier(courier);
        assertEquals(createNewCourierResponse.extract().statusCode(), HttpStatus.CREATED.getValue());

        ValidatableResponse logInCourier = courierSteps.loginCourier(courier.getLogin(), courier.getPassword());
        assertEquals(logInCourier.extract().statusCode(), HttpStatus.OK.getValue());
        int courierId = logInCourier.extract().body().path("id");
        assertTrue(courierId > 0);

        courierSteps.deleteCourier(courier);
    }

    @Test
    @DisplayName("Попытка авторизации курьера без логина")
    @Description("Попытка авторизации курьера без логина")
    public void checkCourierLogInWithoutLogin() {
        apiSettings.pingServer();
        Courier courier;
        courier = courierSteps.createCourierWithRandomParameters();

        courier.setLogin(null);
        ValidatableResponse logInCourierWithoutData = courierSteps.loginCourier(courier.getLogin(), courier.getPassword());
        assertEquals(logInCourierWithoutData.extract().statusCode(), HttpStatus.BAD_REQUEST.getValue());
        assertEquals(logInCourierWithoutData.extract().body().path("message"), "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Попытка авторизации курьера без пароля")
    @Description("Попытка авторизации курьера без пароля")
    public void checkCourierLogInWithoutPassword() {
        apiSettings.pingServer();
        Courier courier;
        courier = courierSteps.createCourierWithRandomParameters();

        courier.setPassword(null);
        ValidatableResponse logInCourierWithoutData = courierSteps.loginCourier(courier.getLogin(), courier.getPassword());
        assertEquals(logInCourierWithoutData.extract().statusCode(), HttpStatus.BAD_REQUEST.getValue());
        assertEquals(logInCourierWithoutData.extract().body().path("message"), "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Попытка авторизации несуществующего курьера")
    @Description("Попытка авторизации несуществующего курьера")
    public void checkCourierAuthWithIncorrectLogin() {
        apiSettings.pingServer();
        Courier courier;
        courier = courierSteps.createCourierWithRandomParameters();
        assertFalse(courierSteps.checkCourierExists(courier.getLogin(), courier.getPassword()));
        ValidatableResponse response = courierSteps.loginCourier(courier.getLogin(), courier.getPassword());
        assertEquals(response.extract().statusCode(), HttpStatus.NOT_FOUND.getValue());
        assertEquals(response.extract().body().path("message"), "Учетная запись не найдена");
    }
}

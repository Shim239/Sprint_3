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

public class CourierLoginTest {
    Courier courier;
    ApiSettings apiSettings = new ApiSettings();
    CourierSteps courierSteps = new CourierSteps();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        apiSettings.pingServer();
        courier = courierSteps.createCourierWithRandomParameters();
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    @Description("Курьер может авторизоваться")
    public void checkCourierCanLogIn() {
        ValidatableResponse createNewCourierResponse = courierSteps.createNewCourier(courier);
        assertEquals(HttpStatus.CREATED.getValue(), createNewCourierResponse.extract().statusCode());

        ValidatableResponse logInCourier = courierSteps.loginCourier(courier.getLogin(), courier.getPassword());
        assertEquals(HttpStatus.OK.getValue(), logInCourier.extract().statusCode());
        int courierId = logInCourier.extract().body().path("id");
        assertTrue(courierId > 0);
    }

    @Test
    @DisplayName("Попытка авторизации курьера без логина")
    @Description("Попытка авторизации курьера без логина")
    public void checkCourierLogInWithoutLogin() {
        courier.setLogin(null);
        ValidatableResponse logInCourierWithoutData = courierSteps.loginCourier(courier.getLogin(), courier.getPassword());
        assertEquals(HttpStatus.BAD_REQUEST.getValue(), logInCourierWithoutData.extract().statusCode());
        assertEquals("Недостаточно данных для входа", logInCourierWithoutData.extract().body().path("message"));
    }

    @Test
    @DisplayName("Попытка авторизации курьера без пароля")
    @Description("Попытка авторизации курьера без пароля")
    public void checkCourierLogInWithoutPassword() {
        courier.setPassword(null);
        ValidatableResponse logInCourierWithoutData = courierSteps.loginCourier(courier.getLogin(), courier.getPassword());
        assertEquals(HttpStatus.BAD_REQUEST.getValue(), logInCourierWithoutData.extract().statusCode());
        assertEquals("Недостаточно данных для входа", logInCourierWithoutData.extract().body().path("message"));
    }

    @Test
    @DisplayName("Попытка авторизации несуществующего курьера")
    @Description("Попытка авторизации несуществующего курьера")
    public void checkCourierAuthWithIncorrectLogin() {
        assertFalse(courierSteps.checkCourierExists(courier.getLogin(), courier.getPassword()));
        ValidatableResponse response = courierSteps.loginCourier(courier.getLogin(), courier.getPassword());
        assertEquals(HttpStatus.NOT_FOUND.getValue(), response.extract().statusCode());
        assertEquals("Учетная запись не найдена", response.extract().body().path("message"));
    }

    @After
    public void clearData() {
        if (courierSteps.checkCourierExists(courier.getLogin(), courier.getPassword())) {
            courierSteps.deleteCourier(courier);
        }
        assertFalse(courierSteps.checkCourierExists(courier.getLogin(), courier.getPassword()));
    }
}

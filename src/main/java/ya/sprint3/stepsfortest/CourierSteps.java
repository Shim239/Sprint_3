package ya.sprint3.stepsfortest;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import ya.sprint3.api.CourierApi;
import ya.sprint3.api.HttpStatus;
import ya.sprint3.objects.Courier;

public class CourierSteps {
    Courier courier = new Courier();
    CourierApi courierApi = new CourierApi();

    /**
     * Метод создания объекта типа "Курьер" с параметрами: логин, пароль, имя
     *
     * @return - курьер
     */
    public Courier createCourierWithRandomParameters() {
        courier.setLogin(RandomStringUtils.randomAlphanumeric(10));
        courier.setPassword(RandomStringUtils.randomAlphanumeric(10));
        courier.setFirstName(RandomStringUtils.randomAlphanumeric(10));
        return courier;
    }

    /**
     * Метод создания нового курьера с проверками корректного создания
     *
     * @param courier - курьер (логин, пароль, имя)
     * @return - http-запрос создания нового курьера
     */
    @Step("Создаем нового курьера")
    public ValidatableResponse createNewCourier(Courier courier) {
        return courierApi.createNewCourier(courier);
    }

    /**
     * Метод логина курьера в систему
     *
     * @param login    - логин курьера
     * @param password - пароль курьера
     * @return - http-запрос логина курьера
     */
    @Step("Логинимся курьером в систему")
    public ValidatableResponse loginCourier(String login, String password) {
        Courier courier = new Courier(login, password);
        return courierApi.loginCourier(courier);
    }

    /**
     * Метод проверки наличия в БД курьера по паре "логин, пароль"
     *
     * @param login    - логин курьера
     * @param password - пароль курьера
     * @return - наличие записи в БД по параметрам курьера (true/false)
     */
    @Step("Проверяем, что курьера с переданными параметрами не существует")
    public boolean checkCourierExists(String login, String password) {
        Courier courier = new Courier(login, password);
        ValidatableResponse response = courierApi.loginCourier(courier);
        if (response.extract().statusCode() == HttpStatus.OK.getValue()) {
            System.out.println("Курьер с такими данными уже существует! Авторизуйтесь.");
            return true;
        } else {
            System.out.println("Курьера с такими данными не существует! Можно регистрироваться.");
            return false;
        }
    }

    /**
     * Метод для удаления курьера по ID
     *
     * @param courier - курьер
     * @return - http-запрос удаления курьера
     */
    @Step("Удаляем курьера")
    public ValidatableResponse deleteCourier(Courier courier) {
        int courierId = loginCourier(courier.getLogin(), courier.getPassword())
                .extract().body().path("id");
        courier.setId(String.valueOf(courierId));
        return courierApi.deleteCourier(courier);
    }
}

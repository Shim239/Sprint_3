package ya.sprint3.api;

import io.restassured.response.ValidatableResponse;
import ya.sprint3.objects.Courier;

import static io.restassured.RestAssured.given;

public class CourierApi extends ApiSettings {

    /**
     * Http-запрос формирования новой записи в БД (новый курьер)
     *
     * @param courier - курьер (логин, пароль, имя)
     * @return - http-запрос
     */
    public ValidatableResponse createNewCourier(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(api + "/courier")
                .then();
    }

    /**
     * Http-запрос логина курьера
     *
     * @param courier - курьер (логин, пароль)
     * @return - http-запрос
     */
    public ValidatableResponse loginCourier(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(api + "/courier/login")
                .then();
    }

    /**
     * Http-запрос удаления курьера
     *
     * @param courier - курьер (id)
     * @return - http-запрос
     */
    public ValidatableResponse deleteCourier(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .delete(api + "/courier/" + courier.getId())
                .then();
    }
}

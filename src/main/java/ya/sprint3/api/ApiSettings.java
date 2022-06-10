package ya.sprint3.api;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.http.ContentType.JSON;

/**
 * Основные настройки, вспомогательные запросы
 */
public class ApiSettings {
    public static final String BASE_URL = "http://qa-scooter.praktikum-services.ru";
    public static final String api = "/api/v1";

    protected RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setContentType(JSON)
                .setBaseUri(BASE_URL)
                .build();
    }

    @Step("Проверяем доступность удаленного сервера")
    public void pingServer() {
        RestAssured.when()
                .get(BASE_URL + api + "/ping")
                .then().assertThat().statusCode(200);
    }
}

package ya.sprint3.api;

import io.restassured.response.ValidatableResponse;
import ya.sprint3.objects.MetroStation;
import ya.sprint3.objects.Order;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderApi extends ApiSettings {

    /**
     * Http-запрос создания нового заказа
     *
     * @return - http-запрос
     */
    public ValidatableResponse createNewOrder(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(api + "/orders")
                .then();
    }

    /**
     * Запрос получения списка заказов
     *
     * @return
     */
    public ValidatableResponse getOrderList() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(api + "/orders")
                .then();
    }

    /**
     * Запрос получения списка станций
     *
     * @return
     */
    public HttpRequest getMetroStationList() {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + api +"/stations/search"))
                .build();
    }
}

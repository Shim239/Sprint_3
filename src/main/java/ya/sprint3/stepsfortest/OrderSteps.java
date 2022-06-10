package ya.sprint3.stepsfortest;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import ya.sprint3.api.OrderApi;
import ya.sprint3.objects.MetroStation;
import ya.sprint3.objects.Order;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class OrderSteps {

    Random random = new Random();
    OrderApi orderApi = new OrderApi();
    Gson gson = new Gson();
    HttpClient client = HttpClient.newHttpClient();

    /**
     * Формирование номера телефона
     *
     * @return - номер телефона
     */
    public String createPhoneNumber() {
        String phone = "+7";
        for (int i = 0; i < 10; i++) {
            int min = 0, max = 9;
            phone += random.nextInt((max - min) + 1 + min);
        }
        return phone;
    }

    /**
     * Метод получения списка станций
     *
     * @return - список станций
     * @throws IOException
     * @throws InterruptedException
     */
    public List<MetroStation> getMetroStationList() throws IOException, InterruptedException {
        HttpRequest request = orderApi.getMetroStationList();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String s = response.body();
        MetroStation[] metroStations = gson.fromJson(s, MetroStation[].class);
        return Arrays.asList(metroStations);
    }

    /**
     * Метод создания объекта -> Заказ с рандомизированными полями
     *
     * @return - заказ
     */
    public Order getOrderWithRandomData() throws IOException, InterruptedException {
        Random random = new Random();
        LocalDate date = LocalDate.now().plusDays(random.nextInt(7));

        String firstName = RandomStringUtils.randomAlphanumeric(10);
        String lastName = RandomStringUtils.randomAlphanumeric(10);
        String address = RandomStringUtils.randomAlphanumeric(10);
        String metroStation = String.valueOf(getMetroStationList().get(random.nextInt(getMetroStationList().size())).getName());
        String phone = createPhoneNumber();
        Integer rentTime = random.nextInt(7);
        String deliveryDate = String.valueOf(date);
        String comment = RandomStringUtils.randomAlphabetic(10);
        String[] color = {"BLACK", "GREY"};

        return new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }

    /**
     * Метод создания нового заказа
     *
     * @return - http-запрос создания нового заказа
     */
    @Step("Создаем новый заказ")
    public ValidatableResponse createNewOrderResponse(Order order) {
        return orderApi.createNewOrder(order);
    }

    /**
     * Метод запроса списка заказов
     *
     * @return - http-запрос списка заказов
     */
    public ValidatableResponse getOrderListResponse() {
        return orderApi.getOrderList();
    }
}

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ya.sprint3.api.ApiSettings;
import ya.sprint3.api.HttpStatus;
import ya.sprint3.objects.Order;
import ya.sprint3.stepsfortest.OrderSteps;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderCreateParametrizedTest {
    private final String[] color;
    private OrderSteps orderSteps;
    ApiSettings apiSettings = new ApiSettings();

    public OrderCreateParametrizedTest(String[] color, String testName) {
        this.color = color;
    }

    @Before
    public void setup() {
        orderSteps = new OrderSteps();
    }

    @Parameterized.Parameters(name = "Цвет в заказе: {1}")
    public static Object[][] isOrderCreated() {
        return new Object[][]{
                {new String[]{"BLACK"}, "Черный"},
                {new String[]{"GREY"}, "Серый"},
                {new String[]{"BLACK", "GREY"}, "Черный и серый"},
                {new String[]{}, "Цвет не выбран"},
        };
    }

    @Test
    @DisplayName("Создание нового заказа с разными вариантами цветов")
    @Description("Создание нового заказа с разными вариантами цветов")
    public void createNewOrderWithColor() throws IOException, InterruptedException {
        apiSettings.pingServer();
        Order order = orderSteps.getOrderWithRandomData();
        order.setColor(color);
        ValidatableResponse response = orderSteps.createNewOrderResponse(order);
        assertEquals(response.extract().statusCode(), HttpStatus.CREATED.getValue());
        int trackOrder = response.extract().body().path("track");
        assertTrue(trackOrder > 0);
    }
}

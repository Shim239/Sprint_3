import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.junit.Before;
import org.junit.Test;
import ya.sprint3.api.ApiSettings;
import ya.sprint3.api.HttpStatus;
import ya.sprint3.stepsfortest.OrderSteps;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OrderListTest {
    ApiSettings apiSettings = new ApiSettings();
    OrderSteps orderSteps;

    @Before
    public void setup() {
        orderSteps = new OrderSteps();
        apiSettings.pingServer();
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Получение списка заказов")
    public void getOrderList() {
        ValidatableResponse response = orderSteps.getOrderListResponse();
        assertEquals(HttpStatus.OK.getValue(), response.extract().statusCode());
        List<String> orders = response.extract().body().path("orders");
        assertNotNull(orders);
    }
}

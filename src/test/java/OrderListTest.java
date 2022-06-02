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
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Получение списка заказов")
    public void getOrderList() {
        apiSettings.pingServer();
        ValidatableResponse response = orderSteps.getOrderListResponse();
        assertEquals(response.extract().statusCode(), HttpStatus.OK.getValue());
        List<String> orders = response.extract().body().path("orders");
        assertNotNull(orders);
    }
}

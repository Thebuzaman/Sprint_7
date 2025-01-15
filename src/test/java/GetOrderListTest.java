import ru.praktikum.scooter.steps.OrderSteps;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.Before;
import org.junit.Test;

public class GetOrderListTest extends OrderSteps {
    @Before
    @Step("Предусловие: URL сервиса 'https://qa-scooter.praktikum-services.ru/'")
    public  void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверяем наличие содержимого в полученном списке")
    public void checkOrderListHasBody() {
        Response response = sendGetRequestForCreatingOrder();
        checkStatus200AndBody(response);
    }
}

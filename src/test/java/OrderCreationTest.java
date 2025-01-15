import ru.praktikum.scooter.pojo.CreateOrder;
import ru.praktikum.scooter.steps.OrderSteps;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

@RunWith(Parameterized.class)
public class OrderCreationTest extends OrderSteps {
    private final List<String> colour;
    private String trackId = null;

    public OrderCreationTest(List<String> colour) {
        this.colour = colour;
    }

    @Before
    @Step("Предусловие: URL сервиса 'https://qa-scooter.praktikum-services.ru/'")
    public  void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }
    @After
    @Step("Постусловие: очистка данных - отмена созданного заказа")
    public  void cleanData() {
        if (trackId != null) {
            cancelOrder(trackId);
        }
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][] {
                {List.of("BLACK", "GREY")},
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of()}
        };
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа самоката в двух цветовых решениях")
    public  void checkDifferentColoursForOrder() {
        String firstName = "Дима";
        String lastName = "Жуков";
        String address = "Москва, ул. Владимирская, 1";
        int metroStation = 7;
        String phone = "8 901 234 56 78";
        int rentTime = 1;
        String deliveryDate = "2025-01-31";
        String comment = "Комментарий";
        CreateOrder createOrder = new CreateOrder(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, colour);
        Response response = sendPostRequestForCreatingOrder(createOrder);
        trackId = checkStatus201ForCreatingOrder(response);
    }

}

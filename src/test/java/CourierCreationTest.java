import ru.praktikum.scooter.pojo.CreateCourier;
import ru.praktikum.scooter.pojo.LoginCourier;
import ru.praktikum.scooter.steps.CourierSteps;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CourierCreationTest extends CourierSteps {
    String login = "Body";
    String password = "1234";
    String firstName = "Alex";
    @Before
    @Step("Предусловие: URL сервиса 'https://qa-scooter.praktikum-services.ru/'")
    public  void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }
    @After
    @Step("Постусловие: очистка данных - удаление созданного курьера")
    public  void cleanData() {
        LoginCourier courierLogin = new LoginCourier(login, password);
        Response response = sendPostRequestForLoginCourier(courierLogin);
        int status = response.then().extract().statusCode();
        if (status == 200) {
            String courierId = response.then().extract().body().path("id").toString();
            deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Можно создать курьера с валидными данными")
    public void checkCreatingCourier() {
        CreateCourier courierCreate = new CreateCourier(login, password, firstName);
        Response response = sendPostRequestForCreatingCourier(courierCreate);
        checkStatus201ForCreatingCourier(response);
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("Невозможно создать двух курьеров с одинаковыми валидными данными")
    public void checkCreatingIdenticalCouriers() {
        // Создание курьера
        CreateCourier courierCreate = new CreateCourier(login, password, firstName);
        Response responseFirst = sendPostRequestForCreatingCourier(courierCreate);
        checkStatus201ForCreatingCourier(responseFirst);
        // Повторное создание курьера
        Response responseSecond = sendPostRequestForCreatingCourier(courierCreate);
        checkStatus409ForCreatingCourier(responseSecond);
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Невозможно создать курьера без данных в поле login")
    public void checkCreatingCourierWithoutLoginField() {
        CreateCourier courierCreate = new CreateCourier("", password, firstName);
        Response response = sendPostRequestForCreatingCourier(courierCreate);
        checkStatus400ForCreatingCourier(response);
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Невозможно создать курьера без данных в поле password")
    public void checkCreatingCourierWithoutPasswordField() {
        CreateCourier courierCreate = new CreateCourier(login, "", firstName);
        Response response = sendPostRequestForCreatingCourier(courierCreate);
        checkStatus400ForCreatingCourier(response);
    }

}


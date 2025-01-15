import ru.praktikum.scooter.pojo.CreateCourier;
import ru.praktikum.scooter.pojo.LoginCourier;
import ru.praktikum.scooter.steps.CourierSteps;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CourierLoginTest extends CourierSteps {
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
    @DisplayName("Авторизация курьера")
    @Description("Курьер авторизируется с валидными данными")
    public void checkLoginCourier() {
        CreateCourier courierCreate = new CreateCourier(login, password, firstName);
        LoginCourier courierLogin = new LoginCourier(login, password);
        sendPostRequestForCreatingCourier(courierCreate);
        Response response = sendPostRequestForLoginCourier(courierLogin);
        checkStatus200ForLogin(response);
    }

    @Test
    @DisplayName("Авторизация курьера без логина")
    @Description("Невозможно авторизироваться без данных в поле login")
    public void checkLoginCourierWithoutLoginField() {
        CreateCourier courierCreate = new CreateCourier(login, password, firstName);
        LoginCourier courierLogin = new LoginCourier("", password);
        sendPostRequestForCreatingCourier(courierCreate);
        Response response = sendPostRequestForLoginCourier(courierLogin);
        checkStatus400ForLogin(response);
    }

    @Test
    @DisplayName("Авторизация курьера без пароля")
    @Description("Невозможно авторизироваться без данных в поле password")
    public void checkLoginCourierWithoutPasswordField() {
        CreateCourier courierCreate = new CreateCourier(login, password, firstName);
        LoginCourier courierLogin = new LoginCourier(login, "");
        sendPostRequestForCreatingCourier(courierCreate);
        Response response = sendPostRequestForLoginCourier(courierLogin);
        checkStatus400ForLogin(response);
    }

    @Test
    @DisplayName("Авторизация курьера с несуществующими данными")
    @Description("Невозможно авторизироваться с несуществующими данными")
    public void checkLoginCourierWithNonexistentData() {
        LoginCourier courierLogin = new LoginCourier(login, password);
        Response response = sendPostRequestForLoginCourier(courierLogin);
        checkStatus404ForLogin(response);
    }

}

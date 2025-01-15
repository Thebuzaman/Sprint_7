package ru.praktikum.scooter.steps;
import ru.praktikum.scooter.pojo.CreateCourier;
import ru.praktikum.scooter.pojo.LoginCourier;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierSteps {
    @Step("Отправка запроса POST на /api/v1/courier для создания курьера")
    public Response sendPostRequestForCreatingCourier(CreateCourier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Статус ответа: 201, поле 'ok': true")
    public void checkStatus201ForCreatingCourier(Response response) {
        response.then().assertThat()
                .body("ok",equalTo(true))
                .and()
                .statusCode(201);
    }

    @Step("Статус ответа: 409, поле 'message': 'Этот логин уже используется. Попробуйте другой.'")
    public void checkStatus409ForCreatingCourier(Response response) {
        response.then().assertThat()
                .body("message",equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }

    @Step("Статус ответа: 400, поле 'message': 'Недостаточно данных для создания учетной записи'")
    public void checkStatus400ForCreatingCourier(Response response) {
        response.then().assertThat()
                .body("message",equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Step("Отправка запроса POST на /api/v1/courier/login для авторизации курьера")
    public  Response sendPostRequestForLoginCourier(LoginCourier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .post("/api/v1/courier/login");
    }

    @Step("Статус ответа: 200, поле 'id' со значением")
    public void checkStatus200ForLogin(Response response) {
        response.then().assertThat()
                .body("id",notNullValue())
                .and()
                .statusCode(200);
    }

    @Step("Статус ответа: 400, поле поле 'message': 'Недостаточно данных для входа'")
    public void checkStatus400ForLogin(Response response) {
        response.then().assertThat()
                .body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Step("Статус ответа: 404, поле 'message': 'Учетная запись не найдена'")
    public void checkStatus404ForLogin(Response response) {
        response.then().assertThat()
                .body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Step("Курьер удален")
    public void deleteCourier(String id) {
        given()
                .delete("/api/v1/courier/{id}", id)
                .then()
                .statusCode(200);
    }

}

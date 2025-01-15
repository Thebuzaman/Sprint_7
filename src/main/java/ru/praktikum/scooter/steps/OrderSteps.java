package ru.praktikum.scooter.steps;
import ru.praktikum.scooter.pojo.CreateOrder;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.notNullValue;

public class OrderSteps {
    @Step("Отправка запроса POST на /api/v1/orders для создания заказа")
    public Response sendPostRequestForCreatingOrder(CreateOrder order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

    @Step("Статус ответа: 201, поле 'track' со значением")
    public String checkStatus201ForCreatingOrder(Response response) {
        response.then().assertThat()
                .body("track",notNullValue())
                .and()
                .statusCode(201);
        return response.then().extract().body().path("track").toString();

    }

    @Step("Заказ отменен")
    public void cancelOrder(String track) {
        given()
                .queryParam("track", track)
                .put("/api/v1/orders/cancel")
                .then().statusCode(200);
    }

    @Step("Отправка запроса GET на /api/v1/orders для получения списков заказов")
    public Response sendGetRequestForCreatingOrder() {
        return given()
                .get("/v1/orders");

    }

    @Step("Статус ответа: 200, ответ содержит заказ")
    public void checkStatus200AndBody(Response response) {
        response.then()
                .assertThat()
                .body("orders.id", notNullValue())
                .and()
                .statusCode(200);
    }
}

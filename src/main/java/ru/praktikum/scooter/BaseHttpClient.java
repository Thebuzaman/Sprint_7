package ru.praktikum.scooter;

import io.restassured.specification.RequestSpecification;
import ru.praktikum.scooter.constants.URL;

import static io.restassured.RestAssured.given;

public class BaseHttpClient {
    protected final RequestSpecification requestSpecification = given()
            .baseUri(URL.BASE_URI)
            .header("Content-type", "application/json");
}

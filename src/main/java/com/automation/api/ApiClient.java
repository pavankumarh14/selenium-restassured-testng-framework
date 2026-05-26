package com.automation.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.util.Map;

public class ApiClient {

    private static RequestSpecification requestSpec;
    private static ResponseSpecification responseSpec;

    public static void init(String baseUri) {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("x-api-key", "reqres-free-v1")
                .log(LogDetail.ALL)
                .build();
        responseSpec = new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    public static RequestSpecification getRequestSpec() { return requestSpec; }

    public static Response get(String endpoint) {
        return RestAssured.given(requestSpec).when().get(endpoint)
                .then().spec(responseSpec).extract().response();
    }

    public static Response post(String endpoint, Object body) {
        return RestAssured.given(requestSpec).body(body).when().post(endpoint)
                .then().spec(responseSpec).extract().response();
    }

    public static Response put(String endpoint, Object body) {
        return RestAssured.given(requestSpec).body(body).when().put(endpoint)
                .then().spec(responseSpec).extract().response();
    }

    public static Response patch(String endpoint, Object body) {
        return RestAssured.given(requestSpec).body(body).when().patch(endpoint)
                .then().spec(responseSpec).extract().response();
    }

    public static Response delete(String endpoint) {
        return RestAssured.given(requestSpec).when().delete(endpoint)
                .then().spec(responseSpec).extract().response();
    }

    public static Response getWithHeaders(String endpoint, Map<String, String> headers) {
        return RestAssured.given(requestSpec).headers(headers).when().get(endpoint)
                .then().spec(responseSpec).extract().response();
    }
}

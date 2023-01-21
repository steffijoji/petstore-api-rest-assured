package Pets;

import Entities.Pets;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static io.restassured.config.LogConfig.logConfig;
import static org.hamcrest.Matchers.lessThan;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PetsTest {

    private static String requestBody = "{\n" +
            "  \"id\": 0,\n" +
            "  \"category\": {\n" +
            "    \"id\": 0,\n" +
            "    \"name\": \"Vira-lata\"\n" +
            "  },\n" +
            "  \"name\": \"Totó\",\n" +
            "  \"photoUrls\": [\n" +
            "    \"cachorro_caramelo.com.br\"\n" +
            "  ],\n" +
            "  \"tags\": [\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"name\": \"Caramelo\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"status\": \"available\"\n" +
            "}";

    public static RequestSpecification request;

    Pets pets = new Pets("Vira-Lata", "Totó", "photo-do-toto.com",
            "caramelo", "available");

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @BeforeEach
    void setRequest() {
        request = given()
                .config(RestAssured.config().logConfig(logConfig().enableLoggingOfRequestAndResponseIfValidationFails()))
                .header("api_key", "special-key")
                .contentType(ContentType.JSON);
    }

    @Test
    @Order(1)
    public void AddPetToTheStore_WithValidData_ReturnOK() {
        Response response = request
                .body(requestBody)
                .when()
                .post("/pet")
                .then()
                .assertThat().statusCode(200).and()
                .time(lessThan(2000L))
                .extract().response();

        Assertions.assertEquals(true, response.getBody().asPrettyString().contains("category"));
        Assertions.assertEquals(true, response.getBody().asPrettyString().contains("status"));
    }

    @Test
    @Order(2)
    public void FindPetByStatus_ReturnOK() {
        request
                .param("status", "available")
                .when()
                .get("/pet/findByStatus")
                .then()
                .assertThat().statusCode(200).and()
                .time(lessThan(2000L));
    }

}

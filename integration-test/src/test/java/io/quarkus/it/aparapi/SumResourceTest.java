package io.quarkus.it.aparapi;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class SumResourceTest {

    @Test
    void testSum() {
        // Test Existing Web Jars
        List<Long> result = given().param("a", "1,2,3,4,5,6,7,8,9,10").param("b", "1,2,3,4,5,6,7,8,9,10")
            .when().get("/sum").then()
                .statusCode(200)
                .extract().body().jsonPath().getList(".", Long.class);
        assertArrayEquals(result.toArray(), Arrays.asList(2L,4L,6L,8L,10L,12L,14L,16L,18L,20L).toArray());
    }
}

package pl.kravchuk.stock.productcatalog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HTTPProductCatalogTest {

    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    int serverPort;

    @Autowired
    ProductCatalog productCatalog;

    @BeforeEach
    void clearProducts() {
        productCatalog.empty();
    }

    @Test
    void itLoadsProductsViaEndpoint() {
        //Arrange
        thereIsDraftProduct("example 0");
        thereIsProducts("example 1");
        thereIsProducts("example 2");

        //Act
        ResponseEntity<Product[]> response = callApiForProducts();
        Product[] products = response.getBody();

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, products.length);
    }

    private void thereIsProducts(String productName) {
        String id = productCatalog.addProduct(
                productName,
                BigDecimal.ONE,
                Arrays.asList("tag1"),
                "image path"
        );
        productCatalog.publish(id);
    }

    private void thereIsDraftProduct(String productName) {
        productCatalog.addProduct(
                productName,
                BigDecimal.ONE,
                Arrays.asList("tag1"),
                "image path"
        );
    }

    private ResponseEntity<Product[]> callApiForProducts() {
        String url = String.format(
                "http://localhost:%s/api/products",
                serverPort
        );
        ResponseEntity<Product[]> response =
                restTemplate.getForEntity(url, Product[].class);

        return response;
    }
}

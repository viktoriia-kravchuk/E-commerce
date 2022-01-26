package pl.kravchuk.stock.sales;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.kravchuk.stock.sales.offerting.Offer;
import pl.kravchuk.stock.sales.ordering.Reservation;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JpaReservationStorageTest {
    @Autowired
    JpaReservationStorage jpaReservationStorage;

    @Test
    void itAllowsToStoreAndLoadReservation() {
        //Arrange
        Reservation reservation = exampleReservation();

        //Act
        jpaReservationStorage.save(reservation);
        Reservation loaded = jpaReservationStorage.load(reservation.getId()).get();

        //Assert
        assertEquals(reservation.getId(), loaded.getId());
        assertEquals(reservation.getCustomerEmail(), loaded.getCustomerEmail());
        assertTrue(reservation.getTotal().compareTo(loaded.getTotal()) == 0, "loaded to is not equal to initial one");
        //@toDo needsToBeChecked assertEquals(reservation.getLinesCount(), loaded.getLinesCount());

    }

    private Reservation exampleReservation() {
        return Reservation.of(
                Offer.of(BigDecimal.TEN, Collections.emptyList()),
                Arrays.asList(BasketItem.of("product-1", BigDecimal.valueOf(10.00))),
                new CustomerData("joh.doe@example.com", "john", "doe")
        );
    }
}

package pl.kravchuk.stock.sales;

import pl.kravchuk.stock.sales.ordering.CustomerDetails;
import pl.kravchuk.stock.sales.ordering.PaymentDetails;

import java.math.BigDecimal;

public interface PaymentGateway {
    PaymentDetails register(String id, BigDecimal total, CustomerDetails customerDetails);
}

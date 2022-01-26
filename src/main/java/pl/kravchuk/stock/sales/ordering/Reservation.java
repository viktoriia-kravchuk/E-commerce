package pl.kravchuk.stock.sales.ordering;

import pl.kravchuk.stock.sales.BasketItem;
import pl.kravchuk.stock.sales.CustomerData;
import pl.kravchuk.stock.sales.DummyPaymentGateway;
import pl.kravchuk.stock.sales.offerting.Offer;
import pl.pluta.stock.sales.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
public class Reservation {
    @Id
    private String id;
    private BigDecimal total;
    @Embedded
    private CustomerDetails customerDetails;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<ReservationLine> lines;

    @Embedded
    private PaymentDetails paymentDetails;
    private Instant paidAt;

    public Reservation() {}

    public Reservation(String id, BigDecimal total, CustomerDetails customerDetails, List<ReservationLine> lines) {
        this.id = id;
        this.total = total;
        this.customerDetails = customerDetails;
        this.lines = lines;
        lines.forEach(line -> line.reservation = this);
    }

    public static Reservation of(Offer currentOffer, List<BasketItem> basketItems, CustomerData customerData) {
        return new Reservation(
                UUID.randomUUID().toString(),
                currentOffer.getTotal(),
                CustomerDetails.of(
                        customerData.getFirstname(),
                        customerData.getLastname(),
                        customerData.getEmail()),
                basketItems.stream()
                        .map(bi -> new ReservationLine(bi.getProductId(), bi.getQuantity()))
                        .collect(Collectors.toList())
        );
    }
    public boolean isPending() {
        return paidAt == null;
    }
    public String getCustomerEmail() {
        return customerDetails.email;
    }
    public int getLinesCount() {
        return lines.size();
    }
    public void registerPayment(DummyPaymentGateway paymentGateway) {
        paymentDetails = paymentGateway.register(id, total, customerDetails);
    }
    public String getId() {
        return id;
    }
    public PaymentDetails paymentDetails() {
        return paymentDetails;
    }

    public BigDecimal getTotal() {
        return total;
    }
}

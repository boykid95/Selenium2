package vn.agest.selenium.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class BillingInfo {

    private final String firstName;
    private final String lastName;
    private final String street;
    private final String city;
    private final String country;
    private final String postcode;
    private final String phone;
    private final String email;
    private final String paymentMethod;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}

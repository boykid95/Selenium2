package vn.agest.selenium.model;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class BillingInfo {
    private String firstName;
    private String lastName;
    private String street;
    private String city;
    private String postcode;
    private String country;
    private String phone;
    private String email;
    private String paymentMethod;

    // ============ Static Helper ============

    private static String trim(String value) {
        return value == null ? "" : value.trim();
    }

    public static BillingInfo ofTrimmed(String firstName, String lastName, String street, String city,
                                        String postcode, String country, String phone, String email,
                                        String paymentMethod) {
        return BillingInfo.builder()
                .firstName(trim(firstName))
                .lastName(trim(lastName))
                .street(trim(street))
                .city(trim(city))
                .postcode(trim(postcode))
                .country(trim(country))
                .phone(trim(phone))
                .email(trim(email))
                .paymentMethod(trim(paymentMethod))
                .build();
    }

    public BillingInfo normalize() {
        this.firstName = trim(firstName);
        this.lastName = trim(lastName);
        this.street = trim(street);
        this.city = trim(city);
        this.postcode = trim(postcode);
        this.country = trim(country);
        this.phone = trim(phone);
        this.email = trim(email);
        this.paymentMethod = trim(paymentMethod);
        return this;
    }

    public String getFullName() {
        return (firstName + " " + lastName).trim();
    }

    @Override
    public String toString() {
        return String.format(
                "BillingInfo(firstName=%s, lastName=%s, street=%s, city=%s, country=%s, postcode=%s, phone=%s, email=%s, paymentMethod=%s)",
                firstName, lastName, street, city, country, postcode, phone, email, paymentMethod
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BillingInfo)) return false;
        BillingInfo that = (BillingInfo) o;
        return Objects.equals(trim(firstName), trim(that.firstName)) &&
                Objects.equals(trim(lastName), trim(that.lastName)) &&
                Objects.equals(trim(street), trim(that.street)) &&
                Objects.equals(trim(city), trim(that.city)) &&
                Objects.equals(trim(postcode), trim(that.postcode)) &&
                Objects.equals(trim(country), trim(that.country)) &&
                Objects.equals(trim(phone), trim(that.phone)) &&
                Objects.equals(trim(email), trim(that.email)) &&
                Objects.equals(trim(paymentMethod), trim(that.paymentMethod));
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, street, city, postcode, country, phone, email, paymentMethod);
    }
}

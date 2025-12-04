package vn.agest.selenium.enums;

import vn.agest.selenium.model.BillingInfo;

public enum BillingProfile {

    DEFAULT(
            "Thanh", "Dang",
            "123 Elm Street", "San Mateo", "United States (US)", "94401",
            "0123456789", "thanh.dang@agest.vn",
            "Direct bank transfer"
    );

    private final BillingInfo billingInfo;

    BillingProfile(String firstName, String lastName,
                   String street, String city, String country, String postcode,
                   String phone, String email, String paymentMethod) {

        this.billingInfo = BillingInfo.ofTrimmed(
                firstName, lastName, street, city, postcode,
                country, phone, email, paymentMethod
        );
    }

    public BillingInfo getInfo() {
        return billingInfo;
    }
}

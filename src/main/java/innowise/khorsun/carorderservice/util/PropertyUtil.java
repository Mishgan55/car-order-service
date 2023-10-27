package innowise.khorsun.carorderservice.util;


public class PropertyUtil {
    private PropertyUtil() {
    }

    public static final String BOOKING_NOT_FOUND = "Booking not Found ";
    public static final String MESSAGE_ERROR_BOOKING_EXISTING = "You already have an order, please finish your previous order first";
    public static final String RETURN_BOOKING_ERROR_MESSAGE = "You have already return the car";
    public static final String CAR_NOT_FOUND = "Car not Found ";
    public static final String PLACE_NOT_FOUND = "Place not Found ";
    public static final String DUPLICATE_PLATE_NUMBER = "Car with this plate number already exists ";
    public static final String PAYMENT_NOT_FOUND = "Payment not Found";
    public static final String INVALID_KEY_MESSAGE = "Invalid payment";
    public static final String SUCCESSFULLY_PAYMENT_MESSAGE = "Your payment was successful!";
    public static final String PAYMENT_URL = "http://localhost:8082/e-car-order/payments";
    public static final String INVALID_SESSION_MESSAGE = "Invalid session data received from Stripe";
    public static final String STRIPE_ERROR_MESSAGE = "Stripe error: ";
    public static final String MALFORMED_URL_MESSAGE = "Malformed URL: ";
    public static final String USER_NOT_FOUND = "User not found with ID:";
    public static final String DUPLICATE_EMAIL_MESSAGE = "User with this email already exists: ";
    public static final String DUPLICATE_PHONE_MESSAGE = "User with this phone number already exists: ";
}

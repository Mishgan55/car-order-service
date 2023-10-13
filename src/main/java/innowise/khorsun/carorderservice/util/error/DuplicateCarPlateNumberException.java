package innowise.khorsun.carorderservice.util.error;

public class DuplicateCarPlateNumberException extends RuntimeException {

    public DuplicateCarPlateNumberException(String message) {
        super(message);
    }
}

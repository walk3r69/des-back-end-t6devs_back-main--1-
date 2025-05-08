package T6Devs_Back.T6Devs_Back.api.model.exception;


public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
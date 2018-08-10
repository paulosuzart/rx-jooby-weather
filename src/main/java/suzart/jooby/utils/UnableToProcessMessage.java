package suzart.jooby.utils;


public class UnableToProcessMessage {
    private boolean success;
    private String message;

    public UnableToProcessMessage() {
        this.success = false;
        this.message = "Request could't be completed. Try again in a minute";
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}

package uz.app.authapp.payload;

public record ResponseMessage(boolean success, String message,Object payload) {
}

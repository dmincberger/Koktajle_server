package springboot_ds.koktajle.dto;

public class UserResponseDto {
    private String message;
    public UserResponseDto(String message){
        System.out.println("KONSTRUKTOR ZAINICJOWANY, PRZYJMUJE WIADOMOSC: ".concat(message));
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }
}

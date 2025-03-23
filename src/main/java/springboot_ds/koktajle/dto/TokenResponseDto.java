package springboot_ds.koktajle.dto;

public class TokenResponseDto {
    private String message;
    private String token;

    public TokenResponseDto(String message, String token){
        this.message = message;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }
    public String getToken(){
        return token;
    }

}

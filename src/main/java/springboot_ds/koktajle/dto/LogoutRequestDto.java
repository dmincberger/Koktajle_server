package springboot_ds.koktajle.dto;

public class LogoutRequestDto {
    private String email;
    private String token;

    public LogoutRequestDto(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}

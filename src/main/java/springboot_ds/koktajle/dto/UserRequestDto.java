package springboot_ds.koktajle.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserRequestDto {
    private String email;

    @NotEmpty(message = "hasło nie może być puste")

    @Size(min = 3, max = 10, message = "hasło musi mieć od 3 do 10 znaków")
    private String password = "";

    private String username;

    private Boolean confirmed = false;
    public UserRequestDto(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.confirmed = false;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "UserRequestDto{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", confirmed=" + confirmed +
                '}';
    }

}

package springboot_ds.koktajle.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserRequestDto {
    @Email(message = "nazwa usera powinna być poprawnym adresem email")
    private String email;

    @NotEmpty(message = "hasło nie może być puste")

    @Size(min = 3, max = 10, message = "hasło musi mieć od 3 do 10 znaków")
    private String password = "";

    private Boolean confirmed = false;
    public UserRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
        this.confirmed = false;
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

}

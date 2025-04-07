package springboot_ds.koktajle.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class AdminRequestDto {
    private String email;

    private String password = "";

    private String username;

    private Boolean confirmed = false;
    public AdminRequestDto(String email, String password, String username,Boolean confirmed) {
        this.email = email;
        this.password = password;
        this.confirmed = confirmed;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword(){
        return password;
    }


    public String getUsername() {
        return username;
    }

    public Boolean isConfirmed() {
        return confirmed;
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

package springboot_ds.koktajle.dto;

public class UserInformationDto {
    private String username;
    private String email;
    private Boolean confirmed;

    public UserInformationDto(String username, String email, Boolean confirmed) {
        this.username = username;
        this.email = email;
        this.confirmed = confirmed;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }
}

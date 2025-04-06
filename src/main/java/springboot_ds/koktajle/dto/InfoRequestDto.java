package springboot_ds.koktajle.dto;

public class InfoRequestDto {
    private String username;
    private String email;

    public InfoRequestDto(String username, String email) {
        this.username = username;
        this.email = email;
    }
    public String getUsername() {
        return this.username;
    }
    public String getEmail(){
        return this.email;
    }
}

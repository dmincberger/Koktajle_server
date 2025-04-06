package springboot_ds.koktajle.dto;

public class ExpirationCheckRequestDto {
    private boolean expired;

    public ExpirationCheckRequestDto(boolean expired) {
        this.expired = expired;
    }

    public boolean isExpired() {
        return expired;
    }

}

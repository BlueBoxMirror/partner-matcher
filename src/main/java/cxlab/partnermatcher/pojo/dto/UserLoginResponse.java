package cxlab.partnermatcher.pojo.dto;

public class UserLoginResponse {
    private String token;
    private Long userId;
    private String username;
    private String qqEmail;

    public UserLoginResponse(String token, Long userId, String username, String qqEmail) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.qqEmail = qqEmail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getQqEmail() {
        return qqEmail;
    }

    public void setQqEmail(String qqEmail) {
        this.qqEmail = qqEmail;
    }
}
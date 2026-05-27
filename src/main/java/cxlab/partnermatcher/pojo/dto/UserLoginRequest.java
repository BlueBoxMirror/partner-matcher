package cxlab.partnermatcher.pojo.dto;

public class UserLoginRequest {
    // 和数据库表的username对应，也和Postman里的JSON键名一致
    private String username;
    private String password;

    // 必须提供getter和setter方法
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
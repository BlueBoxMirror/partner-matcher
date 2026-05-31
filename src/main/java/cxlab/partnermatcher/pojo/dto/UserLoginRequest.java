package cxlab.partnermatcher.pojo.dto;

// ✅ 必须是public类，不能是private/default
public class UserLoginRequest {
    // ✅ 字段名必须和JSON里的键名完全一致（大小写也不能错！）
    // 比如JSON里是"username"，类里必须是username，不能是userName、UserName
    private String username;
    private String password;

    // ✅ 必须是public的无参构造函数，不能省略，也不能是private
    public UserLoginRequest() {
    }

    // ✅ getter/setter方法名必须和字段名严格对应，不能拼错！
    // 比如字段username，对应的getter必须是getUsername，setter必须是setUsername
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
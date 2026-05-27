package cxlab.partnermatcher.pojo;

public class User {
    private Long id;
    private String username;
    private String qqEmail;
    private byte[] password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }
}
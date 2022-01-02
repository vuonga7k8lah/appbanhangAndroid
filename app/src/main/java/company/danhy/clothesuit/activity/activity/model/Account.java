package company.danhy.clothesuit.activity.activity.model;

import java.io.Serializable;


public class Account implements Serializable {

    private String token;

    public Account() {
    }

    public Account(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

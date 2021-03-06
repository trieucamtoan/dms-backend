package ca.toantrieu.dms.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AuthorizationRequest {
    private String userName;
    private String password;

    public AuthorizationRequest() {
        userName = null;
        password = null;
    }

    public AuthorizationRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
package ca.toantrieu.dms.Model;

public class JwtTokenResponse {
    private final String token;

    public JwtTokenResponse(String token) {
        this.token = token;
    }
    public String getToken() {
        return this.token;
    }
}

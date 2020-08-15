package ca.toantrieu.dms.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "USER_TABLE")
public class User {
    @Id
    private int id;
    private String userName;
    private String password;
    private String email;

    public User() {
        this.id = -1;
        this.userName = "";
        this.password = "";
        this.email = "";
    }

    public void setId(int id) {
        this.id = id;
    }

    public User(int id, String userName, String password, String email) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}

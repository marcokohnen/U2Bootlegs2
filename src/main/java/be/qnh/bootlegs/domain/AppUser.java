package be.qnh.bootlegs.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class AppUser extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -7272638676258391958L;

    //object fields
    @NotNull
    @Size(min = 4, max = 15)
    private String name;
    @NotNull
    private String password;
    @NotNull
    @Email
    private String email;

    private String role;

    public AppUser() {
    }

    public AppUser(@NotNull @Size(min = 4, max = 15) String name, @NotNull String password, @NotNull @Email String email, String role) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "name='" + name + '\'' +
                ", password='*************'" +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}

package it.richkmeli.rms.data.User;


import it.richkmeli.jframework.auth.model.exception.ModelException;
import it.richkmeli.jframework.util.regex.RegexManager;
import it.richkmeli.jframework.util.regex.exception.RegexException;
import it.richkmeli.rms.data.device.model.Device;
import it.richkmeli.rms.data.rmc.model.Rmc;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class User {
    @Id
    @Length(max = 50)
    private String email;
    @NotNull
    @Length(max = 100)
    private String password;
    @NotNull
    private Boolean admin;

    @OneToMany(mappedBy = "associatedUser", cascade={CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private Set<Device> devices;

    @OneToMany(mappedBy = "associatedUser", cascade={CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private Set<Rmc> rmcs;

    public User() {
    }

    // for foreign keys
    public User(String email) {
        this.email = email;
        this.password = "";
        this.admin = false;
    }

    public User(String email, String password, Boolean admin) throws ModelException {
        checkUserIntegrity(email, password, admin);
        this.email = email;
        this.password = password;
        this.admin = admin;
    }


    public User(String email, String password) throws ModelException {
        this(email, password, false);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public static void checkUserIntegrity(String email, String password, Boolean admin) throws ModelException {
        try {
            RegexManager.checkEmailIntegrity(email);
        } catch (RegexException e) {
            throw new ModelException("Email is not valid");
        }
        // ...
    }

}
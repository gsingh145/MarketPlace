import java.io.Serializable;

/**
 * Account
 * <p>
 * Stores email and password
 *
 * @author Isaac, Cynthia, Gaurav, Hayden, Albert
 * @version 11/14
 */

public class Account implements Serializable {
    private String email;
    private int passwordHash;


    public Account(String email, String password) {
        this.email = email;

        // We would hash the password more securely if this wasn't just for class
        // We know this is very insecure.
        this.passwordHash = password.hashCode();
    }

    private Account(String email, int passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public boolean authenticateAccount(String inputtedPassword) {
        System.out.println(passwordHash);
        System.out.println(inputtedPassword.hashCode());
        return passwordHash == inputtedPassword.hashCode();
    }
}

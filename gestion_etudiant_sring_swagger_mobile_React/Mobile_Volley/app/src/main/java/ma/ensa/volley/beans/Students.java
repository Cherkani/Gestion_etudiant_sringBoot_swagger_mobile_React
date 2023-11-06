package ma.ensa.volley.beans;
import java.util.List;

public class Students {
    private int id;
    private String firstname;
    private String password;
    private String lastname;
    private String telephone;
    private String login;
    private Filiere filiere;
    private List<Role> roles;

    public Students() {
    }

    public Students(int id, String firstname, String password, String lastname, String telephone, String login, Filiere filiere) {
        this.id = id;
        this.firstname = firstname;
        this.password = password;
        this.lastname = lastname;
        this.telephone = telephone;
        this.login = login;
        this.filiere = filiere;
    }

    public Students(String firstname, String password, String lastname, String telephone, String login, Filiere filiere) {
        this.firstname = firstname;
        this.password = password;
        this.lastname = password;
        this.telephone = telephone;
        this.login = login;
        this.filiere = filiere;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

}

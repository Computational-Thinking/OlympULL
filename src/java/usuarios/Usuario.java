package usuarios;

public abstract class Usuario {
    String userName;
    String password;
    String userType;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }
}

package beans;

public class User {

    private int id;
    private String username;
    private String passhash;

    public User(int id, String username, String passhash) {
        this.id = id;
        this.username = username;
        this.passhash = passhash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasshash() {
        return passhash;
    }

    public void setPasshash(String passhash) {
        this.passhash = passhash;
    }

}

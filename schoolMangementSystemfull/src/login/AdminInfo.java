
package login;


public class AdminInfo {
    int ID;
    String name;
    String password;
    String user_name;

    public AdminInfo(int ID, String name, String password, String user_name) {
        this.ID = ID;
        this.name = name;
        this.password = password;
        this.user_name = user_name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    
}

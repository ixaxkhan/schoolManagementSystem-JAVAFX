
package transport;


public class ViechleRegViewModal {
    private int ID;
    private String Name;
    private String No;
    private int Capacity;
    private String RouteName;

    public ViechleRegViewModal(int ID, String Name, String No, int Capacity, String RouteName) {
        this.ID = ID;
        this.Name = Name;
        this.No = No;
        this.Capacity = Capacity;
        this.RouteName = RouteName;
    }

    
    
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String No) {
        this.No = No;
    }

    public int getCapacity() {
        return Capacity;
    }

    public void setCapacity(int Capacity) {
        this.Capacity = Capacity;
    }

    public String getRouteName() {
        return RouteName;
    }

    public void setRouteName(String RouteName) {
        this.RouteName = RouteName;
    }
    
    
}

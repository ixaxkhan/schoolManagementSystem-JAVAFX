
package transport;


public class RouteViewModal {
    private  int RouteID;
    
    private String Name;
   
    private String From;
    
    private String To;

    public RouteViewModal(int RouteID, String Name, String From, String To) {
        this.RouteID = RouteID;
        this.Name = Name;
        this.From = From;
        this.To = To;
    }
    

    public int getRouteID() {
        return RouteID;
    }

    public void setRouteID(int RouteID) {
        this.RouteID = RouteID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String From) {
        this.From = From;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String To) {
        this.To = To;
    }
    
}

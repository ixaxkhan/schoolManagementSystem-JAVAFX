
package inventory;

import java.sql.Date;


public class ProductAddViewModal {
    private int ProductID;
    private String ProductName;
    private String Description;
    private Date Date;
    private int tbMin;
    private int tbMax;

    public ProductAddViewModal(int ProductID, String ProductName, String Description, Date Date, int tbMin, int tbMax) {
        this.ProductID = ProductID;
        this.ProductName = ProductName;
        this.Description = Description;
        this.Date = Date;
        this.tbMin = tbMin;
        this.tbMax = tbMax;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int ProductID) {
        this.ProductID = ProductID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public Date getDate() {
        return Date;
    }

    public void setDate(Date Date) {
        this.Date = Date;
    }

    public int getTbMin() {
        return tbMin;
    }

    public void setTbMin(int tbMin) {
        this.tbMin = tbMin;
    }

    public int getTbMax() {
        return tbMax;
    }

    public void setTbMax(int tbMax) {
        this.tbMax = tbMax;
    }
    
    
    
}

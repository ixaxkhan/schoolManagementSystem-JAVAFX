
package inventory;

import java.sql.Date;


public class ProductPurchaseViewModal {
    private int PurCaseID;
    private int EmployeeID;
    private String From;
    private String BillNo;
    private String ProductName;
    private int ProductQuantity;
    private double UnitPrice;
    private double TotalPrice;
    private Date date;

    public ProductPurchaseViewModal(int PurCaseID, int EmployeeID, String From, String BillNo, String ProductName, int ProductQuantity, double UnitPrice, double TotalPrice) {
        this.PurCaseID = PurCaseID;
        this.EmployeeID = EmployeeID;
        this.From = From;
        this.BillNo = BillNo;
        this.ProductName = ProductName;
        this.ProductQuantity = ProductQuantity;
        this.UnitPrice = UnitPrice;
        this.TotalPrice = TotalPrice;
    }

    public ProductPurchaseViewModal(int PurCaseID, int EmployeeID, String From, String BillNo, String ProductName, int ProductQuantity, double UnitPrice, double TotalPrice, Date date) {
        this.PurCaseID = PurCaseID;
        this.EmployeeID = EmployeeID;
        this.From = From;
        this.BillNo = BillNo;
        this.ProductName = ProductName;
        this.ProductQuantity = ProductQuantity;
        this.UnitPrice = UnitPrice;
        this.TotalPrice = TotalPrice;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    

    public int getPurCaseID() {
        return PurCaseID;
    }

    public void setPurCaseID(int PurCaseID) {
        this.PurCaseID = PurCaseID;
    }

    public int getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(int EmployeeID) {
        this.EmployeeID = EmployeeID;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String From) {
        this.From = From;
    }

    public String getBillNo() {
        return BillNo;
    }

    public void setBillNo(String BillNo) {
        this.BillNo = BillNo;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public int getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(int ProductQuantity) {
        this.ProductQuantity = ProductQuantity;
    }

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double UnitPrice) {
        this.UnitPrice = UnitPrice;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double TotalPrice) {
        this.TotalPrice = TotalPrice;
    }
    
    
    
}

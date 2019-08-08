/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory;

import java.sql.Date;

/**
 *
 * @author khan
 */
public class ProductSaleViewModal {
    private double UnitPrice;
    
    private double PaidAmount;
  
    private double Dues;
   
    private int SaleId;
    
    private int StuID;
    
    private String ProductName;
   
    private int Quantity;
  
    private double TotalPrice;
    
    private String Status;
    
    private Date date;

    public ProductSaleViewModal(double UnitPrice, double PaidAmount, double Dues, int SaleId, int StuID, String ProductName, int Quantity, double TotalPrice, String Status) {
        this.UnitPrice = UnitPrice;
        this.PaidAmount = PaidAmount;
        this.Dues = Dues;
        this.SaleId = SaleId;
        this.StuID = StuID;
        this.ProductName = ProductName;
        this.Quantity = Quantity;
        this.TotalPrice = TotalPrice;
        this.Status = Status;
    }

    public ProductSaleViewModal(double UnitPrice, double PaidAmount, double Dues, int SaleId, int StuID, String ProductName, int Quantity, double TotalPrice, String Status, Date date) {
        this.UnitPrice = UnitPrice;
        this.PaidAmount = PaidAmount;
        this.Dues = Dues;
        this.SaleId = SaleId;
        this.StuID = StuID;
        this.ProductName = ProductName;
        this.Quantity = Quantity;
        this.TotalPrice = TotalPrice;
        this.Status = Status;
        this.date = date;
    }

    
    
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double UnitPrice) {
        this.UnitPrice = UnitPrice;
    }

    public double getPaidAmount() {
        return PaidAmount;
    }

    public void setPaidAmount(double PaidAmount) {
        this.PaidAmount = PaidAmount;
    }

    public double getDues() {
        return Dues;
    }

    public void setDues(double Dues) {
        this.Dues = Dues;
    }

    public int getSaleId() {
        return SaleId;
    }

    public void setSaleId(int SaleId) {
        this.SaleId = SaleId;
    }

    public int getStuID() {
        return StuID;
    }

    public void setStuID(int StuID) {
        this.StuID = StuID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double TotalPrice) {
        this.TotalPrice = TotalPrice;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }
    
    
    
}

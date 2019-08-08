/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expenses;

import java.sql.Date;

/**
 *
 * @author khan
 */
public class ExpenseConsumeViewModal {
    private int ExpenseConsumeID;
  
    private String CategoryName;
   
    private double ExpenseAmount;
   
    private Date date;
    
    private  String Details;

   

    public ExpenseConsumeViewModal(int ExpenseConsumeID, String CategoryName, double ExpenseAmount, Date date, String Details) {
        this.ExpenseConsumeID = ExpenseConsumeID;
        this.CategoryName = CategoryName;
        this.ExpenseAmount = ExpenseAmount;
        this.date = date;
        this.Details = Details;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String Details) {
        this.Details = Details;
    }
    
    

    public int getExpenseConsumeID() {
        return ExpenseConsumeID;
    }

    public void setExpenseConsumeID(int ExpenseConsumeID) {
        this.ExpenseConsumeID = ExpenseConsumeID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String CategoryName) {
        this.CategoryName = CategoryName;
    }

    public double getExpenseAmount() {
        return ExpenseAmount;
    }

    public void setExpenseAmount(double ExpenseAmount) {
        this.ExpenseAmount = ExpenseAmount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    
}

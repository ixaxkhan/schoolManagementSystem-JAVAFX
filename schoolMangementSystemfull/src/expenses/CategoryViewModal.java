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
public class CategoryViewModal {
    private int tbExpenseID;
    private String tbExpenseName;
    private Date tbDate;

    public CategoryViewModal(int tbExpenseID, String tbExpenseName, Date tbDate) {
        this.tbExpenseID = tbExpenseID;
        this.tbExpenseName = tbExpenseName;
        this.tbDate = tbDate;
    }

    
    public int getTbExpenseID() {
        return tbExpenseID;
    }

    public void setTbExpenseID(int tbExpenseID) {
        this.tbExpenseID = tbExpenseID;
    }

    public String getTbExpenseName() {
        return tbExpenseName;
    }

    public void setTbExpenseName(String tbExpenseName) {
        this.tbExpenseName = tbExpenseName;
    }

    public Date getTbDate() {
        return tbDate;
    }

    public void setTbDate(Date tbDate) {
        this.tbDate = tbDate;
    }
    
}

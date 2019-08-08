/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package account;

import java.sql.Date;

/**
 *
 * @author khan
 */
public class AccountEmployeeSalaryModal {
    
    private int EmpSlip;
    
    private int EmpRegNo;
    
    private String EmpName;
    
    private String EmpFName;
   
    private Date EmpDate;
   
    private String EmpFeeType;
 
    private String EmpStatus;
   
    private double EmpTotalFee;
  
    private double EmpPaidFee;
  
    private double EmpDues;

    public AccountEmployeeSalaryModal(int EmpSlip, int EmpRegNo, String EmpName, String EmpFName, Date EmpDate, String EmpFeeType, String EmpStatus, double EmpTotalFee, double EmpPaidFee) {
        this.EmpSlip = EmpSlip;
        this.EmpRegNo = EmpRegNo;
        this.EmpName = EmpName;
        this.EmpFName = EmpFName;
        this.EmpDate = EmpDate;
        this.EmpFeeType = EmpFeeType;
        this.EmpStatus = EmpStatus;
        this.EmpTotalFee = EmpTotalFee;
        this.EmpPaidFee = EmpPaidFee;
    }

    public AccountEmployeeSalaryModal(int EmpSlip, int EmpRegNo, String EmpName, String EmpFName, Date EmpDate, String EmpFeeType, String EmpStatus, double EmpTotalFee, double EmpPaidFee, double EmpDues) {
        this.EmpSlip = EmpSlip;
        this.EmpRegNo = EmpRegNo;
        this.EmpName = EmpName;
        this.EmpFName = EmpFName;
        this.EmpDate = EmpDate;
        this.EmpFeeType = EmpFeeType;
        this.EmpStatus = EmpStatus;
        this.EmpTotalFee = EmpTotalFee;
        this.EmpPaidFee = EmpPaidFee;
        this.EmpDues = EmpDues;
    }

    
    public int getEmpSlip() {
        return EmpSlip;
    }

    public void setEmpSlip(int EmpSlip) {
        this.EmpSlip = EmpSlip;
    }

    public int getEmpRegNo() {
        return EmpRegNo;
    }

    public void setEmpRegNo(int EmpRegNo) {
        this.EmpRegNo = EmpRegNo;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String EmpName) {
        this.EmpName = EmpName;
    }

    public String getEmpFName() {
        return EmpFName;
    }

    public void setEmpFName(String EmpFName) {
        this.EmpFName = EmpFName;
    }

    public Date getEmpDate() {
        return EmpDate;
    }

    public void setEmpDate(Date EmpDate) {
        this.EmpDate = EmpDate;
    }

    public String getEmpFeeType() {
        return EmpFeeType;
    }

    public void setEmpFeeType(String EmpFeeType) {
        this.EmpFeeType = EmpFeeType;
    }

    public String getEmpStatus() {
        return EmpStatus;
    }

    public void setEmpStatus(String EmpStatus) {
        this.EmpStatus = EmpStatus;
    }

    public double getEmpTotalFee() {
        return EmpTotalFee;
    }

    public void setEmpTotalFee(double EmpTotalFee) {
        this.EmpTotalFee = EmpTotalFee;
    }

    public double getEmpPaidFee() {
        return EmpPaidFee;
    }

    public void setEmpPaidFee(double EmpPaidFee) {
        this.EmpPaidFee = EmpPaidFee;
    }

    public double getEmpDues() {
        return EmpDues;
    }

    public void setEmpDues(double EmpDues) {
        this.EmpDues = EmpDues;
    }
    
    
    
    
}

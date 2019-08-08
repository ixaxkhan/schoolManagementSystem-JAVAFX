/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package account;

import java.sql.Date;


public class AccountFeeCollectionModal {
    
    private int ToutionSlip;
    
    private int ToutionRegNo;
    
    private String ToutionName;
    
    private String ToutionFName;
    
    private Date ToutionDate;
    
    private String ToutionFeeType;
   
    private String ToutionStatus;
   
    private double ToutionTotalFee;
    
    private double ToutionPaidFee;
   
    private double ToutionDues; 

    public AccountFeeCollectionModal(int ToutionSlip, int ToutionRegNo, String ToutionName, String ToutionFName, Date ToutionDate, String ToutionFeeType, String ToutionStatus, double ToutionTotalFee, double ToutionPaidFee, double ToutionDues) {
        this.ToutionSlip = ToutionSlip;
        this.ToutionRegNo = ToutionRegNo;
        this.ToutionName = ToutionName;
        this.ToutionFName = ToutionFName;
        this.ToutionDate = ToutionDate;
        this.ToutionFeeType = ToutionFeeType;
        this.ToutionStatus = ToutionStatus;
        this.ToutionTotalFee = ToutionTotalFee;
        this.ToutionPaidFee = ToutionPaidFee;
        this.ToutionDues = ToutionDues;
    }

    public int getToutionSlip() {
        return ToutionSlip;
    }

    public void setToutionSlip(int ToutionSlip) {
        this.ToutionSlip = ToutionSlip;
    }

    public int getToutionRegNo() {
        return ToutionRegNo;
    }

    public void setToutionRegNo(int ToutionRegNo) {
        this.ToutionRegNo = ToutionRegNo;
    }

    public String getToutionName() {
        return ToutionName;
    }

    public void setToutionName(String ToutionName) {
        this.ToutionName = ToutionName;
    }

    public String getToutionFName() {
        return ToutionFName;
    }

    public void setToutionFName(String ToutionFName) {
        this.ToutionFName = ToutionFName;
    }

    public Date getToutionDate() {
        return ToutionDate;
    }

    public void setToutionDate(Date ToutionDate) {
        this.ToutionDate = ToutionDate;
    }

    public String getToutionFeeType() {
        return ToutionFeeType;
    }

    public void setToutionFeeType(String ToutionFeeType) {
        this.ToutionFeeType = ToutionFeeType;
    }

    public String getToutionStatus() {
        return ToutionStatus;
    }

    public void setToutionStatus(String ToutionStatus) {
        this.ToutionStatus = ToutionStatus;
    }

    public double getToutionTotalFee() {
        return ToutionTotalFee;
    }

    public void setToutionTotalFee(double ToutionTotalFee) {
        this.ToutionTotalFee = ToutionTotalFee;
    }

    public double getToutionPaidFee() {
        return ToutionPaidFee;
    }

    public void setToutionPaidFee(double ToutionPaidFee) {
        this.ToutionPaidFee = ToutionPaidFee;
    }

    public double getToutionDues() {
        return ToutionDues;
    }

    public void setToutionDues(double ToutionDues) {
        this.ToutionDues = ToutionDues;
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employee;

import java.sql.Date;

/**
 *
 * @author khan
 */
public class EmployeeLeaveModal {
    int RegNo;
    String Name;
    String FatherName;
    Date LeaveDate;
    String LeaveReason;
    Double Salary;
    String Gender;

    public EmployeeLeaveModal(int RegNo, String Name, String FatherName, Date LeaveDate, String LeaveReason, Double Salary, String Gender) {
        this.RegNo = RegNo;
        this.Name = Name;
        this.FatherName = FatherName;
        this.LeaveDate = LeaveDate;
        this.LeaveReason = LeaveReason;
        this.Salary = Salary;
        this.Gender = Gender;
    }

   

    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public int getRegNo() {
        return RegNo;
    }

    public void setRegNo(int RegNo) {
        this.RegNo = RegNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getFatherName() {
        return FatherName;
    }

    public void setFatherName(String FatherName) {
        this.FatherName = FatherName;
    }

    public Date getLeaveDate() {
        return LeaveDate;
    }

    public void setLeaveDate(Date LeaveDate) {
        this.LeaveDate = LeaveDate;
    }

    public String getLeaveReason() {
        return LeaveReason;
    }

    public void setLeaveReason(String LeaveReason) {
        this.LeaveReason = LeaveReason;
    }

    public Double getSalary() {
        return Salary;
    }

    public void setSalary(Double Salary) {
        this.Salary = Salary;
    }
    
 
}

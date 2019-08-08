/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student.Registration;

import java.sql.Date;

/**
 *
 * @author khan
 */
public class StudentLeaveModal {
    int regNo;
    String name,gender,fatherName,className,reason,address;
    Date date;

    public StudentLeaveModal(int regNo, String name, String gender, String fatherName, String className, String reason, String address, Date date) {
        this.regNo = regNo;
        this.name = name;
        this.gender = gender;
        this.fatherName = fatherName;
        this.className = className;
        this.reason = reason;
        this.address = address;
        this.date = date;
    }

    public int getRegNo() {
        return regNo;
    }

    public void setRegNo(int regNo) {
        this.regNo = regNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student.Registration;

import java.sql.Date;

public class StudentModel {
  int regNo;
  String name,fatherName,gender,email,phone,address;
  Date date;

    public StudentModel(int regNo, String name, String fatherName, String gender, String email, String phone, String address) {
        this.regNo = regNo;
        this.name = name;
        this.fatherName = fatherName;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public StudentModel(int regNo, String name, String fatherName, String gender, String email, String phone, String address, Date date) {
        this.regNo = regNo;
        this.name = name;
        this.fatherName = fatherName;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.date = date;
    }

    public StudentModel(int regNo) {
        this.regNo = regNo;
    }
    

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
  
}

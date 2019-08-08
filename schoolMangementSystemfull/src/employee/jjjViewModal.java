/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employee;

import java.sql.Date;


public class jjjViewModal {
    int RegNo;
    String Name;
    String FatherName;
    String Gender;
    String Address;
    String Phone;
    String Email;
    String Designation;
    String Experience;
    String Qualifiction;
    double Salary;
    Date date;
    
    

    public jjjViewModal(int RegNo, String Name, String FatherName, String Gender, String Address, String Phone, String Email, String Designation, String Experience, String Qualifiction, double Salary) {
        this.RegNo = RegNo;
        this.Name = Name;
        this.FatherName = FatherName;
        this.Gender = Gender;
        this.Address = Address;
        this.Phone = Phone;
        this.Email = Email;
        this.Designation = Designation;
        this.Experience = Experience;
        this.Qualifiction = Qualifiction;
        this.Salary = Salary;
    }

    public jjjViewModal(int RegNo, String Name, String FatherName, String Gender, String Address, String Phone, String Email, String Designation, String Experience, String Qualifiction, double Salary, Date date) {
        this.RegNo = RegNo;
        this.Name = Name;
        this.FatherName = FatherName;
        this.Gender = Gender;
        this.Address = Address;
        this.Phone = Phone;
        this.Email = Email;
        this.Designation = Designation;
        this.Experience = Experience;
        this.Qualifiction = Qualifiction;
        this.Salary = Salary;
        this.date = date;
    }
    

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String Designation) {
        this.Designation = Designation;
    }

    public String getExperience() {
        return Experience;
    }

    public void setExperience(String Experience) {
        this.Experience = Experience;
    }

    public String getQualifiction() {
        return Qualifiction;
    }

    public void setQualifiction(String Qualifiction) {
        this.Qualifiction = Qualifiction;
    }

    public double getSalary() {
        return Salary;
    }

    public void setSalary(double Salary) {
        this.Salary = Salary;
    }
    
    
}

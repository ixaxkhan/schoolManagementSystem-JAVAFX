/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student.Registration;

/**
 *
 * @author khan
 */
public class PromotionViewModal {
    int regNo;
    String name,fatherName,gender;
    boolean promot;

    public PromotionViewModal(int regNo, String name, String fName, String gender) {
        this.regNo = regNo;
        this.name = name;
        this.fatherName = fName;
        this.gender = gender;
    }

    public PromotionViewModal(int regNo, String name, String fName, String gender, boolean promot) {
        this.regNo = regNo;
        this.name = name;
        this.fatherName = fName;
        this.gender = gender;
        this.promot = promot;
    }
    

    public boolean isPromot() {
        return promot;
    }

    public void setPromot(boolean promot) {
        this.promot = promot;
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
   
}

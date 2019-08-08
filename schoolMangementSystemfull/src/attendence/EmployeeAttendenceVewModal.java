
package attendence;


public class EmployeeAttendenceVewModal {
    private int EmpID;

    private String EmpName;

    private String Gender;

    private String Address;

    private String Phone;
    
    private  boolean present ;

    public EmployeeAttendenceVewModal(int EmpID, String EmpName, String Gender, String Address, String Phone, boolean present) {
        this.EmpID = EmpID;
        this.EmpName = EmpName;
        this.Gender = Gender;
        this.Address = Address;
        this.Phone = Phone;
        this.present = present;
    }

  

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    
    
    public int getEmpID() {
        return EmpID;
    }

    public void setEmpID(int EmpID) {
        this.EmpID = EmpID;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String EmpName) {
        this.EmpName = EmpName;
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
    
    
}

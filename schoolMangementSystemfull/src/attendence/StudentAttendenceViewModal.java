
package attendence;


public class StudentAttendenceViewModal {
    private int RegNo;
    
    private String StuName;
    
    private String FName;
  
    private String Gender;
    
    boolean present ;

    public StudentAttendenceViewModal(int RegNo, String StuName, String FName, String Gender, boolean present) {
        this.RegNo = RegNo;
        this.StuName = StuName;
        this.FName = FName;
        this.Gender = Gender;
        this.present = present;
    }
    

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

   
    public int getRegNo() {
        return RegNo;
    }

    public void setRegNo(int RegNo) {
        this.RegNo = RegNo;
    }

    public String getStuName() {
        return StuName;
    }

    public void setStuName(String StuName) {
        this.StuName = StuName;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }
    
    
}

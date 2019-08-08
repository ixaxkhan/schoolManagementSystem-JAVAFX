
package transport;


public class viechleAsignToStudentViewModal {
    private int ID;

    private String StuName;

    private int StuRegNo;

    private String StuAddress;

    private String ViechleName;

    public viechleAsignToStudentViewModal(int ID, String StuName, int StuRegNo, String StuAddress, String ViechleName) {
        this.ID = ID;
        this.StuName = StuName;
        this.StuRegNo = StuRegNo;
        this.StuAddress = StuAddress;
        this.ViechleName = ViechleName;
    }
    

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getStuName() {
        return StuName;
    }

    public void setStuName(String StuName) {
        this.StuName = StuName;
    }

    public int getStuRegNo() {
        return StuRegNo;
    }

    public void setStuRegNo(int StuRegNo) {
        this.StuRegNo = StuRegNo;
    }

    public String getStuAddress() {
        return StuAddress;
    }

    public void setStuAddress(String StuAddress) {
        this.StuAddress = StuAddress;
    }

    public String getViechleName() {
        return ViechleName;
    }

    public void setViechleName(String ViechleName) {
        this.ViechleName = ViechleName;
    }
    
    
    
}

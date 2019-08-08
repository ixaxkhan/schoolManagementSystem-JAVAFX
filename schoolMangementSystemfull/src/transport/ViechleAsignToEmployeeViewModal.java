
package transport;


public class ViechleAsignToEmployeeViewModal {
   
    private int ID;

    private String EmpName;

    private String EmpDesignation;

    private String ViechleNo;

    private String ViechleName;

    public ViechleAsignToEmployeeViewModal(int ID, String EmpName, String EmpDesignation, String ViechleNo, String ViechleName) {
        this.ID = ID;
        this.EmpName = EmpName;
        this.EmpDesignation = EmpDesignation;
        this.ViechleNo = ViechleNo;
        this.ViechleName = ViechleName;
    }
    
    

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String EmpName) {
        this.EmpName = EmpName;
    }

    public String getEmpDesignation() {
        return EmpDesignation;
    }

    public void setEmpDesignation(String EmpDesignation) {
        this.EmpDesignation = EmpDesignation;
    }

    public String getViechleNo() {
        return ViechleNo;
    }

    public void setViechleNo(String ViechleNo) {
        this.ViechleNo = ViechleNo;
    }

    public String getViechleName() {
        return ViechleName;
    }

    public void setViechleName(String ViechleName) {
        this.ViechleName = ViechleName;
    }
    
}

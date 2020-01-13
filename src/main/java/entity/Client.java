package entity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Client")
public class Client {

    public Client(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "fName")
    private String fName;

    @Column(name = "lName")
    private String lName;

    @Column(name = "pName")
    private String pName;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "passportSerial")
    private int passportSerial;

    @Column(name = "passportNumber")
    private int passportNumber;

    @OneToMany(mappedBy = "client")
    private Set<Agreement> agreements = new HashSet<Agreement>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getPassportSerial() {
        return passportSerial;
    }

    public void setPassportSerial(int passportSerial) {
        this.passportSerial = passportSerial;
    }

    public int getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(int passportNumber) {
        this.passportNumber = passportNumber;
    }

    public Set<Agreement> getAgreements() {
        return agreements;
    }

    public void setAgreements(Set<Agreement> agreements) {
        this.agreements = agreements;
    }

}

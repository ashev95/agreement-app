package entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Agreement")
public class Agreement {

    public Agreement(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "insuranceAmount")
    private double insuranceAmount;

    @Column(name = "typeOfProperty")
    private String typeOfProperty;

    @Column(name = "yearOfConstruction")
    private String yearOfConstruction;

    @Column(name = "area")
    private String area;

    @Column(name = "limitDateStart")
    private Date limitDateStart;

    @Column(name = "limitDateEnd")
    private Date limitDateEnd;

    @Column(name = "settlementDate")
    private Date settlementDate;

    @Column(name = "premium")
    private String premium;

    @Column(name = "agreementNumber")
    private int agreementNumber;

    @Column(name = "concludeDate")
    private Date concludeDate;

    @ManyToOne
    @JoinColumn(name = "clientId")
    private Client client;

    @Column(name = "clientBirthday")
    private Date clientBirthday;

    @Column(name = "clientPassportSerial")
    private String clientPassportSerial;

    @Column(name = "clientPassportNumber")
    private String clientPassportNumber;

    @Column(name = "addressState")
    private String addressState;

    @Column(name = "addressIndex")
    private String addressIndex;

    @Column(name = "addressConcrete")
    private String addressConcrete;

    @Column(name = "addressArea")
    private String addressArea;

    @Column(name = "addressLocality")
    private String addressLocality;

    @Column(name = "addressStreet")
    private String addressStreet;

    @Column(name = "houseNumber")
    private int houseNumber;

    @Column(name = "houseCorps")
    private String houseCorps;

    @Column(name = "building")
    private String building;

    @Column(name = "door")
    private int door;

    @Column(name = "comment")
    private String comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(double insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public String getTypeOfProperty() {
        return typeOfProperty;
    }

    public void setTypeOfProperty(String typeOfProperty) {
        this.typeOfProperty = typeOfProperty;
    }

    public String getYearOfConstruction() {
        return yearOfConstruction;
    }

    public void setYearOfConstruction(String yearOfConstruction) {
        this.yearOfConstruction = yearOfConstruction;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Date getLimitDateStart() {
        return limitDateStart;
    }

    public void setLimitDateStart(Date limitDateStart) {
        this.limitDateStart = limitDateStart;
    }

    public Date getLimitDateEnd() {
        return limitDateEnd;
    }

    public void setLimitDateEnd(Date limitDateEnd) {
        this.limitDateEnd = limitDateEnd;
    }

    public Date getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public int getAgreementNumber() {
        return agreementNumber;
    }

    public void setAgreementNumber(int agreementNumber) {
        this.agreementNumber = agreementNumber;
    }

    public Date getConcludeDate() {
        return concludeDate;
    }

    public void setConcludeDate(Date concludeDate) {
        this.concludeDate = concludeDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getClientBirthday() {
        return clientBirthday;
    }

    public void setClientBirthday(Date clientBirthday) {
        this.clientBirthday = clientBirthday;
    }

    public String getClientPassportSerial() {
        return clientPassportSerial;
    }

    public void setClientPassportSerial(String clientPassportSerial) {
        this.clientPassportSerial = clientPassportSerial;
    }

    public String getClientPassportNumber() {
        return clientPassportNumber;
    }

    public void setClientPassportNumber(String clientPassportNumber) {
        this.clientPassportNumber = clientPassportNumber;
    }

    public String getAddressState() {
        return addressState;
    }

    public void setAddressState(String addressState) {
        this.addressState = addressState;
    }

    public String getAddressIndex() {
        return addressIndex;
    }

    public void setAddressIndex(String addressIndex) {
        this.addressIndex = addressIndex;
    }

    public String getAddressConcrete() {
        return addressConcrete;
    }

    public void setAddressConcrete(String addressConcrete) {
        this.addressConcrete = addressConcrete;
    }

    public String getAddressArea() {
        return addressArea;
    }

    public void setAddressArea(String addressArea) {
        this.addressArea = addressArea;
    }

    public String getAddressLocality() {
        return addressLocality;
    }

    public void setAddressLocality(String addressLocality) {
        this.addressLocality = addressLocality;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getHouseCorps() {
        return houseCorps;
    }

    public void setHouseCorps(String houseCorps) {
        this.houseCorps = houseCorps;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public int getDoor() {
        return door;
    }

    public void setDoor(int door) {
        this.door = door;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

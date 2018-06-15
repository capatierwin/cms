package com.cogclarkview.reporting.models;

import javax.persistence.*;
import java.util.Date;

@Entity(name="account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "is_leader")
    private boolean isLeader;

    @Column(name = "head_cluster_area")
    private String headClusterArea;

    @Column(name = "cluster_area")
    private String clusterArea;

    @Column(name = "leader_id")
    private Long leaderID;

    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "group_age")
    private String groupAge;

    private boolean active;
    private String name;
    private String gender;
    private String address;
    private String journey;
    private String cldp;
    private String username;
    private String password;
    private String type;

    public Account() {}

    public Account(String username, String password, String type, boolean isLeader, String headClusterArea, String clusterArea, long leaderID, boolean active, String name, String address, Date birthday, String contactNumber, String gender, String groupAge, String journey, String cldp) {
        this.username = username;
        this.password = password;
        this.type = type;
        this.isLeader = isLeader;
        this.headClusterArea = headClusterArea;
        this.clusterArea = clusterArea;
        this.leaderID = leaderID;
        this.name = name;
        this.address = address;
        this.birthday = birthday;
        this.contactNumber = contactNumber;
        this.gender = gender;
        this.groupAge = groupAge;
        this.journey = journey;
        this.cldp = cldp;
    }

    public Account(Long id, String clusterArea, Long leaderID, Date birthday, String contactNumber, String groupAge, String name, String gender, String address, String journey) {
        this.id = id;
        this.clusterArea = clusterArea;
        this.leaderID = leaderID;
        this.birthday = birthday;
        this.contactNumber = contactNumber;
        this.groupAge = groupAge;
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.journey = journey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getIsLeader() {
        return isLeader;
    }

    public void setLeader(boolean leader) {
        isLeader = leader;
    }

    public String getHeadClusterArea() {
        return headClusterArea;
    }

    public void setHeadClusterArea(String headClusterArea) {
        this.headClusterArea = headClusterArea;
    }

    public String getClusterArea() {
        return clusterArea;
    }

    public void setClusterArea(String clusterArea) {
        this.clusterArea = clusterArea;
    }

    public Long getLeaderID() {
        return leaderID;
    }

    public void setLeaderID(Long leaderID) {
        this.leaderID = leaderID;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGroupAge() {
        return groupAge;
    }

    public void setGroupAge(String groupAge) {
        this.groupAge = groupAge;
    }

    public String getJourney() {
        return journey;
    }

    public void setJourney(String journey) {
        this.journey = journey;
    }

    public String getCldp() {
        return cldp;
    }

    public void setCldp(String cldp) {
        this.cldp = cldp;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", isLeader=" + isLeader +
                ", headClusterArea='" + headClusterArea + '\'' +
                ", clusterArea='" + clusterArea + '\'' +
                ", leaderID=" + leaderID +
                ", birthday=" + birthday +
                ", contactNumber='" + contactNumber + '\'' +
                ", groupAge='" + groupAge + '\'' +
                ", active=" + active +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", journey='" + journey + '\'' +
                ", cldp='" + cldp + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

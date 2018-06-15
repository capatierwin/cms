package com.cogclarkview.reporting.models;

import javax.persistence.*;
import java.util.Date;

@Entity(name="report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "report_id")
    private long reportID;

    @Column(name = "leader_id")
    private long leaderID;

    @Column(name = "is_verified")
    private boolean isVerified;

    @Column(name = "date_verified")
    private Date dateVerified;

    @Column(name = "cluster_area")
    private String clusterArea;

    @Column(name = "date_submitted")
    private Date dateSubmitted;

    @Column(name = "date_cg")
    private Date dateTimeCG;

    @Column(name = "consolidation_report")
    private String consolidationReport;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "c2s_leader")
    private String c2sLeader;

    private String topic;
    private long offering;
    private String present;
    private String absent;
    private String type;
    private String comment;

    public Report() {}

    public Report(long leaderID, boolean isVerified, Date dateVerified, String clusterArea, Date dateSubmitted, Date dateTimeCG, String consolidationReport, boolean isDeleted, String c2sLeader, String topic, long offering, String present, String absent, String type, String comment) {
        this.leaderID = leaderID;
        this.isVerified = isVerified;
        this.dateVerified = dateVerified;
        this.clusterArea = clusterArea;
        this.dateSubmitted = dateSubmitted;
        this.dateTimeCG = dateTimeCG;
        this.consolidationReport = consolidationReport;
        this.isDeleted = isDeleted;
        this.c2sLeader = c2sLeader;
        this.topic = topic;
        this.offering = offering;
        this.present = present;
        this.absent = absent;
        this.type = type;
        this.comment = comment;
    }

    public String getC2sLeader() {
        return c2sLeader;
    }

    public void setC2sLeader(String c2sLeader) {
        this.c2sLeader = c2sLeader;
    }

    public String getAbsent() {
        return absent;
    }

    public void setAbsent(String absent) {
        this.absent = absent;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public long getReportID() {
        return reportID;
    }

    public void setReportID(long reportID) {
        this.reportID = reportID;
    }

    public long getLeaderID() {
        return leaderID;
    }

    public void setLeaderID(long leaderID) {
        this.leaderID = leaderID;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDateVerified() {
        return dateVerified;
    }

    public void setDateVerified(Date dateVerified) {
        this.dateVerified = dateVerified;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClusterArea() {
        return clusterArea;
    }

    public void setClusterArea(String clusterArea) {
        this.clusterArea = clusterArea;
    }

    public Date getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(Date dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public Date getDateTimeCG() {
        return dateTimeCG;
    }

    public void setDateTimeCG(Date dateTimeCG) {
        this.dateTimeCG = dateTimeCG;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public long getOffering() {
        return offering;
    }

    public void setOffering(long offering) {
        this.offering = offering;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getConsolidationReport() {
        return consolidationReport;
    }

    public void setConsolidationReport(String consolidationReport) {
        this.consolidationReport = consolidationReport;
    }

}

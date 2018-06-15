package com.cogclarkview.reporting.services;

import com.cogclarkview.reporting.dao.AccountRepository;
import com.cogclarkview.reporting.dao.ReportRepository;
import com.cogclarkview.reporting.models.Account;
import com.cogclarkview.reporting.models.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Service
public class MainService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private SubService subService;

    public Account findOne(String username, String password) {
        for (Account account : accountRepository.findAll()) {
            if (
                    account.getUsername() != null &&
                    account.getUsername().equals(username) &&
                    account.getPassword().equals(password)
            ) if (account.getIsLeader() || account.getType().equals("master"))
                return account;
        }
        return null;
    }

    public List<Account> getAccountsByLeaderID(long id) {
        List<Account> members = new ArrayList<>();
        for (Account account : accountRepository.findAll()) {
            if (id == account.getLeaderID()) members.add(account);
        }
        return members;
    }

    public Account findAccountByID(long id) {
        for (Account account : accountRepository.findAll()) {
            if (account.getId() == id) return account;
        }
        return null;
    }

    public void saveReport(Report report) {
        reportRepository.save(report);
    }

    public List<Report> getAllLeadersReports(String type, String clusterArea) {
        List<Report> reports = new ArrayList<>();
        for (Report report : reportRepository.findAll()) {
            if (
                    !report.isVerified() &&
                    report.getType().equals(type) &&
                    report.getClusterArea().equals(clusterArea)
                ) reports.add(report);
        }
        return reports;
    }

    public Report findReportById(long reportID) {
        for (Report report : reportRepository.findAll()) {
            if (report.getReportID() == reportID) return report;
        }
        return null;
    }

    public List<Account> findAccountByReports(List<Report> reports) {
        List<Account> leaders = new ArrayList<>();
        for (Report report : reports) leaders.add(findAccountByID(report.getLeaderID()));
        return leaders;
    }

    public List<Account> getAttendanceFromCSV(String present) {
        List<Account> stringList = new ArrayList<>();
        for (String aPresentSplit : present.split(","))
            stringList.add(findAccountByID(Long.valueOf(aPresentSplit)));

        return stringList;
    }

    public String setAbsentsToString(String presentAttendance, Long leaderId) {
        List<Account> members = getAccountsByLeaderID(leaderId);
        List<String> absents = new ArrayList<>();
        List<String> presents = Arrays.asList(presentAttendance.split(","));
        for (Account account : members) absents.add(String.valueOf(account.getId()));
        absents.removeAll(presents);
        return subService.mergeAttendanceFromList(absents);
    }

    public List<Account> getClusterHeads() {
        List<Account> clusterAreas = new ArrayList<>();
        for(Account account : accountRepository.findAll()) {
            if (account.getHeadClusterArea() != null) clusterAreas.add(account);
        }
        return clusterAreas;
    }

    public List<Account> getAllLeaders() {
        List<Account> allLeaders = new ArrayList<>();
        for (Account account : accountRepository.findAll()) {
            if (account.getIsLeader()) allLeaders.add(account);
        }
        return allLeaders;
    }

    public void saveAccount(Account member) {
        accountRepository.save(member);
    }

    public List<String> getClusterAreas() {
        List<String> clusterAreas = new ArrayList<>();
        for (Account account : accountRepository.findAll()) {
            if (account.getType().equals("cluster head"))
                clusterAreas.add(account.getHeadClusterArea());
        }
        return clusterAreas;
    }

    public List<Report> getReportsByLeaderID(Long id) {
        List<Report> reports = new ArrayList<>();
        for (Report report : reportRepository.findAll()) {
            if (report.getLeaderID() == id && !report.isVerified())
                reports.add(report);
        }
        return reports;
    }

    public void deleteReport(Long report_id) {
        reportRepository.deleteById(report_id);
    }

    public List<Report> getAllVerifiedReports() {
        List<Report> allValidatedReports = new ArrayList<>();
        for (Report report : reportRepository.findAll()) {
            if (report.isVerified() && !report.isDeleted()) allValidatedReports.add(report);
        }
        return allValidatedReports;
    }

    public List<Account> getLeadersByLeaderIdInReports() {
        List<Account> leaders = new ArrayList<>();
        for (Report report : getAllVerifiedReports()) {
            leaders.add(findAccountByID(report.getLeaderID()));
        }
        return new ArrayList<>(new HashSet<>(leaders));
    }

    public List<String> getClusterAreasByMembers(List<Account> accountsByLeaderID) {
        List<String> areas = new ArrayList<>();
        for (Account account : accountsByLeaderID) areas.add(account.getClusterArea());
        return new ArrayList<>(new HashSet<>(areas));
    }

    private List<Account> getRegularMembersNames(Long leaderId, boolean is_regular) {
        List<Account> regular = new ArrayList<>();
        for (Account member : getAccountsByLeaderID(leaderId)) {
            if (is_regular && member.isActive()) regular.add(member);
            else if (!is_regular && !member.isActive()) regular.add(member);
        }
        return regular;
    }

    public Integer getNumberOfReports(String headClusterArea, String sunday) {
        List<Report> reports = getAllReportsByClusterArea(headClusterArea, sunday);
        if (reports.size() > 0) return reports.size();
        return 0;
    }

    private List<Report> getAllReportsByClusterArea(String headClusterArea, String sunday) {
        List<Report> reports = new ArrayList<>();
        for (Report report : reportRepository.findAll()) {
            if (report.getClusterArea().equals(headClusterArea) && report.getType().equals(sunday) && !report.isVerified())
                reports.add(report);
        }
        return reports;
    }

    public ResponseEntity<Object> getCGFile() {
        StringBuilder fileContent = new StringBuilder(
                "CG LEADER, CLUSTER AREA, REGULAR, IRREGULAR, TOTAL\n"
        );
        List<Account> allLeaders = getAllLeaders();
        List<Account> regulars;
        List<Account> irregulars;
        int maxLength;
        for (String clusterAreas : getClusterAreas()) {
            for (Account leader : allLeaders) {
                if (leader.getClusterArea().equals(clusterAreas)) {
                    regulars = getRegularMembersNames(leader.getId(), true);
                    irregulars = getRegularMembersNames(leader.getId(), false);
                    maxLength = Math.max(regulars.size(),irregulars.size());

                    fileContent.append(leader.getName());
                    if (leader.getType().equals("cluster head"))
                        fileContent.append(" - ").append(leader.getHeadClusterArea()).append(" Cluster Head");
                    fileContent.append(",").append(leader.getClusterArea()).append(",")
                            .append(regulars.size()).append(",").append(irregulars.size()).append(",")
                            .append(getAccountsByLeaderID(leader.getId()).size()).append("\n");

                    if (maxLength > 0) {
                        for (int i = 0; i < maxLength; i++) {
                            fileContent.append(",,");
                            if ((i+1) <= regulars.size()) fileContent.append(regulars.get(i).getName());
                            fileContent.append(",");
                            if ((i+1) <= irregulars.size()) fileContent.append(irregulars.get(i).getName());
                            fileContent.append("\n");
                        }
                    }
                    fileContent.append("\n");
                }
            }
        }
        return returnResponseEntity("\\CareGroups.csv", fileContent);
    }

    private ResponseEntity<Object> returnResponseEntity(String filename, StringBuilder fileContent) {
        File file = new File(filename);
        FileWriter fileWriter = null;
        InputStreamResource resource = null;
        try {
            fileWriter = new FileWriter(filename);
            fileWriter.write(fileContent.toString());
            fileWriter.flush();
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/txt"))
                .body(resource);
    }

    public ResponseEntity<Object> getReportsFile() {
        int max;
        String[] presentArray, absentArray;
        List<Account> presentList = new ArrayList<>();
        List<Account> absentList = new ArrayList<>();
        StringBuilder fileContent = new StringBuilder(
                        "CG/C2S LEADER'S NAME," +
                        "REPORT TYPE," +
                        "CLUSTER AREA," +
                        "TOPIC," +
                        "OFFERING," +
                        "PRESENT," +
                        "ABSENT," +
                        "MEETING DATE," +
                        "DATE SUBMITTED," +
                        "DATE VERIFIED," +
                        "CONSOLIDATION REPORT," +
                        "CLUSTER HEAD COMMENT\n"
        );
        List<Report> allCheckedReports = getAllCheckedReports(new ArrayList<>());
        if (allCheckedReports.size() > 0) {
            for (Report report : allCheckedReports) {
                presentArray = report.getPresent().split(",");
                absentArray = report.getAbsent().split(",");
                if (report.getType().equals("C2S")) fileContent.append(report.getC2sLeader());
                else fileContent.append(findAccountByID(report.getLeaderID()).getName());

                fileContent.append(",")
                        .append(report.getType()).append(",")
                        .append(report.getClusterArea()).append(",")
                        .append(report.getTopic()).append(",")
                        .append(report.getOffering()).append(",");

                if (report.getType().equals("C2S")) {
                    fileContent.append(presentArray.length).append(",")
                            .append(absentArray.length);
                } else {
                    presentList = getAttendanceFromCSV(report.getPresent());
                    absentList = getAttendanceFromCSV(report.getAbsent());
                    fileContent.append(presentList.size()).append(",").append(absentList.size());
                }
                fileContent.append(",")
                    .append(subService.convertDateToStr(report.getDateTimeCG(),"EEEE MMM dd yyyy hh:mm a"))
                    .append(",")
                    .append(subService.convertDateToStr(report.getDateSubmitted(),"EEEE MMM dd yyyy hh:mm a"))
                    .append(",")
                    .append(subService.convertDateToStr(report.getDateVerified(),"EEEE MMM dd yyyy hh:mm a"))
                    .append(",").append(report.getConsolidationReport()).append(",")
                    .append(report.getComment()).append("\n");
                max = Math.max(presentArray.length, absentArray.length);

                if (report.getType().equals("C2S")) {
                    for (int i = 0 ; i < max ; i++) {
                        fileContent.append(",,,,,");
                        if (presentArray.length > i) fileContent.append(presentArray[i]);
                        fileContent.append(",");
                        if (absentArray.length > i) fileContent.append(absentArray[i]);
                        fileContent.append("\n");
                    }
                } else {
                    for (int i = 0 ; i < max ; i++) {
                        fileContent.append(",,,,,");
                        if (presentList.size() > i) fileContent.append(presentList.get(i).getName());
                        fileContent.append(",");
                        if (absentList.size() > i) fileContent.append(absentList.get(i).getName());
                        fileContent.append("\n");
                    }
                }
                fileContent.append("\n");
            }
        }
        return returnResponseEntity("\\Reports.csv", fileContent);
    }

    private List<Report> getAllCheckedReports(ArrayList<Report> reports) {
        for (Report report : reportRepository.findAll()) {
            if (report.isDeleted() && report.isVerified())
                reports.add(report);
        }
        return reports;
    }

}

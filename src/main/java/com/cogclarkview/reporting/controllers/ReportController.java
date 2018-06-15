package com.cogclarkview.reporting.controllers;

import com.cogclarkview.reporting.models.Account;
import com.cogclarkview.reporting.models.Report;
import com.cogclarkview.reporting.services.MainService;
import com.cogclarkview.reporting.services.SubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class ReportController {

    @Autowired
    private MainService mainService;

    @Autowired
    private SubService subService;

    @GetMapping("/report")
    public String cgReport(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        request.setAttribute("message", session.getAttribute("message"));
        session.removeAttribute("message");
        Account account = (Account) session.getAttribute("account");
        if(account != null) {
            List<Account> members = mainService.getAccountsByLeaderID(account.getId());
            List<Account> c2sLeaders = new ArrayList<>();
            c2sLeaders.add(account);
            c2sLeaders.addAll(members);
            request.setAttribute("areas", mainService.getClusterAreas());
            session.setAttribute("account", account);
            request.setAttribute("members", members);
            request.setAttribute("c2sLeaders", c2sLeaders);
            request.setAttribute("clusters", mainService.getClusterAreasByMembers(mainService.getAccountsByLeaderID(account.getId())));
            request.setAttribute("account", account);
            if (account.getType().equals("admin"))
                request.setAttribute("totalReport", mainService.getAllVerifiedReports().size());
            return "report_form";
        }

        try { response.sendRedirect("login"); }
        catch (IOException e) { e.printStackTrace(); }
        return "login";
    }

    @PostMapping("/save_report")
    public String saveReport(
            @ModelAttribute Report report,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        HttpSession session = request.getSession();
        request.setAttribute("message", session.getAttribute("message"));
        session.removeAttribute("message");
        Account account = (Account) session.getAttribute("account");
        if (account != null) {
            session.setAttribute("account", account);
            request.setAttribute("account", account);
            Long id = (Long) session.getAttribute("reportID");
            if (id != null) report.setReportID(id);
            report.setDateSubmitted(subService.currentDateTime());
            report.setLeaderID(account.getId());

            if (request.getParameter("type").equals("C2S")) {
                report.setPresent(subService.mergeAttendance(request.getParameterValues("c2sPresentName")));
                report.setAbsent(subService.mergeAttendance(request.getParameterValues("c2sAbsentName")));
                report.setOffering(0);
            } else if (request.getParameterValues("attendance") != null){
                report.setPresent(subService.mergeAttendance(request.getParameterValues("attendance")));
                report.setAbsent(mainService.setAbsentsToString(report.getPresent(),report.getLeaderID()));
            }

            report.setDateTimeCG(
                    subService.changeDateFormat(
                            request.getParameter("dateCG") + " " +
                                    request.getParameter("timeCG") + ":00",
                            "yyyy-MM-dd HH:mm:ss")
            );

            try {
                mainService.saveReport(report);
            } catch (Exception e) {
                session.setAttribute("message", "Please double check your values.");
                try { response.sendRedirect("report"); }
                catch (IOException ex) { ex.printStackTrace(); }
                return "report_form";
            }

            request.setAttribute("reports", mainService.getReportsByLeaderID(account.getId()));
            session.setAttribute("message", "You've successfully submitted a " + report.getType() + " report." );
            try { response.sendRedirect(""); }
            catch (IOException e) { e.printStackTrace(); }
            return "index";
        }

        try { response.sendRedirect("login"); }
        catch (IOException e) { e.printStackTrace(); }
        return "login";
    }

    @GetMapping("reports")
    public String viewReports(
            @RequestParam String type,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        request.setAttribute("message", session.getAttribute("message"));
        session.removeAttribute("message");
        Account account = (Account) session.getAttribute("account");
        if (account != null && account.getType().equals("cluster head")) {
            String typeReport = subService.reportType(type);
            request.setAttribute("type", typeReport);
            List<Report> reports = mainService.getAllLeadersReports(typeReport, account.getHeadClusterArea());
            List<Account> leaders = mainService.findAccountByReports(reports);
            request.setAttribute("leaders", leaders);
            request.setAttribute("reports", reports);
            session.setAttribute("account", account);
            request.setAttribute("account", account);
            if (account.getType().equals("admin"))
                request.setAttribute("totalReport", mainService.getAllVerifiedReports().size());
            return "reports_list";
        }

        try { response.sendRedirect("login"); }
        catch (IOException e) { e.printStackTrace(); }
        return "login";
    }

    @GetMapping("view_report")
    public String viewReport(
            @RequestParam long reportID,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        HttpSession session = request.getSession();
        request.setAttribute("message", session.getAttribute("message"));
        session.removeAttribute("message");
        Account account = (Account) session.getAttribute("account");
        if (account != null) {
            Report report = mainService.findReportById(reportID);
            String leaderName = mainService.findAccountByID(report.getLeaderID()).getName();
            request.setAttribute("dateCG", subService.convertDateToStr(report.getDateTimeCG(),"EEEE, MMM dd, yyyy hh:mm a"));
            request.setAttribute("dateSubmitted", subService.convertDateToStr(report.getDateSubmitted(),"EEEE, MMM dd, yyyy hh:mm a"));
            request.setAttribute("leaderName", leaderName);
            request.setAttribute("report", report);

            if (report.getType().equals("C2S")) {
                request.setAttribute("present", subService.removeNullFromAttendance(report.getPresent().split(",")));
                request.setAttribute("absent", subService.removeNullFromAttendance(report.getAbsent().split(",")));
            } else {
                request.setAttribute("present", mainService.getAttendanceFromCSV(report.getPresent()));
                request.setAttribute("absent", mainService.getAttendanceFromCSV(report.getAbsent()));
            }

            if (account.getType().equals("admin"))
                request.setAttribute("totalReport", mainService.getAllVerifiedReports().size());

            session.setAttribute("account", account);
            request.setAttribute("account", account);
            return "view_report";
        }

        try { response.sendRedirect("login"); }
        catch (IOException e) { e.printStackTrace(); }
        return "login";
    }

    @PostMapping("validate_report")
    public String validateReport(
            @RequestParam long reportID,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        request.setAttribute("message", session.getAttribute("message"));
        session.removeAttribute("message");
        Account account = (Account) session.getAttribute("account");
        if (account != null) {
            Report report = mainService.findReportById(reportID);
            report.setDateVerified(subService.currentDateTime());
            report.setComment(request.getParameter("comment"));
            report.setVerified(true);
            mainService.saveReport(report);
            session.setAttribute("account", account);
            request.setAttribute("account", account);
            String type = subService.reportType(report.getType());
            session.setAttribute("message", "You've successfully verified " +
                    mainService.findAccountByID(report.getLeaderID()).getName() +
                    "'s " + report.getType() + " report." );
            try { response.sendRedirect("reports?type=" + type); }
            catch (IOException e) { e.printStackTrace(); }
            return "reports_list";
        }

        try { response.sendRedirect("login"); }
        catch (IOException e) { e.printStackTrace(); }
        return "login";
    }

    @GetMapping("update_report")
    public String updateReport(
            @RequestParam Long report_id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        HttpSession session = request.getSession();
        request.setAttribute("message", session.getAttribute("message"));
        session.removeAttribute("message");
        Account account = (Account) session.getAttribute("account");
        if (account != null) {
            Report report = mainService.findReportById(report_id);
            List<Account> members = mainService.getAccountsByLeaderID(account.getId());
            request.setAttribute("members", members);
            request.setAttribute("areas", mainService.getClusterAreas());
            request.setAttribute("report", report);
            request.setAttribute("clusters", mainService.getClusterAreasByMembers(mainService.getAccountsByLeaderID(account.getId())));
            request.setAttribute("date",
                    subService.convertDateToStr(
                            report.getDateTimeCG(),
                            "yyyy-MM-dd"
                    )
            );
            request.setAttribute("time",
                    subService.convertDateToStr(
                            report.getDateTimeCG(),
                            "HH:mm:ss"
                    )
            );

            if (report.getType().equals("C2S")) {
                List<Account> c2sLeaders = new ArrayList<>();
                c2sLeaders.add(account);
                c2sLeaders.addAll(members);
                request.setAttribute("c2sLeaders", c2sLeaders);
            }
            session.setAttribute("reportID", report_id);
            session.setAttribute("account", account);
            request.setAttribute("account", account);
            if (account.getType().equals("admin"))
                request.setAttribute("totalReport", mainService.getAllVerifiedReports().size());
            return "report_form";
        }

        try { response.sendRedirect("login"); }
        catch (IOException e) { e.printStackTrace(); }
        return "login";
    }

    @GetMapping("delete_report")
    public String deleteReport(
            @RequestParam Long report_id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        HttpSession session = request.getSession();
        request.setAttribute("message", session.getAttribute("message"));
        session.removeAttribute("message");
        Account account = (Account) session.getAttribute("account");
        if (account != null) {
            session.setAttribute("message", "You've successfully deleted a " +
                    mainService.findReportById(report_id).getType() + " report." );
            mainService.deleteReport(report_id);
            try { response.sendRedirect(""); }
            catch (IOException e) { e.printStackTrace(); }
            return "index";
        }

        try { response.sendRedirect("login"); }
        catch (IOException e) { e.printStackTrace(); }
        return "login";
    }

    @GetMapping("verified_reports")
    public String showVerifiedReports(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        request.setAttribute("message", session.getAttribute("message"));
        session.removeAttribute("message");
        Account account = (Account) session.getAttribute("account");
        if (account != null && account.getType().equals("admin")) {
            request.setAttribute("verifiedReports",mainService.getAllVerifiedReports());
            request.setAttribute("heads", mainService.getClusterHeads());
            request.setAttribute("leaders", mainService.getLeadersByLeaderIdInReports());

            session.setAttribute("account", account);
            request.setAttribute("account", account);
            return "verified_reports";
        }

        try { response.sendRedirect("login"); }
        catch (IOException e) { e.printStackTrace(); }
        return "login";
    }

    @GetMapping("view_verified_report")
    public String showVerifiedReportsForm(
            @RequestParam Long report_id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        HttpSession session = request.getSession();
        request.setAttribute("message", session.getAttribute("message"));
        session.removeAttribute("message");
        Account account = (Account) session.getAttribute("account");
        if (account != null && account.getType().equals("admin")) {
            Report report = mainService.findReportById(report_id);
            request.setAttribute("report", report);
            request.setAttribute("leaderName", mainService.findAccountByID(report.getLeaderID()).getName());
            request.setAttribute("dateCG", subService.convertDateToStr(report.getDateTimeCG(),"EEEE, MMM dd, yyyy hh:mm a"));
            request.setAttribute("dateSubmitted", subService.convertDateToStr(report.getDateSubmitted(),"EEEE, MMM dd, yyyy hh:mm a"));
            request.setAttribute("dateVerified", subService.convertDateToStr(report.getDateVerified(),"EEEE, MMM dd, yyyy hh:mm a"));

            if (report.getType().equals("C2S")) {
                request.setAttribute("present", report.getPresent().split(","));
            } else {
                request.setAttribute("present", mainService.getAttendanceFromCSV(report.getPresent()));
                request.setAttribute("absent", mainService.getAttendanceFromCSV(report.getAbsent()));
            }
            if (account.getType().equals("admin"))
                request.setAttribute("totalReport", mainService.getAllVerifiedReports().size());

            session.setAttribute("account", account);
            request.setAttribute("account", account);
            return "view_report";
        }

        try { response.sendRedirect("login"); }
        catch (IOException e) { e.printStackTrace(); }
        return "login";
    }

    @GetMapping("check_report")
    public String checkReport(
            @RequestParam Long report_id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        HttpSession session = request.getSession();
        request.setAttribute("message", session.getAttribute("message"));
        session.removeAttribute("message");
        Account account = (Account) session.getAttribute("account");
        if (account != null && account.getType().equals("admin")) {
            Report report = mainService.findReportById(report_id);
            report.setDeleted(true);
            mainService.saveReport(report);
            session.setAttribute("message", "You've successfully checked " +
                    mainService.findAccountByID(report.getLeaderID()).getName() +
                    "'s " + report.getType() + " report." );
            try { response.sendRedirect("verified_reports"); }
            catch (IOException e) { e.printStackTrace(); }
            return "verified_reports";
        }

        try { response.sendRedirect("login"); }
        catch (IOException e) { e.printStackTrace(); }
        return "login";
    }

}

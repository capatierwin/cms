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
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private MainService mainService;

    @Autowired
    private SubService subService;

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        request.removeAttribute("account");
        HttpSession session = request.getSession();
        session.removeAttribute("account");
        session.invalidate();
        return "login";
    }

    @GetMapping("/")
    public String getHome(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        request.setAttribute("message", session.getAttribute("message"));
        session.removeAttribute("message");
        if (account != null) {
            request.setAttribute("reports", mainService.getReportsByLeaderID(account.getId()));
            request.setAttribute("members", mainService.getAccountsByLeaderID(account.getId()));
            request.setAttribute("areas", mainService.getClusterAreasByMembers(mainService.getAccountsByLeaderID(account.getId())));

            if (account.getType().equals("cluster head")) {
                Integer sundayReport = mainService.getNumberOfReports(account.getHeadClusterArea(), "Sunday");
                Integer CGReport = mainService.getNumberOfReports(account.getHeadClusterArea(), "Care Group");
                Integer C2SReport = mainService.getNumberOfReports(account.getHeadClusterArea(), "C2S");
                Integer totalReport = sundayReport + CGReport + C2SReport;
                request.setAttribute("totalReport", totalReport);
                request.setAttribute("noOfSundayReports", sundayReport);
                request.setAttribute("noOfCGReports", CGReport);
                request.setAttribute("noOfC2SReports", C2SReport);
            } else if (account.getType().equals("admin"))
                request.setAttribute("totalReport", mainService.getAllVerifiedReports().size());

            session.setAttribute("account", account);
            request.setAttribute("account", account);
            return "index";
        }

        try { response.sendRedirect("login"); }
        catch (IOException e) { e.printStackTrace(); }
        return "login";
    }

    @PostMapping("/")
    public String postHome(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Account account = mainService.findOne(request.getParameter("username"), request.getParameter("password"));

        if(account != null) {
            session.setAttribute("account", account);
            request.setAttribute("account", account);
            if (account.getType().equals("master")) {
                try { response.sendRedirect("create"); }
                catch (IOException e) { e.printStackTrace(); }
                return "create";
            }

            if (account.getType().equals("cluster head")) {
                Integer sundayReport = mainService.getNumberOfReports(account.getHeadClusterArea(), "Sunday");
                Integer CGReport = mainService.getNumberOfReports(account.getHeadClusterArea(), "Care Group");
                Integer C2SReport = mainService.getNumberOfReports(account.getHeadClusterArea(), "C2S");
                Integer totalReport = sundayReport + CGReport + C2SReport;
                request.setAttribute("totalReport", totalReport);
                request.setAttribute("noOfSundayReports", sundayReport);
                request.setAttribute("noOfCGReports", CGReport);
                request.setAttribute("noOfC2SReports", C2SReport);
            } else if (account.getType().equals("admin"))
                request.setAttribute("totalReport", mainService.getAllVerifiedReports().size());

            request.setAttribute("areas", mainService.getClusterAreasByMembers(mainService.getAccountsByLeaderID(account.getId())));
            request.setAttribute("members", mainService.getAccountsByLeaderID(account.getId()));
            request.setAttribute("reports", mainService.getReportsByLeaderID(account.getId()));
            return "index";
        }
        try { response.sendRedirect("login"); }
        catch (IOException e) { e.printStackTrace(); }
        return "login";
    }

    @GetMapping("cg_list")
    public String CGList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        request.setAttribute("message", session.getAttribute("message"));
        session.removeAttribute("message");
        if (account != null && account.getType().equals("admin")) {
            request.setAttribute("leaders", mainService.getAllLeaders());
            request.setAttribute("heads", mainService.getClusterHeads());
            session.setAttribute("account", account);
            request.setAttribute("account", account);
            request.setAttribute("totalReport", mainService.getAllVerifiedReports().size());
            return "cg_list";
        }

        try { response.sendRedirect("login"); }
        catch (IOException e) { e.printStackTrace(); }
        return "login";
    }

    @PostMapping("save_account")
    public String saveAccount(
            @ModelAttribute Account account,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        account.setBirthday(
                subService.changeDateFormat(
                        request.getParameter("birthday"),
                        "yyyy-MM-dd"
                )
        );
        account.setLeader(Boolean.parseBoolean(request.getParameter("isLeader")));
        if (account.getUsername().equals("")) account.setUsername(null);
        if (account.getPassword().equals("")) account.setPassword(null);
        if (account.getHeadClusterArea().equals("")) account.setHeadClusterArea(null);
        mainService.saveAccount(account);

        try { response.sendRedirect("create"); }
        catch (IOException e) { e.printStackTrace(); }
        return "create";
    }

    @GetMapping("create")
    public String createAccount(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account != null && account.getType().equals("master")) {
            session.setAttribute("account", account);
            request.setAttribute("account", account);
            if (request.getAttribute("return") != null) return "change";
            return "create";
        }

        try { response.sendRedirect("login"); }
        catch (IOException e) { e.printStackTrace(); }
        return "login";
    }

    @GetMapping("change")
    public String changeAccount(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        request.setAttribute("return", "change");
        return createAccount(request, response);
    }

    @PostMapping("save_update")
    public String saveChange(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Account account = mainService.findAccountByID(Long.parseLong(request.getParameter("id")));
        account.setUsername(request.getParameter("username"));
        account.setPassword(request.getParameter("password"));
        mainService.saveAccount(account);

        try { response.sendRedirect("/change"); }
        catch (IOException e) { e.printStackTrace(); }
        return "change";
    }

    @GetMapping("change_credentials")
    public String changeCredentials(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        request.setAttribute("message", session.getAttribute("message"));
        session.removeAttribute("message");
        if (account != null) {
            session.setAttribute("account", account);
            request.setAttribute("account", account);
            if (account.getType().equals("admin"))
                request.setAttribute("totalReport", mainService.getAllVerifiedReports().size());
            return "change_credentials";
        }

        try { response.sendRedirect("login"); }
        catch (IOException e) { e.printStackTrace(); }
        return "login";
    }

    @PostMapping("save_credentials")
    public String saveCredentials(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        request.setAttribute("message", session.getAttribute("message"));
        session.removeAttribute("message");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Account account = (Account) session.getAttribute("account");
        if (account != null) {
            if (mainService.findOne(username, password) != null) {
                session.setAttribute("message", "Please try another credentials.");
                session.setAttribute("account", account);
                request.setAttribute("account", account);
                try { response.sendRedirect("change_credentials"); }
                catch (IOException e) { e.printStackTrace(); }
                return "change_credentials";
            }
            account.setUsername(username);
            account.setPassword(password);
            mainService.saveAccount(account);

            if (account.getType().equals("master")) {
                try { response.sendRedirect("create"); }
                catch (IOException e) { e.printStackTrace(); }
                return "create";
            }

            session.setAttribute("message", "You've successfully updated your credentials.");
            try { response.sendRedirect(""); }
            catch (IOException e) { e.printStackTrace(); }
            return "index";
        }

        try { response.sendRedirect("login"); }
        catch (IOException e) { e.printStackTrace(); }
        return "login";
    }

}

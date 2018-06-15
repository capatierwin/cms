package com.cogclarkview.reporting.controllers;

import com.cogclarkview.reporting.models.Account;
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
public class MemberController {

    @Autowired
    private MainService mainService;

    @Autowired
    private SubService subService;

    @GetMapping("/member")
    public String showMembersData(
            @RequestParam long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        HttpSession session = request.getSession();
        request.setAttribute("message", session.getAttribute("message"));
        session.removeAttribute("message");
        Account account = (Account) session.getAttribute("account");
        if (account != null) {
            Account worker = mainService.findAccountByID(id);
            List<Account> members = mainService.getAccountsByLeaderID(id);
            if (account.getType().equals("admin"))
                request.setAttribute("totalReport", mainService.getAllVerifiedReports().size());
            request.setAttribute("leader", mainService.findAccountByID(worker.getLeaderID()));
            request.setAttribute("bday", subService.convertDateToStr(worker.getBirthday(), "MMM dd, yyyy"));
            request.setAttribute("age", subService.getAge(worker.getBirthday()));
            session.setAttribute("account", account);
            request.setAttribute("areas", mainService.getClusterAreasByMembers(mainService.getAccountsByLeaderID(worker.getId())));
            request.setAttribute("account", account);
            request.setAttribute("regular", subService.getRegularMembers(members));
            request.setAttribute("irregular", subService.getIrregularMembers(members));
            request.setAttribute("members", members);
            request.setAttribute("worker", worker);
            session.setAttribute("worker", worker);
            return "member";
        }

        try { response.sendRedirect("login"); }
        catch (IOException e) { e.printStackTrace(); }
        return "login";
    }

    @GetMapping("/add_member")
    public String add_member(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        request.setAttribute("message", session.getAttribute("message"));
        session.removeAttribute("message");
        Account account = (Account) session.getAttribute("account");
        if (account != null) {
            if (account.getType().equals("admin"))
                request.setAttribute("totalReport", mainService.getAllVerifiedReports().size());
            session.setAttribute("account", account);
            request.setAttribute("account", account);
            request.setAttribute("areas", mainService.getClusterAreas());
            return "member_form";
        }

        try { response.sendRedirect("login"); }
        catch (IOException e) { e.printStackTrace(); }
        return "login";
    }

    @PostMapping("save_member")
    public String saveMember(
            @ModelAttribute Account member,
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
            member.setBirthday(
                    subService.changeDateFormat(
                            request.getParameter("birthday"),
                            "yyyy-MM-dd"
                    )
            );

            if (session.getAttribute("memberID") != null) {
                Account worker = mainService.findAccountByID((Long) session.getAttribute("memberID"));
                if (!worker.getIsLeader() && member.getJourney().equals("Leader") && worker.getUsername() == null) {
                    Account updatedAccount = new Account(
                            worker.getId(), member.getClusterArea(), worker.getLeaderID(), member.getBirthday(),
                            member.getContactNumber(), member.getGroupAge(), member.getName(), member.getGender(),
                            member.getAddress(), member.getJourney()
                    );
                    updatedAccount.setActive(Boolean.parseBoolean(request.getParameter("active")));
                    session.setAttribute("updatedAccount", updatedAccount);
                    try { response.sendRedirect("set_credentials"); }
                    catch (IOException e) { e.printStackTrace(); }
                    return "credentials";
//                } else if (worker.getIsLeader()) {
                } else {
                    worker.setActive(Boolean.parseBoolean(request.getParameter("active")));
                    worker.setName(member.getName());
                    worker.setAddress(member.getAddress());
                    worker.setClusterArea(member.getClusterArea());
                    worker.setBirthday(member.getBirthday());
                    worker.setContactNumber(member.getContactNumber());
                    worker.setGender(member.getGender());
                    worker.setGroupAge(member.getGroupAge());
                    worker.setJourney(member.getJourney());
                    session.setAttribute("message", "You've successfully updated the profile of " + worker.getName()+ "." );
                    mainService.saveAccount(worker);
                }
            } else if (member.getJourney().equals("Leader")) {
                member.setActive(Boolean.parseBoolean(request.getParameter("active")));
                member.setLeaderID(account.getId());
                session.setAttribute("updatedAccount", member);
                try { response.sendRedirect("set_credentials"); }
                catch (IOException e) { e.printStackTrace(); }
                return "credentials";
            } else if (member.getType() == null) {
                member.setType("member");
                member.setLeaderID(account.getId());
                member.setId((Long) session.getAttribute("updateAccountID"));
                member.setActive(Boolean.parseBoolean(request.getParameter("active")));
                session.setAttribute("message", "You've successfully added " +
                        member.getName() + " as your member." );
                mainService.saveAccount(member);
            }
            try { response.sendRedirect("/"); }
            catch (IOException e) { e.printStackTrace(); }
            return "index";
        }
        try { response.sendRedirect("login"); }
        catch (IOException e) { e.printStackTrace(); }
        return "login";
    }

    @GetMapping("update_member")
    public String updateMember(
            @RequestParam long id,
            @RequestParam long leaderID,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        request.setAttribute("message", session.getAttribute("message"));
        session.removeAttribute("message");
        Account account = (Account) session.getAttribute("account");
        if (account != null && leaderID == account.getId()) {
            Account member = mainService.findAccountByID(id);
            request.setAttribute("member", member);
            request.setAttribute("bday",
                            subService.convertDateToStr(
                                    member.getBirthday(),
                                    "yyyy-MM-dd"
                            )
            );
            if (account.getType().equals("admin"))
                request.setAttribute("totalReport", mainService.getAllVerifiedReports().size());
            request.setAttribute("areas", mainService.getClusterAreas());
            session.setAttribute("memberID", id);
            session.setAttribute("account", account);
            request.setAttribute("account", account);
            if (account.getType().equals("admin"))
                request.setAttribute("totalReport", mainService.getAllVerifiedReports().size());
            return "member_form";
        }

        try { response.sendRedirect("login"); }
        catch (IOException e) { e.printStackTrace(); }
        return "login";
    }

    @GetMapping("set_credentials")
    public String setCredentials(
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        request.setAttribute("message", session.getAttribute("message"));
        session.removeAttribute("message");
        Account account = (Account) session.getAttribute("account");
        if (account != null) {
            session.setAttribute("updatedAccount", session.getAttribute("updatedAccount"));
            session.setAttribute("account", account);
            request.setAttribute("account", account);
            if (account.getType().equals("admin"))
                request.setAttribute("totalReport", mainService.getAllVerifiedReports().size());
            return "credentials";
        }

        try { response.sendRedirect("login"); }
        catch (IOException e) { e.printStackTrace(); }
        return "login";
    }

    @PostMapping("set_leader")
    public String setLeader(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        HttpSession session = request.getSession();
        request.setAttribute("message", session.getAttribute("message"));
        session.removeAttribute("message");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Account account = (Account) session.getAttribute("account");
        if (account != null) {
            session.setAttribute("account", account);
            request.setAttribute("account", account);
            Account updatedAccount = (Account) session.getAttribute("updatedAccount");
            if (updatedAccount == null)
                updatedAccount = (Account) session.getAttribute("worker");

            if (mainService.findOne(username,password) != null) {
                session.setAttribute("message", "Please try another credentials.");
                try { response.sendRedirect("set_credentials"); }
                catch (IOException e) { e.printStackTrace(); }
                return "credentials";
            }


            updatedAccount.setUsername(username);
            updatedAccount.setPassword(password);
            updatedAccount.setType("leader");
            updatedAccount.setLeader(true);
            updatedAccount.setActive(true);

            mainService.saveAccount(updatedAccount);
            session.setAttribute("message", "You've successfully set " + updatedAccount.getName() + " as leader.");
            try { response.sendRedirect("/"); }
            catch (IOException e) { e.printStackTrace(); }
            return "index";
        }

        try { response.sendRedirect("login"); }
        catch (IOException e) { e.printStackTrace(); }
        return "login";
    }


}

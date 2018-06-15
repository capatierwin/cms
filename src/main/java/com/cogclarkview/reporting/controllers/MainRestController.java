package com.cogclarkview.reporting.controllers;

import com.cogclarkview.reporting.models.Account;
import com.cogclarkview.reporting.services.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

@RestController
public class MainRestController {

    @Autowired
    private MainService mainService;

    @GetMapping("/dl_cg")
    public ResponseEntity<Object> downloadCGFile(HttpServletResponse response, HttpServletRequest request) throws IOException {
        Account account = (Account) request.getSession().getAttribute("account");
        if (account != null && account.getType().equals("admin")) return mainService.getCGFile();
        try { response.sendRedirect("/"); }
        catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    @GetMapping("/dl_reports")
    public ResponseEntity<Object> downloadReportsFile(HttpServletRequest request, HttpServletResponse response) {
        Account account = (Account) request.getSession().getAttribute("account");
        if (account != null && account.getType().equals("admin")) return mainService.getReportsFile();
        try { response.sendRedirect("/"); }
        catch (IOException e) { e.printStackTrace(); }
        return null;
    }
}
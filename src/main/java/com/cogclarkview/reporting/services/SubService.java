package com.cogclarkview.reporting.services;

import com.cogclarkview.reporting.models.Account;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class SubService {

    public int getAge(Date birthday) {
        LocalDate now = LocalDate.now();
        Instant instant = Instant.ofEpochMilli(birthday.getTime());
        LocalDate bday =
                LocalDateTime
                        .ofInstant(instant, ZoneId.systemDefault())
                        .toLocalDate();
        if (
                (now.getMonthValue() > bday.getMonthValue()) ||
                        (
                                (now.getMonthValue() == bday.getMonthValue()) &&
                                (now.getDayOfMonth() >= bday.getDayOfMonth())
                        )
        ) { return ( now.getYear() - bday.getYear() ); }

        return ( now.getYear() - bday.getYear() - 1 );
    }

    public String mergeAttendance(String[] attendances) {
        StringBuilder attendance = new StringBuilder();
        for (int i = 0; i < attendances.length; i++) {
            if (i == (attendances.length - 1)) attendance.append(attendances[i]);
            else attendance.append(attendances[i]).append(",");
        }
        return attendance.toString();
    }

    public String mergeAttendanceFromList(List<String> attendances) {
        StringBuilder attendance = new StringBuilder();
        for (int i = 0; i < attendances.size(); i++) {
            if (i == (attendances.size() - 1)) attendance.append(attendances.get(i));
            else attendance.append(attendances.get(i)).append(",");
        }
        return attendance.toString();
    }

    public Date changeDateFormat(String s, String format) {
        DateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date currentDateTime() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy HH:mm aaa");
        return changeDateFormat(dateFormat.format(timestamp), "EEE, MMM dd, yyyy HH:mm aaa");
    }

    public String convertDateToStr(Date date, String dFormat) {
        return new SimpleDateFormat(dFormat).format(date);
    }

    public int getRegularMembers(List<Account> members) {
        int regular = 0;
        for (Account account : members) {
            if (account.isActive()) regular++;
        }
        return regular;
    }

    public int getIrregularMembers(List<Account> members) {
        int irrgegular = 0;
        for (Account account : members) {
            if (!account.isActive()) irrgegular++;
        }
        return irrgegular;
    }

    public List<String> removeNullFromAttendance(String[] split) {
        List<String> temp = new ArrayList<>();
        for (String values : split) if (values.length() > 0) temp.add(values);
        return temp;
    }

    public String reportType(String type) {
        switch (type) {
            case "sunday":
                return "Sunday";
            case "cg":
                return"Care Group";
            default:
                return "C2S";
        }
    }
}

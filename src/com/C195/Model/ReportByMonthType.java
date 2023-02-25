package com.C195.Model;

import com.C195.helper.DateTimeProcessing;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;

/**
 * @author brandonLackey
 */
public class ReportByMonthType {
    private String month;
    private final String type;
    private final Integer count;
    private static final ObservableList<ReportByMonthType> allReportItems = FXCollections.observableArrayList();

    /**
     *
     * @param month
     * @param type
     * @param count
     */
    public ReportByMonthType(String month, String type, Integer count) {
        this.month = month;
        this.type = type;
        this.count = count;
    }

    /**
     *
     * @return
     */
    public static ObservableList<ReportByMonthType> getAllReportItems() {return allReportItems;}

    /**
     *
     * @param newReportItem
     */
    public static void addReportItem(ReportByMonthType newReportItem) {allReportItems.add(newReportItem);}

    /**
     *
     * @return
     */
    public String getMonth() {return month;}

    /**
     *
     * @return
     */
    public String getType() {return type;}

    /**
     *
     * @param newType
     */
    public void setType(String newType) {this.month = newType;}

    /**
     *
     * @return
     */
    public Integer getCount() {return count;}

    /**
     *
     */
    public static void getReportItems() {
        int count = 0;
        List<String> uniqueReportItems = new ArrayList<>();
        for (Appointment apt : Appointment.getAllapts()) {
            String[] splitStartDateTime = DateTimeProcessing.splitDateTime(apt.getAptStartDateTime());
            String[] splitStartDate = splitStartDateTime[0].split("-");
            String reportItem = splitStartDate[0] + "-" + splitStartDate[1] + "|" + apt.getAptType();
            if (!uniqueReportItems.contains(reportItem)) {
                uniqueReportItems.add(reportItem);
            }
        }
        for (String rptItem : uniqueReportItems) {
            int countRpts = 0;
            for (Appointment apt : Appointment.getAllapts()) {
                String[] splitStartDateTime = DateTimeProcessing.splitDateTime(apt.getAptStartDateTime());
                String[] splitStartDate = splitStartDateTime[0].split("-");
                String reportItem = splitStartDate[0] + "-" + splitStartDate[1] + "|" + apt.getAptType();
                if (reportItem.equals(rptItem)) {
                    countRpts++;
                }
            }
            String[] outItemArr = rptItem.split("\\|");
            ReportByMonthType.addReportItem(new ReportByMonthType(outItemArr[0],outItemArr[1],countRpts));
        }
    }
}

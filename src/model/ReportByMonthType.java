package model;

import helper.DateTimeProcessing;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Division Class is developed to manage report by month objects for the report by month table view report.
 * @author brandonLackey
 */
public class ReportByMonthType {
    private final String month;
    private final String type;
    private final Integer count;
    private static final ObservableList<ReportByMonthType> allReportItems = FXCollections.observableArrayList();

    /**
     * Sets reference variables used to refer to ReportByMonthType class constructor.
     *
     * @param month Report month string compiled from appointment objects
     * @param type Report type string compiled from appointment objects
     * @param count Report count of returns by month and type
     */
    public ReportByMonthType(String month, String type, Integer count) {
        this.month = month;
        this.type = type;
        this.count = count;
    }

    /**
     * Retries all report items from the allReportItems ObservableList.
     * @return Returns an observableList containing all report items.
     */
    public static ObservableList<ReportByMonthType> getAllReportItems() {return allReportItems;}

    /**
     * Adds a new report item to the allReportItems ObservableList.
     *
     * @param newReportItem All report item object to add to the allReportItems ObservableList.
     */
    public static void addReportItem(ReportByMonthType newReportItem) {allReportItems.add(newReportItem);}

    /**
     * getter for the report item month
     *
     * @return Returns the report item month as a string
     */
    public String getMonth() {return month;}

    /**
     * getter for the report item type
     *
     * @return Returns the report item type as a string
     */
    public String getType() {return type;}

    /**
     * getter for the report item count
     *
     * @return Returns the report item count as an integer
     */
    public Integer getCount() {return count;}

    /**
     * Retrives report items by combing the year, month, and type as a string then counts and removes duplicates. The function
     * then creates ReportByMonthType objects based on these results.
     */
    public static void getReportItems() {
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

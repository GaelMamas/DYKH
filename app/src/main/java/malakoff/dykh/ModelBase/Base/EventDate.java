package malakoff.dykh.ModelBase.Base;

/**
 * Created by gael on 14/02/2018.
 */

public class EventDate {

    private String day;
    private String month;
    private String year;
    private String mBCAD;

    public EventDate(String mBCAD, String year, String month, String day) {

        this.mBCAD = mBCAD;
        this.year = year;
        this.month = month;
        this.day = day;

    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getmBCAD() {
        return mBCAD;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setmBCAD(String mBCAD) {
        this.mBCAD = mBCAD;
    }
}

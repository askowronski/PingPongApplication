package app.ViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by askowronski on 12/5/17.
 */
public class DateRange {

    public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public Date beginning;
    public Date end;

    public DateRange(Date beginning, Date end) {
        this.beginning = beginning;
        this.end = end;
    }

    public Date getBeginning() {
        return beginning;
    }

    public void setBeginning(Date beginning) {
        this.beginning = beginning;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getBeginningString() {
        return df.format(this.beginning);
    }

    public String getEndString() {
        return df.format(this.end);
    }
}

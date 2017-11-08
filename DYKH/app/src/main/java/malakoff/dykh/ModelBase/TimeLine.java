package malakoff.dykh.ModelBase;

import android.text.TextUtils;

import java.util.List;

import malakoff.dykh.ModelBase.Base.EraTime;


/**
 * Created by user on 01/01/2016.
 */
public class TimeLine extends EraTime {

    private List<EraTime> eraTimeList;

    public TimeLine(List<EraTime> eraTimeList) throws IllegalArgumentException{

        super(eraTimeList.get(0).getBeginnig(), eraTimeList.get(eraTimeList.size()-1).getDuration(), eraTimeList.get(0).getTitle());

        if (eraTimeList.isEmpty()) {
            throw new IllegalArgumentException("Impossible to create a period of time with these arguments");
        }

        this.eraTimeList = eraTimeList;

    }

    public List<EraTime> getEraTimeList() {
        return eraTimeList;
    }

    public void setEraTimeList(List<EraTime> eraTimeList) {
        this.eraTimeList = eraTimeList;
    }
}

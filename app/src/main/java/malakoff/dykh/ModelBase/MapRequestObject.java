package malakoff.dykh.ModelBase;

import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by vm32776n on 23/02/2017.
 */

public class MapRequestObject{
    private MarkerOptions markerOptions;
    private String errorContent;
    private boolean isRequestSucceeded;

    public MapRequestObject(MarkerOptions markerOptions, String errorContent, boolean isRequestSucceeded) {
        this.markerOptions = markerOptions;
        this.errorContent = errorContent;
        this.isRequestSucceeded = isRequestSucceeded;
    }

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    public String getErrorContent() {
        return errorContent;
    }

    public boolean isRequestSucceeded() {
        return isRequestSucceeded;
    }
}

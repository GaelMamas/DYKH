package malakoff.dykh.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import malakoff.dykh.Activities.Base.BaseDawerActivity;
import malakoff.dykh.AppApplication.Constants;
import malakoff.dykh.Event.Event;
import malakoff.dykh.Fragments.Base.InstanceBaseFragement;
import malakoff.dykh.Fragments.Base.WriteEventBaseFragment;
import malakoff.dykh.R;
import malakoff.dykh.Utils.UsefulGenericMethods;

/**
 * Created by user on 12/07/2016.
 */
public class ModifyEventFragment extends WriteEventBaseFragment{

    private Event currentEvent;


    @Override
    protected void populateViews(Bundle savedInstanceState) {
        super.populateViews(savedInstanceState);

        currentEvent = mEventManager.readSpecificEvent(getArguments().getString(Constants.DYKH_FRAGMENT_SELECTION));

        if(getActivity() instanceof BaseDawerActivity){
            ((BaseDawerActivity)getActivity()).setDefaultDrawable();
            ((BaseDawerActivity) getActivity()).changeToolbarConfig(R.drawable.ic_arrow_back_24dp,
                    currentEvent != null && !TextUtils.isEmpty(currentEvent.getTitle())?
                            currentEvent.getTitle():getString(R.string.app_name));
        }

        if(currentEvent != null){
            titleEditText.setText(currentEvent.getTitle());
            List<String> themes = Arrays.asList(getResources().getStringArray(R.array.event_themes));
            if(themes.contains(currentEvent.getTheme())){
                themeSprinner.setSelection(themes.indexOf(currentEvent.getTheme()));
            }

            storyEditText.setText(currentEvent.getStory());

            List<String> todayLocations = UsefulGenericMethods.getWorldCountriesList();
            //TODO Change current.getLoction() into current.getLocationModernCalling()
            if(todayLocations.contains(currentEvent.getLocation())){
                try {
                    todayLocationSpinner.setSelection(themes.indexOf(currentEvent.getLocation()));
                }catch (IndexOutOfBoundsException e){
                    Log.d(ModifyEventFragment.class.getSimpleName(), e.getLocalizedMessage());
                }
            }
            historicLocationEditText.setText(currentEvent.getLocation());

            yearEditText.setText(currentEvent.getSliceTime());

            publishButton.setText(R.string.event_modify_apply_button);
        }
    }

    public static ModifyEventFragment newInstance(String eventId) {

        Bundle args = new Bundle();

        ModifyEventFragment fragment = new ModifyEventFragment();
        args.putString(Constants.DYKH_FRAGMENT_SELECTION, eventId);
        fragment.setArguments(args);
        return fragment;
    }
}

package malakoff.dykh.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import malakoff.dykh.Activities.Base.BaseDawerActivity;
import malakoff.dykh.Activities.MySpaceActivity;
import malakoff.dykh.Activities.UtilitaryActivity;
import malakoff.dykh.AppApplication.AppApplication;
import malakoff.dykh.AppApplication.Constants;
import malakoff.dykh.CellHolder.EventCellHolder;
import malakoff.dykh.Event.Event;
import malakoff.dykh.Fragments.Base.RecyclerBaseFragment;
import malakoff.dykh.Network.GsonArrayRequest;
import malakoff.dykh.Network.MySingleton;
import malakoff.dykh.R;
import malakoff.dykh.Recycler.ReAdapterViewProvider;
import malakoff.dykh.Recycler.ReCellHolder;
import malakoff.dykh.Utils.UsefulGenericMethods;

/**
 * Created by user on 23/06/2016.
 */

public class EventsFeedBaseFragment extends RecyclerBaseFragment<Event>{

    private View searchEventLayout;
    private ImageView backSpaceView;
    private EditText searchEventEditText;

    @Override
    protected void assignViews(View view) {
        super.assignViews(view);
        searchEventLayout = view.findViewById(R.id.event_searching_layout);
        backSpaceView = (ImageView)view.findViewById(R.id.image_delete_text);
        searchEventEditText = (EditText)view.findViewById(R.id.edittext_searching_event);
    }

    @Override
    protected void populateViews(Bundle savedInstanceState) {
        super.populateViews(savedInstanceState);

        if(getActivity() instanceof MySpaceActivity){
            ((MySpaceActivity) getActivity()).setNavigationBarIcon();
            ((MySpaceActivity) getActivity()).enableDrawerOpening(true);

        }else{
            searchEventEditText.setFocusable(true);
            searchEventEditText.setFocusableInTouchMode(true);
            searchEventEditText.requestFocus();
            UsefulGenericMethods.showKeyboard(getContext(), searchEventEditText);

        }

        backSpaceView.setOnClickListener(this);
        searchEventEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                getFilter(editable.toString().toLowerCase(Locale.getDefault()));
            }
        });

        searchEventEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(TextUtils.isEmpty(v.getText())) return false;

                getFilter(v.getText().toString().toLowerCase(Locale.getDefault()));
                UsefulGenericMethods.hideKeyboard(getContext(), searchEventEditText);

                return true;
            }
        });

    }

    @Override
    protected void loadQuery() {
        if (mEventManager.getEvents() == null || mEventManager.getEvents().isEmpty()) {

            Map<String, String> param = new HashMap<>();
            param.put("_id", AppApplication.getUserInfo().getUserId());

            MySingleton
                    .getInstance(getContext())
                    .addToRequestQueue(
                    new GsonArrayRequest(
                            Request.Method.GET,
                            Constants.SERVER_URL_ROOT + Constants.SERVER_URL_EVENT_ROUT + "/getEventsByUserId/598afcdf81a5a420e464ee49",
                            JSONArray.class,
                            param,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    List<Event> events = new Gson().fromJson(String.valueOf(response),
                                            new TypeToken<List<Event>>() {
                                            }.getType());

                                    if(events != null) {

                                        mEventManager.loadEvents(events);

                                        mEventManager.readEvents(AppApplication.getUserInfo().getUserId());

                                        adapter.addDatas(mEventManager.getEvents());

                                    }else{
                                        //noinspection ConstantConditions
                                        Snackbar.make(getView(), "Something went wrong!", Toast.LENGTH_LONG).show();
                                    }

                                    refreshFinished();

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //noinspection ConstantConditions
                            Snackbar.make(getView(), "That didn't work!", Toast.LENGTH_LONG).show();

                            refreshFinished();
                        }
                    })
            );
        }else {

            mEventManager.readEvents(AppApplication.getUserInfo().getUserId());

            adapter.addDatas(mEventManager.getEvents());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadQuery();
    }

    @Override
    protected ReAdapterViewProvider<Event> getAdapterCellBuilder() {
        return new ReAdapterViewProvider<Event>() {
            @Override
            public int getLayoutId(int holderType) {
                return R.layout.cell_summary_event;
            }

            @Override
            public ReCellHolder<Event> getHolder(int holderType, View view) {
                return new EventCellHolder(view);
            }

            @Override
            public void onItemClicked(int position, Event data) {
                UtilitaryActivity.open(getActivity(), data.getLocation(), String.valueOf(position),
                        data.getTitle(), data.getTheme(), AppApplication.getUserInfo().getUserId()
                            .contentEquals(data.getUserId()));
            }

            @Override
            public boolean onItemLongClicked(int position, Event data) {
                return false;
            }
        };
    }

    public void getFilter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        List<Event> buffer = new ArrayList<>();
        if (charText.length() == 0) {
            buffer.addAll(mEventManager.getEvents());
        }
        else
        {
            for (Event event : mEventManager.getEvents())
            {
                if (event.getTitle().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    buffer.add(event);
                }
            }
        }
        adapter.setUpNewData(buffer);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_events_feed;
    }

    public static EventsFeedBaseFragment newInstance(long selection) {

        Bundle args = new Bundle();

        EventsFeedBaseFragment fragment = new EventsFeedBaseFragment();
        args.putLong(Constants.DYKH_FRAGMENT_SELECTION, selection);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()){
            case R.id.image_delete_text:

                if(!TextUtils.isEmpty(searchEventEditText.getText())) {
                    searchEventEditText.setText("");
                }
                break;
        }

    }

    @Override
    protected RecyclerView.LayoutManager buildLayoutManager() {
        return new StaggeredGridLayoutManager(2,1);
    }

    @Override
    protected void reinitToolbar() {
        super.reinitToolbar();
        if (getActivity() instanceof BaseDawerActivity) {
            ((BaseDawerActivity)getActivity()).setDefaultDrawable();
            ((BaseDawerActivity) getActivity()).changeToolbarConfig(R.drawable.ic_arrow_back_24dp,
                    R.string.app_name);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
    }

    @Override
    public void onStop() {
        UsefulGenericMethods.hideKeyboard(getContext(), getView()); //TODO glitch from here to HomeFragment where we're in the MainActivity
        super.onStop();
    }
}

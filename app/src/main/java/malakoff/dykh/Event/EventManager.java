package malakoff.dykh.Event;

import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import malakoff.dykh.AppApplication.Constants;
import malakoff.dykh.BackEnd.GsonEventFactory;
import malakoff.dykh.Factory.FactoryManagers;

public class EventManager {

    private static final int STARTER_INDEX = 12;

    /**
     *
     */
    private List<Event> events;
    private GsonEventFactory gsonEventFactory;
    private Map<String, Event> eventHashMapHandler;

    /**
     * Getter of events
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * Setter of events
     */
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    /**
     * @param location
     * @param theme
     * @param sliceTime
     * @param eventTitle
     */
    public void readSpecificEvent(String theme, String sliceTime, String location, String eventTitle) {
        // TODO Auto-generated method
    }

    public Event readSpecificEvent(String eventId) {

        if(TextUtils.isEmpty(eventId))return null;

        for (Event event : events) {
            if (event.getEventId().contentEquals(eventId)) {
                return event;
            }
        }

        return null;
    }

    /**
     * @param event
     * @param userId
     */
    public void submitModification(Event event, long userId) {
        // TODO Auto-generated method
    }

    /**
     * @param userId
     */
    public void readEvents(String userId) {
        if (gsonEventFactory == null) {
            gsonEventFactory = FactoryManagers.getGsonEventInstance();
        }

        events = gsonEventFactory.getEvents(userId);
    }

    /**
     * @param eventTitle
     * @param theme
     * @param eventSliceTime
     * @param userId
     * @param eventLocation
     * @param eventStory
     * @return
     */
    public boolean fillForm2CreateEvent(String eventTitle,
                                        String theme,
                                        String eventSliceTime,
                                        String userId,
                                        String todayLocation,
                                        String eventLocation,
                                        String eventStory) {

        Event newEvent = new Event();

        newEvent.setTitle(eventTitle);
        newEvent.setTheme(theme);
        newEvent.setSliceTime(eventSliceTime);
        newEvent.setUserId(userId);
        newEvent.setLocation(eventLocation);
        newEvent.setLocationModernCalling(todayLocation);
        newEvent.setStory(eventStory);

        if (gsonEventFactory == null) {
            gsonEventFactory = FactoryManagers.getGsonEventInstance();
        }

        return gsonEventFactory.createEvent(newEvent);

    }

    public boolean fillForm2CreateEvent(Event event) {
        if (gsonEventFactory == null) {
            gsonEventFactory = FactoryManagers.getGsonEventInstance();
        }

        return gsonEventFactory.createEvent(event);
    }

    public boolean loadEvents(List<Event> newEvents) {
        if (newEvents == null || newEvents.isEmpty()) return false;

        if (gsonEventFactory == null) {
            gsonEventFactory = FactoryManagers.getGsonEventInstance();
        }

        eventHashMapHandler = new HashMap<>();

        for (Event event : newEvents) {
            eventHashMapHandler.put(event.getSliceTime(), event);
        }

        return gsonEventFactory.addEvents(newEvents);
    }

    /**
     * @param userId
     * @param eventPosition
     */
    public void modifyEvent(long userId, int eventPosition) {
        // TODO Auto-generated method
    }

    /**
     * @param userdId
     * @param status
     * @return
     */
    public boolean createEvent(long userdId, boolean status) {
        // TODO Auto-generated method
        return false;
    }

    /**
     * @param userId
     * @param event
     */
    public void submitModification(long userId, Event event) {
        // TODO Auto-generated method
    }

    /**
     * @param era
     * @return
     */
    public Event pullEraMajorEvents(String era) {
        // TODO Auto-generated method
        return null;
    }

    /**
     * @param year
     * @return
     */
    public Event passYear(String year, Event previousSelectedEvent) {

        if (TextUtils.isEmpty(year) && TextUtils.isEmpty(year.split(" ")[0])) return null;

        String askedYear = year.split(" ")[0];


        if (previousSelectedEvent == null) {

            Event neighborEvent;

            if (Constants.BEGINNING_OF_MANTIME.contentEquals(year)) {
                neighborEvent = gsonEventFactory.getEvents().get(STARTER_INDEX);

                float gap = Float.parseFloat(neighborEvent.getSliceTime().split(" ")[0]) - Float.parseFloat(askedYear);

                return gap <= 50f ? neighborEvent : null;

            } else if (eventHashMapHandler.containsKey(year)) {

                Log.d(EventManager.class.getName(), "Jump failed: " + year + "==" + eventHashMapHandler.get(year).getSliceTime() + " ?");

                return eventHashMapHandler.get(year);
            }

            return null;

        } else {
            return eventHashMapHandler.containsKey(year) ? eventHashMapHandler.get(year) : previousSelectedEvent;
        }

    }

    public Event passYearAfterBrutalChange(String year) {

        if (TextUtils.isEmpty(year) && TextUtils.isEmpty(year.split(" ")[0])) return null;

        if (eventHashMapHandler.containsKey(year)) {
            Log.d(EventManager.class.getName(), "Jump succeeded: " + year + "==" + eventHashMapHandler.get(year).getSliceTime() + " ?");
            return eventHashMapHandler.get(year);
        }

        return null;
    }

    /**
     * @param location
     * @return
     */
    public Event convertPlaceIntoEvent(String location) {
        // TODO Auto-generated method
        return null;
    }

    /**
     * @param eventPosition
     * @param userId
     */
    public void modifyEvent(int eventPosition, long userId) {
        // TODO Auto-generated method
    }

    /**
     * @param title
     * @return
     */
    public Event pickEvent(String title) {
        // TODO Auto-generated method
        return null;
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public Event getProperEvents(Integer key, String value) {
        // TODO Auto-generated method
        return null;
    }
}

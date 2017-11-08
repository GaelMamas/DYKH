package malakoff.dykh.BackEnd;

import java.util.ArrayList;
import java.util.List;

import malakoff.dykh.Event.Event;
import malakoff.dykh.Helper.GenericUsefulMethods;

public class GsonEventFactory {


	/**
	 * 
	 */

	private List<Event> events;

	/**
	 * 
	 * @return 
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
	 * 
	 * @param result 
	 * @param event 
	 * @param userId 
	 * @return 
	 */
	public boolean submitModification(boolean result, Event event, long userId) { 
		// TODO Auto-generated method
		return false;
	 }

	/**
	 * 
	 * @param eventId 
	 * @param userId 
	 * @return 
	 */
	public boolean validateEvent(long eventId, long userId) { 
		// TODO Auto-generated method
		return false;
	 }

	/**
	 * 
	 * @param newEvent 
	 * @return 
	 */
	public boolean createEvent(Event newEvent) {
		if(newEvent == null)return false;

		//newEvent.setEventId(GenericUsefulMethods.generateLongId());
		newEvent.setIsValidate(false);

		if(events == null){
			events = new ArrayList<>();
		}

		return events.add(newEvent);
	 }

	public boolean addEvents(List<Event> newEvents){
		if(newEvents == null || newEvents.isEmpty()) return false;

		if(events == null){
			events = new ArrayList<>();
		}

		return events.addAll(newEvents);
	}

	/**
	 * 
	 * @param event 
	 * @param userId 
	 * @param result 
	 * @return 
	 */
	public boolean submitModification(Event event, long userId, boolean result) { 
		// TODO Auto-generated method
		return false;
	 }

	/**
	 * 
	 * @param result 
	 * @param userId 
	 * @param event 
	 * @return 
	 */
	public boolean submitModification(boolean result, long userId, Event event) { 
		// TODO Auto-generated method
		return false;
	 }

	public List<Event> getEvents(long userId) {
		List<Event> eventsFeeder = new ArrayList<>();

		if(events == null || events.isEmpty()){
			return new ArrayList<>();
		}

		if(GenericUsefulMethods.isHistorian(userId).isHistorian()){

			for(Event event: events){
				if(event.getIsValidate()){
					eventsFeeder.add(event);
				}
			}

			return eventsFeeder;
		}else{
			for(Event event: events){
				if(event.getIsValidate()){//How to have a set that keep this criteria
					eventsFeeder.add(event);
				}
				else if(event.getUserId() == userId){
					eventsFeeder.add(event);
				}
			}

			return eventsFeeder;
		}
	}
}

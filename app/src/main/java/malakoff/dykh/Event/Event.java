package malakoff.dykh.Event;

import com.google.android.gms.maps.model.LatLng;

import malakoff.dykh.User.User;

public class Event {

	/**
	 * 
	 */
	private String _id;
	/**
	 *
	 */
	private String sliceTime;
	/**
	 *
	 */
	private String location;
	/**
	 *
	 */
	private String userId;
	/**
	 *
	 */
	private String title;
	/**
	 *
	 */
	private String story;
	/**
	 *
	 */
	private String theme;
	/**
	 *
	 */
	private boolean isValidate;

	private String locationModernCalling;

	private double longitude;

	private double latitude;

	private User user;


	//TODO REMOVE THIS LATER WHEN WS ARE GONNA BE SET UP
	public Event(){}

	public Event(
			String eventId,
			String sliceTime,
			String location,
			String locationModernCalling,
			String userId,
			String title,
			String story,
			String theme
	){
		this._id = eventId;
		this.sliceTime = sliceTime;
		this.location = location;
		this.locationModernCalling = locationModernCalling;
		this.userId = userId;
		this.title = title;
		this.story = story;
		this.theme = theme;
	}
	//TODO END

	/**
	 * Getter of eventId
	 */
	public String getEventId() {
	 	 return _id;
	}
	/**
	 * Setter of eventId
	 */
	public void setEventId(String eventId) {
		 this._id = eventId;
	}
	/**
	 * Getter of sliceTime
	 */
	public String getSliceTime() {
	 	 return sliceTime; 
	}
	/**
	 * Setter of sliceTime
	 */
	public void setSliceTime(String sliceTime) { 
		 this.sliceTime = sliceTime; 
	}
	/**
	 * Getter of location
	 */
	public String getLocation() {
	 	 return location; 
	}
	/**
	 * Setter of location
	 */
	public void setLocation(String location) { 
		 this.location = location; 
	}
	/**
	 * Getter of _id
	 */
	public String getUserId() {
	 	 return userId; 
	}
	/**
	 * Setter of _id
	 */
	public void setUserId(String userId) {
		 this.userId = userId; 
	}
	/**
	 * Getter of title
	 */
	public String getTitle() {
	 	 return title; 
	}
	/**
	 * Setter of title
	 */
	public void setTitle(String title) { 
		 this.title = title; 
	}
	/**
	 * Getter of story
	 */
	public String getStory() {
	 	 return story; 
	}
	/**
	 * Setter of story
	 */
	public void setStory(String story) { 
		 this.story = story; 
	}
	/**
	 * Getter of theme
	 */
	public String getTheme() {
	 	 return theme; 
	}
	/**
	 * Setter of theme
	 */
	public void setTheme(String theme) { 
		 this.theme = theme; 
	}
	/**
	 * Getter of isValidate
	 */
	public boolean getIsValidate() {
	 	 return isValidate; 
	}
	/**
	 * Setter of isValidate
	 */
	public void setIsValidate(boolean isValidate) { 
		 this.isValidate = isValidate; 
	}


	public String getLocationModernCalling() {
		return locationModernCalling;
	}

	public void setLocationModernCalling(String locationModernCalling) {
		this.locationModernCalling = locationModernCalling;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public User getUser() {
		return user;
	}
}

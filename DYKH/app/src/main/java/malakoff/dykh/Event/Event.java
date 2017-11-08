package malakoff.dykh.Event;

public class Event {

	/**
	 * 
	 */
	private long eventId;
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
	private long userId;
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



	//TODO REMOVE THIS LATER
	public Event(){}

	public Event(
			long eventId,
			String sliceTime,
			String location,
			long userId,
			String title,
			String story,
			String theme
	){
		this.eventId = eventId;
		this.sliceTime = sliceTime;
		this.location = location;
		this.userId = userId;
		this.title = title;
		this.story = story;
		this.theme = theme;

	}
	//TODO END

	/**
	 * Getter of eventId
	 */
	public long getEventId() {
	 	 return eventId; 
	}
	/**
	 * Setter of eventId
	 */
	public void setEventId(long eventId) { 
		 this.eventId = eventId; 
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
	 * Getter of userId
	 */
	public long getUserId() {
	 	 return userId; 
	}
	/**
	 * Setter of userId
	 */
	public void setUserId(long userId) { 
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

}

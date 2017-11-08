package malakoff.dykh.User;

public class User {

	/**
	 * 
	 */
	protected long userId;


	protected boolean isHistorian;

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

	public boolean isHistorian() {
		return isHistorian;
	}

	public void setHistorian(boolean historian) {
		isHistorian = historian;
	}
}

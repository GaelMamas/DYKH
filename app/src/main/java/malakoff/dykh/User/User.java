package malakoff.dykh.User;

public class User {

	/**
	 * 
	 */
	protected String _id;


	protected boolean isHistorian;

	protected String name;

	protected String email;

	/**
	 * Getter of _id
	 */
	public String getUserId() {
	 	 return _id;
	}

	/**
	 * Setter of _id
	 */
	public void setUserId(String _id) {
		 this._id = _id;
	}

	public boolean isHistorian() {
		return isHistorian;
	}

	public void setHistorian(boolean historian) {
		isHistorian = historian;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}

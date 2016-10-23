package net.usrlib.cms.user;

public abstract class User {
	protected int uuid;
	protected String name;
	protected UserRole userRole;
	protected String address;
	protected String phone;

	public User() {
	}

	public User(int uuid, String name, String address, String phone, UserRole userRole) {
		setUuid(uuid);
		setName(name);
		setAddress(address);
		setPhone(phone);
		setUserRole(userRole);
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public String getAddress() {
		return address;
	}

	public String getPhone() {
		return phone;
	}
}

package net.usrlib.cms.user;

import java.util.List;

public abstract class User {
	protected long uuid;
	protected String firstName;
	protected String lastName;
	protected UserRole userRole;
	protected List<Address> addressList;
	protected List<Phone> phoneList;

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public void setAddressList(List<Address> addressList) {
		this.addressList = addressList;
	}

	public void setPhoneList(List<Phone> phoneList) {
		this.phoneList = phoneList;
	}

	public long getUuid() {
		return uuid;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public List<Address> getAddressList() {
		return addressList;
	}

	public List<Phone> getPhoneList() {
		return phoneList;
	}
}

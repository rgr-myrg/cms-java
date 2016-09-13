package net.usrlib.cms;

import java.util.List;

abstract class User {
	protected long uuid;
	protected String firstName;
	protected String lastName;
	protected UserRole userRole;
	protected List<Address> addressList;
	protected List<Phone> phoneList;
}

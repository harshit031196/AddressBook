package com.addressbooksystem;

import java.util.Map;
public class AddressBook {

	Map<String,contactInfo> Ab;
	
	public void setAddressBook(Map<String,contactInfo> Ab) {
		this.Ab=Ab;
	}
	public Map<String,contactInfo> getAddressBook(){
		return this.Ab;
	}
}


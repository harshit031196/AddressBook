package com.addressbooksystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AddressBookServiceTest {
//	@Test
//	public void givenAddressBookDB_WhenRetrieved_ShouldMatchTotalEntries() {
//		AddressBookService addressBookService = new AddressBookService();
//		int enteries = addressBookService.readAddressBook();
//		assertEquals(4, enteries);
//	}
	@Test
	public void givenNewPhoneNumberForAContact_WhenUpdated_ShouldBeUpdatedInTheDB() {
		AddressBookService addressBookService = new AddressBookService();
		addressBookService.readAddressBook();
		addressBookService.updateContactPhoneNumber(1, 7563245872L);
	}

}

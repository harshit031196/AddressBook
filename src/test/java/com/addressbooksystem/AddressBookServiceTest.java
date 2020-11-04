package com.addressbooksystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Map;

import org.junit.Test;

import com.addressbooksystem.AddressBookService.IOTYPE;

public class AddressBookServiceTest {
	@Test
	public void givenAddressBookDB_WhenRetrieved_ShouldMatchTotalEntries() {
		AddressBookService addressBookService = new AddressBookService();
		int enteries = addressBookService.readAddressBook();
		assertEquals(4, enteries);
	}
	@Test
	public void givenNewPhoneNumberForAContact_WhenUpdated_ShouldBeUpdatedInTheDB() {
		AddressBookService addressBookService = new AddressBookService();
		addressBookService.readAddressBook();
		addressBookService.updateContactPhoneNumber(1, 7563245872L);
	}
	@Test
	public void givenDateRange_WhenRetrievedFromDB_ShouldMatchTotalEntries() {
		AddressBookService addressBookService = new AddressBookService();
		addressBookService.readAddressBook();
		LocalDate startDate = LocalDate.of(2020, 01, 01);
		LocalDate endDate = LocalDate.now();
		LinkedList<Contact> contactInGivenDateRange = addressBookService.readContactForDateRange(IOTYPE.DB_IO, startDate, endDate); 
		assertEquals(2, contactInGivenDateRange.size());
	}
	
	@Test
	public void givenAddressBookDB_WhenRetrievedContactCountByCity_ShouldReturnCorrectResult() {
		AddressBookService addressBookService = new AddressBookService();
		Map<String, Integer> contactCountByCity = addressBookService.getContactCountByCityFromDB(); 
		assertEquals(1, (int) contactCountByCity.get("Mumbai"));
		assertEquals(1, (int) contactCountByCity.get("Gurgaon"));
		assertEquals(1, (int) contactCountByCity.get("Chennai"));
	}
	
	@Test
	public void givenAddressBookDB_WhenRetrievedContactCountByState_ShouldReturnCorrectResult() {
		AddressBookService addressBookService = new AddressBookService();
		Map<String, Integer> contactCountByState = addressBookService.getContactCountByStateFromDB(); 
		assertEquals(1, (int) contactCountByState.get("Maharashtra"));
		assertEquals(1, (int) contactCountByState.get("Haryana"));
		assertEquals(1, (int) contactCountByState.get("Tamil Nadu"));
	}
}

package com.addressbooksystem;

import java.io.File; 
import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.addressbooksystem.AddressBook.TYPE;
import com.addressbooksystem.*;


@FunctionalInterface
interface AddKVpair {
	void addKVPair(String key, LinkedList<Contact> value);
}

public class AddressBookService {

	public enum IOTYPE {
		CONSOLE, TXT_FILE, CSV_FILE, JSON_FILE, DB_IO;
	}

	private static Map<String, List<Contact>> addressBooks = new HashMap<String, List<Contact>>();
	private static Map<TYPE, AddressBook> addressBooksDB = null;
	private static AddressBookDBService addressBookDBService = new AddressBookDBService();
	private static final Scanner SC = new Scanner(System.in);

	public AddressBookService() {
		
	}

	/**
	 * To add contact to the address book
	 */
	public void addAddressBook(String addressBookName) {
		List<Contact> addressBook = new LinkedList<Contact>();
		addressBooks.put(addressBookName, addressBook);
		System.out.println("Address Book added Successfully!");
	}

	/**
	 * To check if Address book by particular name exists or not
	 */
	public boolean isAddressBookByThatNameExists(String addressBookName) {
		int count = (int) addressBooks.entrySet().stream().filter(a -> a.getKey().equalsIgnoreCase(addressBookName))
				.count();
		return (count == 1) ? true : false;
	}

	/**
	 * To add contact in particular address book
	 */
	public void addContactToParticularAddressBook(String addressBookName, Contact contact) {
		boolean isAddressBookByThatNameExists = isAddressBookByThatNameExists(addressBookName);
		Predicate<Contact> predicate = contactObj -> contactObj.equals(contact);
		if (isAddressBookByThatNameExists) {
			boolean isSame = addressBooks.get(addressBookName).stream().anyMatch(predicate);
			if (!isSame) {
				addressBooks.get(addressBookName).add(contact);
				System.out.println("Contact added successfully!");
			} else {
				System.out.println("Contact by that name already exists in the particular address book! Try again!");
			}
		} else {
			System.out.println(
					"Address Book by that Name does not exist! Do you want to create new addressbook by this name? (Y/N");
			String option = SC.next();
			if (option.equalsIgnoreCase("y")) {
				addAddressBook(addressBookName);
				addressBooks.get(addressBookName).add(contact);
			}
		}
	}

	/**
	 * To create Contact
	 */
	public Contact createContact() {
		System.out.println("Enter the details to add the contact \nfirst name:");
		String firstName = SC.next();
		System.out.println("last name:");
		String lastName = SC.next();
		System.out.println("Address: ");
		SC.nextLine();
		String address = SC.nextLine();
		System.out.println("City: ");
		String city = SC.nextLine();
		System.out.println("State: ");
		String state = SC.nextLine();
		System.out.println("Zip: ");
		long zip = SC.nextLong();
		System.out.println("Phone Number: ");
		long phoneNumber = SC.nextLong();
		System.out.println("Email Address: ");
		String email = SC.next();
		Contact contactObject = new Contact(firstName, lastName, address, city, state, email, zip, phoneNumber);
		return contactObject;

	}

	/**
	 * To get the contact from address book using first and last name
	 */
	public Contact getContact(String firstName, String lastName) {
		Contact contact = null;
		for (Map.Entry<String, List<Contact>> entry : addressBooks.entrySet()) {
			for (Contact contactObject : entry.getValue()) {
				if (contactObject.getFirstName().equalsIgnoreCase(firstName)
						&& contactObject.getLastName().equalsIgnoreCase(lastName)) {
					contact = contactObject;
					break;
				}

			}
		}
		return contact;
	}

	/**
	 * To edit contact in the address book
	 */
	public void editContact(String firstName, String lastName) {
		Contact contact = getContact(firstName, lastName);
		if (contact != null) {
			System.out.println("Enter the details again to update the contact \nfirst name:");
			contact.setFirstName(SC.next());
			System.out.println("last name:");
			contact.setLastName(SC.next());
			System.out.println("Address: ");
			SC.nextLine();
			contact.setAddress(SC.nextLine());
			System.out.println("City: ");
			contact.setCity(SC.nextLine());
			System.out.println("State: ");
			contact.setState(SC.nextLine());
			System.out.println("Zip: ");
			contact.setZip(SC.nextLong());
			System.out.println("Phone Number: ");
			contact.setPhoneNumber(SC.nextLong());
			System.out.println("Email Address: ");
			contact.setEmail(SC.nextLine());
			System.out.println("Contact Updated Successfully!");
		}

		else {
			System.out.println("NO such contact in the address book!");
		}
	}

	/**
	 * To remove contact from the address book
	 */
	public void removeContact(String firstName, String lastName) {
		boolean flag = false;
		for (Map.Entry<String, List<Contact>> entry : addressBooks.entrySet()) {
			for (Contact contactObject : entry.getValue()) {
				if (contactObject.getFirstName().equalsIgnoreCase(firstName)
						&& contactObject.getLastName().equalsIgnoreCase(lastName)) {
					entry.getValue().remove(contactObject);
					flag = true;
					System.out.println("Contact removed from the address book successfully");
					break;
				}
			}
		}
		if (!flag) {
			System.out.println("Sorry, no such contact exist with that name!");
		}
	}

	public void viewAddressBook(String addressBookName) {
		if (isAddressBookByThatNameExists(addressBookName)) {
			if (addressBooks.get(addressBookName).size() == 0) {
				System.out.println("Sorry! No contact present in this address Book");
			} else {
				addressBooks.get(addressBookName).forEach(System.out::println);
			}
		} else {
			System.out.println("Sorry! No address book by this name present in the system");
		}
	}

	/**
	 * To get the list of contacts in particular state
	 */
	public List<Contact> listOfContactsInParticularState(String state) {
		LinkedList<Contact> contactInParticularState = (LinkedList<Contact>) addressBooks.entrySet().stream()
				.flatMap(entry -> entry.getValue().stream()).filter(contact -> contact.getState().equals(state))
				.collect(Collectors.toCollection(LinkedList::new));
		if (contactInParticularState.size() == 0) {
			System.out.println("No contact exist in particular state");
		}
		return contactInParticularState;
	}

	/**
	 * To get the list of contacts in particular city
	 */
	public List<Contact> listOfContactsInParticularCity(String city) {
		LinkedList<Contact> contactInParticularCity = (LinkedList<Contact>) addressBooks.entrySet().stream()
				.flatMap(entry -> entry.getValue().stream()).filter(contact -> contact.getCity().equals(city))
				.collect(Collectors.toCollection(LinkedList::new));
		if (contactInParticularCity.size() == 0) {
			System.out.println("No contact exist in particular city");
		}
		return contactInParticularCity;
	}

	/**
	 * To get contacts by city
	 */
	public Map<String, LinkedList<Contact>> addressBookByCity() {
		Map<String, LinkedList<Contact>> contactByCities = new HashMap<String, LinkedList<Contact>>();
		Function<String, LinkedList<Contact>> cityToContactsInThatCity = str -> (LinkedList<Contact>) listOfContactsInParticularCity(str);
		AddKVpair addingKVPair = (x, y) -> contactByCities.put(x, y);
		listOfAllCities().forEach(str -> addingKVPair.addKVPair(str, cityToContactsInThatCity.apply(str)));

		return contactByCities;
	}

	/**
	 * To get contacts by state
	 */
	public Map<String, LinkedList<Contact>> addressBookByState() {
		Map<String, LinkedList<Contact>> contactByStates = new HashMap<String, LinkedList<Contact>>();
		Function<String, LinkedList<Contact>> stateToContactsInThatState = str -> (LinkedList<Contact>) listOfContactsInParticularState(str);
		AddKVpair addingKVPair = (x, y) -> contactByStates.put(x, y);
		listOfAllStates().forEach(str -> addingKVPair.addKVPair(str, stateToContactsInThatState.apply(str)));
		return contactByStates;
	}

	/**
	 * To get the list of city based on all contacts in tha address books
	 */
	public List<String> listOfAllCities() {
		List<String> cities = addressBooks.entrySet().stream().flatMap(entry -> entry.getValue().stream())
				.map(contact -> contact.getCity()).collect(Collectors.toList());
		return cities;
	}

	/**
	 * To get the list of state based on all contacts in tha address books
	 */
	public List<String> listOfAllStates() {
		List<String> states = addressBooks.entrySet().stream().flatMap(entry -> entry.getValue().stream())
				.map(contact -> contact.getState()).collect(Collectors.toList());
		return states;
	}

	/**
	 * To get contact count by city
	 */
	public Map<String, Integer> contactCountByCity() {
		Map<String, Integer> countByCities = new HashMap<String, Integer>();
		Function<String, Integer> countByCity = str -> (Integer) listOfContactsInParticularCity(str).size();
		listOfAllCities().stream().forEach(str -> countByCities.put(str, countByCity.apply(str)));
		return countByCities;
	}

	/**
	 * To get contact count by city
	 */
	public Map<String, Integer> contactCountByState() {
		Map<String, Integer> countByStates = new HashMap<String, Integer>();
		Function<String, Integer> countByState = str -> listOfContactsInParticularState(str).size();
		listOfAllStates().stream().forEach(str -> countByStates.put(str, countByState.apply(str)));
		return countByStates;
	}

	/**
	 * To sort the entries in the address book by person's name
	 */
	public void sortAddressBookByPersonName(String addressBookName) {
		sortAddressBook(addressBookName, Comparator.comparing(Contact::getFirstName));
	}

	/**
	 * To sort the entries in the address book by city
	 */
	public void sortAddressBookByCity(String addressBookName) {
		sortAddressBook(addressBookName, Comparator.comparing(Contact::getCity));
	}

	/**
	 * To sort the entries in the address book by state
	 */
	public void sortAddressBookByState(String addressBookName) {
		sortAddressBook(addressBookName, Comparator.comparing(Contact::getState));
	}
	
	/**
	 * Sorts the address book based on comparator
	 */
	private void sortAddressBook(String addressBookName, Comparator<Contact> comparator) {
		boolean isAddressBookByThatNameExists = isAddressBookByThatNameExists(addressBookName);
		if (isAddressBookByThatNameExists) {
			if (addressBooks.get(addressBookName).size() == 0) {
				System.out.println("Sorry! There is no contact in this address book to sort!");
			} else {
				LinkedList<Contact> sortedAddressBook = addressBooks.get(addressBookName)
																	.stream()
																	.sorted(comparator)
																	.collect(Collectors.toCollection(LinkedList::new));

				addressBooks.replace(addressBookName, sortedAddressBook);
				System.out.println(addressBooks.get(addressBookName));
			}
		} else {
			System.out.println("There is no address book by this name in the address books");
		}
	}

	/**
	 * To sort the entries in the address book by zip
	 */
	public void sortAddressBookByZip(String addressBook) {
		boolean isAddressBookByThatNameExists = isAddressBookByThatNameExists(addressBook);
		if (isAddressBookByThatNameExists) {
			if (addressBooks.get(addressBook).size() == 0) {
				System.out.println("Sorry! There is no contact in this address book to sort!");
			} else {
				LinkedList<Contact> sortedAddressBook = addressBooks.get(addressBook).stream()
						.sorted(Comparator.comparing(Contact::getZip))
						.collect(Collectors.toCollection(LinkedList::new));

				addressBooks.replace(addressBook, sortedAddressBook);
				System.out.println(addressBooks.get(addressBook));
			}
		} else {
			System.out.println("There is no address book by this name in the address books");
		}
	}

	/**
	 * Writes the address book in a file
	 */
	public void writeAddressBook(String addressBookName, IOTYPE ioType) {
		if (isAddressBookByThatNameExists(addressBookName)) {
			if (ioType.equals(IOTYPE.TXT_FILE)) {
				new AddressBookFileIO().writeTextFile(addressBookName,
						(LinkedList<Contact>) addressBooks.get(addressBookName));
			} else if (ioType.equals(IOTYPE.CSV_FILE)) {
				try {
					new AddressBookFileIO().writeCSVFile(addressBookName,
							(LinkedList<Contact>) addressBooks.get(addressBookName));
				} catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
					e.printStackTrace();
				}
			} else if (ioType.equals(IOTYPE.JSON_FILE)) {
				try {
					new AddressBookFileIO().writeJSONFile(addressBookName,
							(LinkedList<Contact>) addressBooks.get(addressBookName));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				addressBooks.get(addressBookName).forEach(System.out::println);
			}
		} else {
			System.out.println("Please enter the correct address book name!");
		}
	}

	/**
	 * Reads the file and adds contacts to the particular address book
	 */
	public void readContacts(String fileName, String addressBookName, IOTYPE ioType) {
		File file = new File(fileName);
		LinkedList<Contact> contactList = new LinkedList<Contact>();
		if (ioType.equals(IOTYPE.TXT_FILE)) {
			contactList = new AddressBookFileIO().readTextFile(file);
		} else if (ioType.equals(IOTYPE.CSV_FILE)) {
			contactList = new AddressBookFileIO().readCSVFile(file);
		} else if (ioType.equals(IOTYPE.JSON_FILE)) {
			contactList = new AddressBookFileIO().readJSONFile(file);
		} else {
			System.out.println("Enter the no of contacts you want to add: ");
			int noOfContacts = SC.nextInt();
			for (int i = 0; i < noOfContacts; i++) {
				contactList.add(createContact());
			}
		}

		if (!isAddressBookByThatNameExists(addressBookName)) {
			addAddressBook(addressBookName);
			contactList.forEach(contact -> addContactToParticularAddressBook(addressBookName, contact));
		} else {
			contactList.forEach(contact -> addContactToParticularAddressBook(addressBookName, contact));
		}
	}

	/**
	 * Read contacts from DB, added between given date range
	 */
	public LinkedList<Contact> readContactForDateRange(IOTYPE type, LocalDate startDate, LocalDate endDate) {
		if (type.equals(IOTYPE.DB_IO)) {
			try {
				return addressBookDBService.getContactForDateRange(startDate, endDate);
			} catch (DatabaseException e) {
				System.out.println(e.getMessage());
			}
		}
		return null;
	}

	/**
	 * Reads Address Books from DB
	 */
	public int readAddressBook() {
		try {
			addressBooksDB = addressBookDBService.readAddressBook();
			return getContactCount(addressBooksDB);
		} catch (DatabaseException e) {
			System.out.println(e.getMessage());
		}
		return 0;
	}

	/**
	 * Update the contact number in data base for a given contact
	 */
	public void updateContactPhoneNumber(int contactId, long phoneNumber) {
		try {
			int rowAffected = addressBookDBService.updateContactPhoneNumber(contactId, phoneNumber);
			if(rowAffected == 1) {
				addressBooksDB.entrySet().forEach(entry -> entry.getValue().getContacts().stream().forEach(contact->  
												{ if (contact.getId() == contactId) contact.setPhoneNumber(phoneNumber);}));
			}
		} catch (DatabaseException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Checks whether a contact is in sync with DB
	 */
	public boolean isContactInSyncWithDB(int contactId) {
		try {
			Contact updatedContact = addressBookDBService.getContact(contactId);
			for (Map.Entry<TYPE, AddressBook> entry : addressBooksDB.entrySet()) {
				boolean flag = entry.getValue().getContacts().stream().filter(contact -> contact.getId() == contactId).allMatch(contact -> contact.equals(updatedContact));
				if(flag == false) {
					return false;
				}
			}
			return true;
		} catch (DatabaseException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/**
	 * returns the contact count in all address books
	 */
	private int getContactCount(Map<TYPE, AddressBook> addressBooks) {
		int count = 0;
		for(Map.Entry<TYPE, AddressBook> entry : addressBooks.entrySet()) {
			count += entry.getValue().getContacts().size();
		}
		return count;
	}
	/**
	 * @return contact count by city
	 */
	public Map<String, Integer> getContactCountByCityFromDB() {
		Map<String, Integer> contactCountByCity = null;
		try {
			contactCountByCity = addressBookDBService.readContactCountByCity();
		} catch (DatabaseException e) {
			System.out.println(e.getMessage());
		}
		return contactCountByCity;
	}
	/**
	 * @return contact count by state
	 */
	public Map<String, Integer> getContactCountByStateFromDB() {
		Map<String, Integer> contactCountByState = null;
		try {
			contactCountByState = addressBookDBService.readContactCountByState();
		} catch (DatabaseException e) {
			System.out.println(e.getMessage());
		}
		return contactCountByState;
	}
	
	public void addContactToDB(String firstName, String lastName, String address, String city, String state,
			String email, long zip, long phoneNumber, LocalDate date, TYPE... types) {
		try {
			Contact contact = addressBookDBService.addContactToDB(firstName, lastName, address, city,
																  state, email, zip, phoneNumber, date, types);
			for(TYPE type : types) {
				addressBooksDB.get(type).getContacts().add(contact);
			}
		} catch (DatabaseException e) {
			System.out.println(e.getMessage());
		}
		
	}
}

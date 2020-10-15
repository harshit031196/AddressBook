package com.addressbooksystem;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AddressBookMain {
	static HashMap<String, AddressBook> mp1 = new HashMap<>(); 
	static HashMap<String, contactInfo> mp = new HashMap<>();
	//Method to add new contact
	public static contactInfo createAddressBook() {
		Scanner sc = new Scanner(System.in);
		contactInfo contact = new contactInfo();
		
		System.out.println("Enter the first name");
		contact.setFirstName(sc.next());
		System.out.println("Enter the last name");
		contact.setLastName(sc.next());
		System.out.println("Enter the address");
		contact.setAddress(sc.next());
		System.out.println("Enter the city");
		contact.setCity(sc.next());
		System.out.println("Enter the state");
		contact.setState(sc.next());
		System.out.println("Enter the zip code");
		contact.setZip(sc.next());
		System.out.println("Enter the phone number");
		contact.setPhoneNumber(sc.next());
		System.out.println("Enter the email address");
		contact.setEmail(sc.next());
		return contact;
		
	}
	//Method to edit the already existing contact
	public static void editInfo() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter the first name of the contact you want to edit the details of");
		String find = sc.next();
		if(mp.containsKey(find)) {
			contactInfo updated = createAddressBook();
			mp.remove(find);
			mp.put(updated.getFirstName(), updated);
			System.out.println("The contact is updated");
		}else {
			System.out.println("The contact does not exist");
		}
	}
	//Method to delete an existing contact
	public static void delete() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter the first name of the person that you want to delete from the Phone book");
		String delName = sc.next();
		if(mp.containsKey(delName)) {
			mp.remove(delName);
			System.out.println("The contact is deleted");
		}else {
			System.out.println("The contact does not exist in the phonebook");
		}
	}
	//Method to add multiple people together in the same address book 
	public static void multiplePeople() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter the total number of contacts that you want to add");
		int count = sc.nextInt();
		for(int i=0;i<count;i++) {
			contactInfo a = createAddressBook();
			mp.put(a.getFirstName(), a);
		}
		System.out.println("All the contacts are added");
	}
	//Method to add a new address book
	public static void newAddressBook() {
		AddressBook ab = new AddressBook();
		System.out.println("Please enter a unique name for the address book");
		Scanner sc = new Scanner(System.in);
		String name = sc.next();
		boolean flag=false;
		while(!flag) {
			if(mp1.containsKey(name)) {
				System.out.println("The following name for an address book already exists. Please enter a unique name");
			}else {
				flag=true;
				ab.setAddressBook(new HashMap<String,contactInfo>());
				mp1.put(name, ab);
				System.out.println("The address book has been added to the system");
			}
		}
	}
	//Method to search Person by city or state
	public static void searchPersonCityState(String city, String state) {
		List<AddressBook> listAB = mp1.values().stream().collect(Collectors.toList());
		for(int i=0;i<listAB.size();i++) {
			List<contactInfo> names = listAB.get(i).getAddressBook().values().stream().filter(e->state.equals(e.getState())).collect(Collectors.toList());
			for(int j=0;j<names.size();i++) {
				System.out.println(names.get(j).getFirstName());
			}
		}
	}
	//Method to sort by name
	public static void sortByName() {
		List<AddressBook> listAB = mp1.values().stream().collect(Collectors.toList());
		for(int i=0;i<listAB.size();i++) {
			Map result = listAB.get(i).getAddressBook().entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
					(oldValue, newValue) -> oldValue, LinkedHashMap::new));
		}
	}
	//Method to sort by city state or zip code
	public static void sortByCityStateZip() {
		
	}
	//Main method
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AddressBook ab1 = new AddressBook();
		ab1.setAddressBook(new HashMap<String,contactInfo>());
		System.out.println("Welcome to Address Book");
		Scanner sc = new Scanner(System.in);
		System.out.println("Please choose from the following options and enter the number corresponding to that choice");
		System.out.println("1. Add a contact into the Address Book");
		System.out.println("2. Edit a contact already present in the Address Book");
		System.out.println("3. Delete a contact");
		System.out.println("4. Bulk add contacts in the Address Book");
		System.out.println("5. Add multiple address book");
		System.out.println("6. Search people in a city or a state");
		System.out.println("7. Sort by name");
		System.out.println("8. Sort by city, state, zip");
		int choice = sc.nextInt();
		switch(choice) {
		case 1: 
			contactInfo c = createAddressBook();
			if(ab1.getAddressBook().keySet().stream().anyMatch(e->e.equals(c.getFirstName()))) {
				System.out.println("The person is already present in the address book");
			}else {
				ab1.getAddressBook().put(c.getFirstName(),c);
				System.out.println("The contact is added");
			}
			
			break;
		case 2: 
			editInfo();
			break;
		case 3: 
			delete();
			break;
		case 4: 
			multiplePeople();
			break;
		case 5:
			System.out.println("Please enter the number of address books that you want to enter");
			int n = sc.nextInt();
			for(int i=0;i<n;i++) {
				newAddressBook();
			}
			System.out.println("All address books have been added to the system ");
		case 6:
			System.out.println("Please enter the name of the city and the state");
			String city = sc.next();
			String state = sc.next();
			searchPersonCityState(city,state);
		case 7:
			sortByName();
		case 8:
			sortByCityStateZip();
		default: 
			break;
		}
	}
}

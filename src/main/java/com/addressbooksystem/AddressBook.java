package com.addressbooksystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
public class AddressBook {
	public enum IOAddressBookService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	}
	Map<String,contactInfo> Ab = new HashMap<String,contactInfo>();
	ArrayList<contactInfo> list = new ArrayList<>();
	private String name;
	public AddressBook() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter total contacts you want to add in address book");
		int n = sc.nextInt();
		while (n-->0) {
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
			list.add(contact);
			this.Ab.put(contact.getFirstName(), contact);
		}
	}
	public void setAddressBook(Map<String,contactInfo> Ab) {
		this.Ab=Ab;
	}
	public Map<String,contactInfo> getAddressBook(){
		return this.Ab;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void writeIntoCSVFile(IOAddressBookService fileIo) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		if(fileIo.equals(IOAddressBookService.FILE_IO)) {
			new AddressBookCSVService().writeToCSV(list);
		}
	}
	
	public void readFromCSVFile(IOAddressBookService ioService) throws IOException {
		if(ioService.equals(IOAddressBookService.FILE_IO)) {
			ArrayList<contactInfo> contactList = (ArrayList<contactInfo>) new AddressBookCSVService().readToCSV();
			System.out.println(contactList);
		}
	}
	public void writeIntoJSONFile(IOAddressBookService fileIo) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		if(fileIo.equals(IOAddressBookService.FILE_IO)) {
			new AddressBookJSONService().writeToJson();
		}
	}
	
}


package com.addressbooksystem;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import com.addressbooksystem.AddressBook.IOAddressBookService;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class AddressBookMain {
	static HashMap<String, AddressBook> mp1 = new HashMap<>(); 
	
	//Main method
	public static void main(String[] args) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter the number of address book you want to create");
		int n = sc.nextInt();
		for(int i=0;i<n;i++) {
			System.out.println("Please enter the name of the address book: "+i+1);
			String name=sc.next();
			AddressBook ab = new AddressBook();
			ab.setName(name);
			mp1.put(name, ab);
		}
		for(Map.Entry m: mp1.entrySet()) {
			((AddressBook) m.getValue()).writeIntoCSVFile(IOAddressBookService.FILE_IO);
		}
		
		for(Map.Entry m: mp1.entrySet()) {
			((AddressBook) m.getValue()).readFromCSVFile(IOAddressBookService.FILE_IO);
		}
		for(Map.Entry m : mp1.entrySet()) {
			((AddressBook) m.getValue()).writeIntoJSONFile(IOAddressBookService.FILE_IO);
		}
	}

}

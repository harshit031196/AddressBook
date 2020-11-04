package com.addressbooksystem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.addressbooksystem.AddressBook.TYPE;
import com.addressbooksystem.DatabaseException.ExceptionType;


public class AddressBookDBService {

	/**
	 * Reads address Book from DB
	 */
	public Map<AddressBook.TYPE, AddressBook> readAddressBook() throws DatabaseException {
		String query = "SELECT * FROM address_book";
		LinkedList<AddressBook> addressBooks = getAddressBooks(query);
		addressBooks.forEach(addressBook -> {
			try {
				addressBook.getContacts().addAll(getContactsFromDB(addressBook.getId()));
			} catch (DatabaseException e) {
				System.out.println(e.getMessage());
			}
		});
		Map<TYPE, AddressBook> addressBookMap = new HashMap<AddressBook.TYPE, AddressBook>();
		addressBooks.forEach(addressBook -> addressBookMap.put(addressBook.type, addressBook));
		return addressBookMap;
	}
	

	/**
	 * returns the contact with a given ID
	 */
	public Contact getContact(int contactId) throws DatabaseException {
		String query = "SELECT * FROM contact, contact_address WHERE contact.contact_id = ? and contact_address.contact_id = ?;";
		try(Connection connection = getConnection()){
			PreparedStatement statement;
			statement = connection.prepareStatement(query);
			statement.setInt(1, contactId);
			statement.setInt(2, contactId);
			ResultSet result = statement.executeQuery();
			return getContacts(result).get(0);
		} catch (SQLException e) {
			throw new DatabaseException("Error while executing the query", ExceptionType.UNABLE_TO_EXECUTE_QUERY);
		}
	}

	
	/**
	 * To update the contact's phone number
	 */
	public int updateContactPhoneNumber(int contactId, long phoneNumber) throws DatabaseException {
		String query = "UPDATE contact SET phone_number = ? WHERE contact_id = ?";
		try(Connection connection = getConnection()){
			PreparedStatement statement;
			statement = connection.prepareStatement(query);
			statement.setLong(1, phoneNumber);
			statement.setInt(2, contactId);
			return statement.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException("Error while executing the query", ExceptionType.UNABLE_TO_EXECUTE_QUERY);
		}
	}
	
	/**
	 * Retrieves all the contacts from a result set
	 */
	private LinkedList<Contact> getContacts(ResultSet result) throws DatabaseException {
		List<Contact> contacts = new LinkedList<Contact>();
		try {	
			while(result.next()) {
				int id = result.getInt("contact_id");
				String firstName = result.getString("first_name");
				String lastName = result.getString("last_name");
				String address = result.getString("address");
				String city = result.getString("city");
				String state = result.getString("state");
				long zip = result.getLong("zip");
				long phoneNumber = result.getLong("phone_number");
				String email = result.getString("email");
				contacts.add(new Contact(id, firstName, lastName, address, city, state, email, zip, phoneNumber));
			}
			return (LinkedList<Contact>) contacts;
		} catch (SQLException e) {
			throw new DatabaseException("Error while executing the query", ExceptionType.UNABLE_TO_EXECUTE_QUERY);
		}
	}

	/**
	 * Returns list of contact from particular address book with given book id
	 */
	private LinkedList<Contact> getContactsFromDB(int book_id) throws DatabaseException {
		String query = "select * from contact where contact.contact_id in (select contact_id from address_book_contact where book_id = ?);";
		LinkedList<Contact> list = new LinkedList<Contact>();
		try(Connection connection = getConnection()){
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, book_id);
			ResultSet result = statement.executeQuery();
			list = getContacts(result);
		} catch (SQLException e) {
			throw new DatabaseException("Error while executing the query", ExceptionType.UNABLE_TO_EXECUTE_QUERY);
		}
		return list;
	}

	/**
	 * Returns all the address books from the DB
	 */
	private LinkedList<AddressBook> getAddressBooks(String query) throws DatabaseException {
		LinkedList<AddressBook> list = new LinkedList<AddressBook>();
		try (Connection connection = getConnection();) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while(resultSet.next()) {
				int id = resultSet.getInt("book_id");
				String name = resultSet.getString("name");
				String type = resultSet.getString("type");
				if(type.equalsIgnoreCase("family")) {
					list.add(new AddressBook(id, name, TYPE.FAMILY));
				} 
				if(type.equalsIgnoreCase("friends")) {
					list.add(new AddressBook(id, name, TYPE.FRIEND));
				}
				if(type.equalsIgnoreCase("profession")) {
					list.add(new AddressBook(id, name, TYPE.PROFESSION));
				}
			}
			return list;
		} catch (SQLException e) {
			throw new DatabaseException("Error while executing the query", ExceptionType.UNABLE_TO_EXECUTE_QUERY);
		}
	}
	
	/**
	 * To get connection object
	 */
	private Connection getConnection() throws DatabaseException {
		String jdbcURL = "jdbc:mysql://localhost:3306/address_book_service";
		String user = "root";
		String password = "harshit";
		Connection connection;
		try {
			connection = DriverManager.getConnection(jdbcURL, user, password);
			System.out.println("Connection successfully established!" + connection);
		} catch (SQLException e) {
			throw new DatabaseException("Unable to connect to the database", ExceptionType.UNABLE_TO_CONNECT);
		}
		return connection;
	}
}

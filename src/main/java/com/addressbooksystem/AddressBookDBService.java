package com.addressbooksystem;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
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
	 * Returns List of contacts that were added between the given date range
	 */
	public LinkedList<Contact> getContactForDateRange(LocalDate startDate, LocalDate endDate) throws DatabaseException {
		String query = String.format("SELECT * FROM contact WHERE date_added BETWEEN '%s' AND '%s'", Date.valueOf(startDate), Date.valueOf(endDate));
		return getContacts(query);
	}
	
	/**
	 * To update the contact's phone number
	 */
	public int updateContactPhoneNumber(int contactId, long phoneNumber) throws DatabaseException {
		String query = "UPDATE contact SET phone_number1 = ? WHERE contact_id = ?";
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
				LocalDate date = result.getDate("date_added").toLocalDate();
				contacts.add(new Contact(id, firstName, lastName, address, city, state, email, zip, phoneNumber, date));
			}
			return (LinkedList<Contact>) contacts;
		} catch (SQLException e) {
			throw new DatabaseException("Error while executing the query", ExceptionType.UNABLE_TO_EXECUTE_QUERY);
		}
	}

	/**
	 * Return Contacts retrieved by given query
	 */
	private LinkedList<Contact> getContacts(String query) throws DatabaseException {
		try(Connection connection = getConnection()){
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			return getContacts(result);
		} catch (SQLException e) {
			throw new DatabaseException("Error while executing the query", ExceptionType.UNABLE_TO_EXECUTE_QUERY);
		}
	}

	/**
	 * Returns list of contact from particular address book with given book id
	 */
	private LinkedList<Contact> getContactsFromDB(int book_id) throws DatabaseException {
		String query = String.format("select * from contact where contact.contact_id in (select contact_id from address_book_contact where book_id = '%s')", book_id);
		return this.getContacts(query);
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
	/**
	 * Returns contact count by city
	 */
	public Map<String, Integer> readContactCountByCity() throws DatabaseException {
		String query = "SELECT COUNT(contact_id) as count, city FROM contact GROUP BY city";
		return getContactCountByCityOrState(query, "city");
	}
	/**
	 * Returns contact count by city or state given the query and field name
	 */
	private Map<String, Integer> getContactCountByCityOrState(String query, String fieldName) throws DatabaseException{
		Map<String, Integer> contactCountByCityOrState = new HashMap<String, Integer>();
		try(Connection connection = getConnection()){
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			while(result.next()) {
				int count = result.getInt("count");
				String cityOrState = result.getString(fieldName);
				contactCountByCityOrState.put(cityOrState, count);
			}
			return contactCountByCityOrState;
		} catch (SQLException e) {
			throw new DatabaseException("Error while executing the query", ExceptionType.UNABLE_TO_EXECUTE_QUERY);
		}
	}
	public Map<String, Integer> readContactCountByState() throws DatabaseException {
		String query = "SELECT COUNT(contact_id) as count, state FROM contact GROUP BY state";
		return getContactCountByCityOrState(query, "state");
	}
	
	public Contact addContactToDB(String firstName, String lastName, String address, String city, String state,
			String email, long zip, long phoneNumber, LocalDate date, TYPE[] types) throws DatabaseException {
		Connection connection = null;
		int contactId = -1;
		Contact contact = null;
		
		try {
			connection = getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			throw new DatabaseException("Error while setting Auto Commit", ExceptionType.AUTO_COMMIT_ERROR);
		}
		
		contactId = addContact(connection, firstName, lastName, address, city, state, email, zip, phoneNumber, date);
		boolean isSuccessfull = addToContactAddressBookTable(connection, contactId, types);
		if(isSuccessfull) {
			contact = new Contact(contactId, firstName, lastName, address, city, state, email, zip, phoneNumber, date);
		}
		try {
			connection.commit();
		} catch (SQLException e) {
			throw new DatabaseException("Cannot Commit", ExceptionType.UNABLE_TO_COMMIT);
		}finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new DatabaseException("Cannot close connection object", ExceptionType.UNABLE_TO_CLOSE_CONNECTION);
				}
			}
		}
		return contact;
	}
	
	private int addContact(Connection connection, String firstName, String lastName, String address, String city,
			String state, String email, long zip, long phoneNumber, LocalDate date) throws DatabaseException {
		int contactId = -1;
		try(Statement statement = connection.createStatement()){
			String query = String.format("INSERT INTO contact (date_added, first_name, last_name, phone_number, email,address,city,state,zip)"+
										  "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')", date, firstName, lastName, phoneNumber, email,address,city,state,zip);
			int rowAffected = statement.executeUpdate(query, statement.RETURN_GENERATED_KEYS);
			contactId = -1;
			if(rowAffected == 1) {
				ResultSet result = statement.getGeneratedKeys();
				if(result.next()) {
					contactId = result.getInt(1);
				}
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
				throw new DatabaseException("Cannot Roll Back", ExceptionType.UNABLE_TO_ROLL_BACK);
			}
			throw new DatabaseException("Error while executing the query", ExceptionType.UNABLE_TO_EXECUTE_QUERY);
		}
		
		return contactId;
	}
	/**
	 * add contact to address_book_contact table
	 */
	private boolean addToContactAddressBookTable(Connection connection, int contactId, TYPE[] types) throws DatabaseException {
		boolean flag = true;
		try(Statement statement = connection.createStatement();){
			for(TYPE type : types) {
				int addressBookId = getAddressBookId(connection, type);
				if(addressBookId != -1) {
					String query = String.format("insert into address_book_contact (contact_id, book_id) "
							+ "Values ('%s','%s')", contactId, addressBookId);
					int rowAffected = statement.executeUpdate(query);
					if(rowAffected != 1) {	
						flag = false;
					}
				}
			}
			return flag;
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
				throw new DatabaseException("Cannot Roll Back", ExceptionType.UNABLE_TO_ROLL_BACK);
			}
			throw new DatabaseException("Error while executing the query", ExceptionType.UNABLE_TO_EXECUTE_QUERY);
		}
	}

	private int getAddressBookId(Connection connection, TYPE type) throws DatabaseException {
		int bookId = -1;
		String query = "Select book_id from address_book where type = ?";
		try(PreparedStatement statement = connection.prepareStatement(query);) {
			statement.setString(1, type.toString());
			ResultSet result = statement.executeQuery();
			while(result.next()) bookId = result.getInt("book_id");
			return bookId;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new DatabaseException("Error while executing the query", ExceptionType.UNABLE_TO_EXECUTE_QUERY);
		}
	}

}

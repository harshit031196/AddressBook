import java.util.*;
public class bookadress {
static HashMap<String, contactInfo> mp = new HashMap<>();
	//Method to add new contact
	public static contactInfo addContact() {
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
			contactInfo updated = addContact();
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
			contactInfo a = addContact();
			mp.put(a.getFirstName(), a);
		}
		System.out.println("All the contacts are added");
	}
	//Main method
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Welcome to Address Book");
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter the name of the address book");
		AddressBook ab = new AddressBook();
		
		System.out.println("Please choose from the following options and enter the number corresponding to that choice");
		System.out.println("1. Add a contact into the Address Book");
		System.out.println("2. Edit a contact already present in the Address Book");
		System.out.println("3. Delete a contact");
		System.out.println("4. Bulk add contacts in the Address Book");
		int choice = sc.nextInt();
		switch(choice) {
		case 1: 
			contactInfo c = addContact();
			if(mp.get(c.getFirstName()) == null) {
				System.out.println("The person is already present in the address book");
			}else {
				mp.put(c.getFirstName(),c);
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
		default: 
			break;
		}
	}

}

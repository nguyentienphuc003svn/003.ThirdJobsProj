import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class TeleBook {

	public static ArrayList<Contact> Fname = new ArrayList<Contact>();

	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//
//		TeleBook mc = new TeleBook();
//
//		String myChoice = mc.getChoice();
//		//System.out.println("Choice + [" + myChoice + "]");
//
//		while (!myChoice.equalsIgnoreCase("9"))
//		{
//			if (myChoice.equals("1")) mc.addContact();
//			if (myChoice.equals("2")) mc.sortByName();;
//			if (myChoice.equals("3")) mc.searchTelephone();;
//
//			System.out.println("\n\n");
//			myChoice = mc.getChoice();
//		}
//
//		//Save
//		System.out.println("\nEnd of Contact Manager");
//
//	}

	//Input Prompt
	public String getChoice()
	{
		System.out.println("Welcome to Contact Manager");
		System.out.println("--------------------");

		System.out.println("\nContact Manager Menu");
		System.out.println("--------------------");
		System.out.println("1 - Add contact");
		System.out.println("2 – Sort contact");
		System.out.println("3 – Search contacts");
		System.out.println("9 - Exit");
		System.out.println("\nEnter an option:");

		Scanner scan = new Scanner(System.in);
		String CustChoice = scan.nextLine();		
		while (CustChoice.matches("[0-9]+") == false) 
		{	
			System.out.println("\nEnter a VALID option:");
			CustChoice = scan.nextLine();
		}
		while (Integer.parseInt(CustChoice) != 1 && Integer.parseInt(CustChoice) != 2 && Integer.parseInt(CustChoice) != 3 && Integer.parseInt(CustChoice) != 9)
		{	
			System.out.println("\nEnter a VALID option:");
			CustChoice = scan.nextLine();
		}

		return CustChoice;
	}

	public void addContact()
	{
		Contact myContact = new Contact();
		
		//Fname
		System.out.println("Please enter the Name");
		Scanner scan = new Scanner(System.in);
		String fnameScan = scan.nextLine();		
		while (fnameScan.length()<=0) 
		{	
			System.out.println("Please enter the VALID First Name");
			fnameScan = scan.nextLine();
		}
		myContact.setName(fnameScan.trim());

		//Telephone
		System.out.println("Please enter the Telephone");
		String extensionScan = scan.nextLine();		
		while (extensionScan.length()<=0) 
		{	
			System.out.println("Please enter the VALID Extension");
			extensionScan = scan.nextLine();
		}
		while (extensionScan.matches("[0-9]+") == false) 
		{	
			System.out.println("Please enter the VALID Extension");
			extensionScan = scan.nextLine();
		}
		myContact.setTelephone(extensionScan.trim());
		
		TeleBook.Fname.add(myContact);
	}
	
	public void searchTelephone()
	{

		//Telephone
		System.out.println("Please enter the Telephone to search");
		Scanner scan = new Scanner(System.in);
		String extensionScan = scan.nextLine();		
		while (extensionScan.length()<=0) 
		{	
			System.out.println("Please enter the VALID Extension");
			extensionScan = scan.nextLine();
		}
		while (extensionScan.matches("[0-9]+") == false) 
		{	
			System.out.println("Please enter the VALID Extension");
			extensionScan = scan.nextLine();
		}
		
		Iterator<Contact> it = TeleBook.Fname.iterator();
		int count = 0;
		Contact foundContact = null;
		while (it.hasNext())
		{
			Contact tempContact = it.next();
			
			if (tempContact.getTelephone().trim().equals(extensionScan.trim())) {count++;foundContact=tempContact;} 
		}
		
		if (count > 0) System.out.println("Found person " + foundContact.getName() + " for tel " + extensionScan);
		else System.out.println("Not found any person for that telephone");
		
		
	}
	
	public void sortByName()
	{
		Contact[] ContactArray = new Contact[TeleBook.Fname.size()];
		ContactArray = TeleBook.Fname.toArray(ContactArray);
		
		for (int i = 0; i < ContactArray.length; i++)
		{
			for (int j = i+1; j < ContactArray.length; j++)
			{
				if (ContactArray[j].getName().compareToIgnoreCase(ContactArray[i].getName()) < 0)
				{
					Contact tempContact = ContactArray[i];
					ContactArray[i] = ContactArray[j];
					ContactArray[j] = tempContact;
				}
			}
		}
		
		TeleBook.Fname.clear();
		for (int i = 0; i < ContactArray.length; i++)
		{
			TeleBook.Fname.add(ContactArray[i]);
		}
	}
	
	public void listContact()
	{
		for (int i=0; i<TeleBook.Fname.size(); i++)
		{
			System.out.println(TeleBook.Fname.get(i).getName() + " has tel " + TeleBook.Fname.get(i).getTelephone());
		}
	}

//	public void showFiveContact()
//	{
//		for (int i=0; i<TeleBook.Fname.size(); i++)
//		{
//			System.out.println(TeleBook.Fname.get(i) + " " + TeleBook.Lname.get(i) + " " + TeleBook.Extension.get(i));
//		}
//	}

	public void showArray(Object[] showArray)
	{
		for (int i=0; i<showArray.length; i++)
		{
			System.out.println("Value at [" + (i+1) + "] is [" + String.valueOf(showArray[i]) + "]");
		}
	}



}

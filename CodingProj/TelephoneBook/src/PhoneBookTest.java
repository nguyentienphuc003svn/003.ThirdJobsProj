
public class PhoneBookTest {
	
	public static void main(String[] args) {

		TeleBook mc = new TeleBook();

		String myChoice = mc.getChoice();
		//System.out.println("Choice + [" + myChoice + "]");

		while (!myChoice.equalsIgnoreCase("9"))
		{
			if (myChoice.equals("1")) mc.addContact();
			if (myChoice.equals("2")) {mc.sortByName();mc.listContact();}
			if (myChoice.equals("3")) mc.searchTelephone();;

			System.out.println("\n\n");
			myChoice = mc.getChoice();
		}

		//Save
		System.out.println("\nEnd of Contact Manager");
	}

}


import java.io.*;
import java.util.*;


public class Client {
	
    /* The data that will be saved and restored */
	private static SimpleStack<Profile> profileStack = null;
	private final static String filename = "datafile.txt";
	
	public static void main(String[] args){
	    Client client = new Client();
        if(client.restore(filename)) {
            System.out.println("Restored from " + filename + ", found:");
        } else {
            System.out.println("Generating new data:");
            client.newData();
            System.out.println("Saving data...");
            client.save(filename);
        }	
		
		mainMenu();
	}

    /**
     * Attempts to restore from a previous save
     * @param fileName The filename of the save
     * @return true on success, false on failure
     */
	boolean restore(String filename) {
		try {
			ObjectInputStream restoreStream = new ObjectInputStream(new FileInputStream(filename));
			profileStack = (SimpleStack)restoreStream.readObject();
		}
		catch(FileNotFoundException e) {
			System.err.println(filename + " does not exist.");
			return false;
		}
		catch(IOException e) {
			System.err.println("Error resuming from " + filename);
			return false;
		}
		catch(ClassNotFoundException e) {
			System.err.println("Error resuming from " + filename);
			return false;
		}
        return true;
    }

    /**
     * Attempts to save the myData object to a file
     * @param fileName The filename of the save
     */
    public static void save(String filename) {
        try {
            ObjectOutputStream saveStream = new ObjectOutputStream(new FileOutputStream(filename));
            saveStream.writeObject(profileStack);
        }
        catch(IOException e) {
            System.err.println("Something went wrong saving to " + filename);
            e.printStackTrace();
        }
    }

    /**
     * Generates new data for the ProfileStack<Profile> profileStack object
     */
    void newData() {
        profileStack = new SimpleStack<Profile>();
    }
	/**
	*The mainMenu
	*Holds the main menu of the program
	**/
	public static void mainMenu(){
		Scanner kb = new Scanner(System.in);
		
		System.out.println("");
		System.out.println("Main menu: ");
		System.out.print("1. List Profiles");
		System.out.print("\t\t2. Create a New Profile");
	    System.out.print("\n3. Show Profile");
		System.out.print("\t\t\t4. Edit Profile");
	    System.out.print("\n5. Follow a Profile");
		System.out.print("\t\t6. Unfollow a Profile");
		System.out.println("\n7. Friend Recommendation");
		System.out.println("0. Exit NameBook");
		
		int ans = 0;
		boolean loop = true;
		while(loop == true){
			System.out.print("Please enter a menu option: ");
			try{
				ans = kb.nextInt();
				if((ans < 0 )|| (ans > 7)){
					loop = true;
				}
				else{
					loop = false;
				}
			}
			catch(InputMismatchException ime){
				System.out.println("I'm sorry, that is not an acceptable answer. Try again.");
				kb.next();
				loop = true;
			}
		}
		
		
		if(ans == 1){
			list();
			save(filename);
			mainMenu();
		}
		
		if(ans == 2){
			Profile x = create();
			profileStack.add(x);
			save(filename);
			mainMenu();
		}	
		
		if(ans == 3){
			show();
			save(filename);
			mainMenu();
		}
		
		if(ans == 4){
			edit();
			save(filename);
			mainMenu();
		}
		
		if(ans == 5){
			follow();
			save(filename);
			mainMenu();
		} 
		if(ans == 6){
			unfollow();
			save(filename);
			mainMenu();
		}
		if(ans == 7){
			recommend();
			save(filename);
			mainMenu();
		}
		if(ans == 0){
			save(filename);
			quit();
		}

	}
	/**
	*The list() method
	* Displays a list of profiles for the user to view
	**/
	public static void list(){
		System.out.println("");
		System.out.println("The profiles that currently exist are: ");
		int count = 1;
		int i = profileStack.size();
		
		//Casts the future project[] as an object[] because topItems deals with generics
		Object[] profileCast = profileStack.topItems(i);
		
		//lists profiles
		for (Object o: profileCast){
			Profile profile = (Profile)o;
			System.out.println(count+". " + profile.getName());
			count++;
		}
		

		mainMenu();
	}
	/**
	* The create() method
	* Creates a new Profile object to add to profileStack
	* @return userInfo The new profile that contains the user's info
	**/
	public static Profile create(){
		System.out.println("");
		System.out.println("Ok! Let's make you a new profile!");
		
		
		Scanner kb = new Scanner(System.in);
		String name;
		System.out.print("Please enter your name: ");
		name = kb.nextLine();
		
		
		Profile userInfo = new Profile();
		
		//set name
		userInfo.setName(name);
		
		System.out.println("Okay, great "+name+", Now let's get your 'about me' section!");
		//set about
		System.out.print("Your About Me: ");
		String about = kb.nextLine();
		userInfo.setAbout(about);
	

		return userInfo;
	}	
	/**
	* The show() method
	* Shows a profile to the user
	**/
	public static void show(){
		
		int i = profileStack.size();
		int ans = chooseProfile();
		Object[] profileCast = profileStack.topItems(i);
		Object o = profileCast[ans - 1];
		Profile profile = (Profile)o;
		System.out.println("");
		System.out.println("The profile you selected: ");
		System.out.println("Name: "+profile.getName());
		System.out.println("About: "+profile.getAbout());
		System.out.println("(Up to) Three most recently followed friends:");
		ProfileInterface[] friends = profile.following(3);
		if(friends == null){
			System.out.println("This person has no friends :-(");
			mainMenu();
		}
		//avoiding a null pointer if the length of friends[] is < 3
		if(friends.length == 1){
			System.out.println("\t"+friends[0].getName());
		}
		else if(friends.length == 2){
			System.out.println("\t"+friends[0].getName());
			System.out.println("\t"+friends[1].getName());
		}
		
		//otherwise, just write the name of the recent profiles
		else{
			for(int j = 0; j<3; j++){
				System.out.println("\t"+friends[j].getName());
			}
		}
		

		
	}
	/**
	* The edit() method
	* Allows the user to edit a specific profile
	**/
	public static void edit(){
		Scanner kb = new Scanner(System.in);
		int i = profileStack.size();
		int ans = chooseProfile();
		
		Object[] profileCast = profileStack.topItems(i);
		Object o = profileCast[ans - 1];
		Profile profile = (Profile)o;
		
		System.out.println("The profile you selected: ");
		System.out.println("Name: "+profile.getName());
		System.out.println("About: "+profile.getAbout());
		
		//this block is pretty self-explanitory
		boolean loop = true;
		String ansUser = "";
		while(loop == true){
			System.out.print("Do you want to change the name? Yes or no: ");
			ansUser = kb.nextLine();
			if(ansUser.equalsIgnoreCase("yes")){
				System.out.print("What do you want to change the name to?: ");
				String newName = kb.nextLine();
				profile.setName(newName);
				loop = false;
				System.out.println("");
			}
			else if(ansUser.equalsIgnoreCase("no")){
				System.out.println("Ok! No name change.");
				loop = false;
				
			}
			else{
				System.out.println("I'm sorry, that is not a valid answer. Try again");
				loop = true;
			}
		}
		loop = true;
		ansUser = "";
		while(loop == true){
			System.out.print("Do you want to change the about? Yes or no: ");
			ansUser = kb.nextLine();
			if(ansUser.equalsIgnoreCase("yes")){
				System.out.print("What do you want to change the about to?: ");
				String newAbout = kb.nextLine();
				profile.setAbout(newAbout);
				loop = false;
			}
			else if(ansUser.equalsIgnoreCase("no")){
				System.out.println("Ok! No about change.");
				loop = false;
				
			}
			else{
				System.out.println("I'm sorry, that is not a valid answer. Try again");
				loop = true;
			}
		}		
		
		System.out.println("The profile's updated information: ");
		System.out.println("Name: "+profile.getName());
		System.out.println("About: "+profile.getAbout());
	
	}
	/**
	* The follow() method
	* Allows the user to make one profile follow another
	**/
	public static void follow(){
	    int i = profileStack.size();
		Object[] profileCast = profileStack.topItems(i);
		
		ProfileInterface profileOne = null;
		ProfileInterface profileTwo = null;
		
		System.out.println("");
		System.out.println("Okay, let's follow a profile!");
		System.out.println("Choose the profile to add a friend to");
		int userAnsOne = chooseProfile();
		
		int userAnsTwo = 0;
		boolean loop = true;
		
		Object o = profileCast[userAnsOne - 1];
		profileOne = (Profile)o;
		System.out.println("Number one: "+profileOne.getName());
		
		
		while(loop == true){
			System.out.println("Choose a profile to befriend: ");
			userAnsTwo = chooseProfile();
			Object ob = profileCast[userAnsTwo - 1];
			profileTwo = (Profile)ob;
			
			if(userAnsOne == userAnsTwo){
				System.out.println("I'm sorry, but a profile cannot follow itself! Try again.");
				loop = true;
			}
			else{
				loop = false;
			}
		}

		profileOne.follow(profileTwo);

		
		
		System.out.println("Friend list: "+profileOne.following(1)[0].getName());
		
		
		System.out.println("Okay, great! "+profileOne.getName()+" followed "+profileTwo.getName());
		
	}
	/**
	* The unfollow() method
	* Allows the user to choose a profile, and make it unfollow the most recently followed profile
	**/
	public static void unfollow(){
        int i = profileStack.size();
		Object[] profileCast = profileStack.topItems(i);
		
		ProfileInterface profileOne = null;
		ProfileInterface profileTwo = null;
		
		System.out.println("");
		System.out.println("Okay, let's unfollow a profile!");
		System.out.println("Choose the profile to remove a friend from");
		int userAnsOne = chooseProfile();
		

		//casting of object to eventually become Profiles
		Object o = profileCast[userAnsOne - 1];
		profileOne = (Profile)o;
		System.out.println("Acting as: "+profileOne.getName());
		
		Profile goodbye = (Profile)profileOne.unfollow();

		
		
		System.out.println("You unfriended "+goodbye.getName());
		
		
		
	}
	/**
	* The recommend() method
	* Gives the user a profile to potentially follow, based on the friends of the friend of an arbitrary profile
	* Will say "no profile to recommend" if the friend's profile list is null
	**/
	public static void recommend(){
		Scanner kb = new Scanner(System.in);
		System.out.println();
		System.out.println("Please choose the file that you want to get a recommendation for: ");
		
		int i = profileStack.size();
		int ans = chooseProfile();
		
		//casting Objects from topItems to eventually become profiles
		Object[] profileCast = profileStack.topItems(i);
		Object o = profileCast[ans - 1];
		ProfileInterface profile = (Profile)o;
		
		ProfileInterface rec = profile.recommend();
		
		if (rec == null){
			System.out.println("I'm sorry, there is no one to recommend :-(");
		}
		else{
			System.out.println("Recommendation: "+rec.getName());
			boolean loop = true;
			while(loop){
				System.out.print("Do you want to follow this person?: ");
				String userAns = kb.next();
				if(userAns.equalsIgnoreCase("yes")){
					profile.follow(rec);
					System.out.println("Okay, followed!");
					loop = false;
				}
				else if(userAns.equalsIgnoreCase("no")){
					System.out.println("Ok, I won't follow them.");
					loop = false;
				}
				else{
					System.out.println("That is not a valid entry. Try entering yes or no.");
					loop = true;
				}
			}
		}
		System.out.println("Back to main menu!");
		mainMenu();
	}
	/**
	* The chooseProfile() method
	* Allows the user to pick a profile from a list of current profiles. 
	*@return ans The number of the profile that the user wants
	**/
	public static int chooseProfile(){
		Scanner kb = new Scanner(System.in);
		int count  = 1;
		int ans = 0;
		System.out.println("");
		System.out.println("The profiles that currently exist are: ");
		
		int i = profileStack.size();
		//casting the profiles as an object bc topItems() deals with generics
		Object[] profileCast = profileStack.topItems(i);
		
		//cast all objects to Profiles 
		for (Object o: profileCast){
			Profile profile = (Profile)o;
			System.out.println(count+". " + profile.getName());
			count++;
		}		
		//profile selection
		boolean loop = true;
		while(loop == true){
			System.out.print("Please choose a profile to select: ");
			try{
				ans = kb.nextInt();

			}
			catch(InputMismatchException ime){
				System.out.println("I'm sorry, that's not a valid answer. Try again.");
				loop = true;
			}
			if(ans < 1){
				System.out.println("I'm sorry, that's not a valid answer. Try again.");
				loop = true;
			}
			else if(ans > count){
				System.out.println("I'm sorry, that's not a valid answer. Try again.");
				loop = true;
			}
			else{
				loop  = false;
			}
		}
		
		return ans;
		
	}
	/**
	* The quit() method
	* Exits the profile 
	**/
	public static void quit(){
		System.out.println("Thank you for using NameBook. Goodbye");
		System.exit(0);
	}
}

 
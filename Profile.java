import java.io.*;
import java.util.Random;
public class Profile implements ProfileInterface,java.io.Serializable{
	
	private SimpleStack<ProfileInterface> friendList= new SimpleStack<ProfileInterface>(250);
	private String name;
	private String about;
		
    /**
     * Sets the profile's name
     * @param name The new name
     */
    public void setName(String n){
		name = n;
    }

    /**
     * Gets the profile's name
     * @return The name
     */
    public String getName(){
		return name;
	}

    /**
     * Sets the profile's "about me" blurb
     * @param about The new blurb
     */
    public void setAbout(String abt){
		about = abt;
		
	}

    /**
     * Gets the profile's "about me" blurb
     * @return The blurb
     */
    public String getAbout(){
		return about;
	}
	
	/**
	* Gets the profile's friendList
	* @return the friendList
	**/
	public SimpleStack<ProfileInterface> getFriendList(){
		return friendList;
	}

    /**
     * Adds another profile to this profile's following stack. The most obvious failure is when this
     * profile already follows the maximum number of other profiles. Although the stack may be
     * capable of holding duplicate items, this method should also return false if this profile is
     * already following other.
     * @param other The profile to follow
     * @return True if successful, false otherwise
	 **/
    public boolean follow(ProfileInterface other){
		boolean success = true;
		/**
		if(friendList.contains(other)){
			success = false;                                      ;
		}
		else{
			friendList.add(other);
			success = true;
		} **/
		
		friendList.add(other);
		return success;
	}

    /**
     * Removes the most recently-followed profile from this profile's following stack
     * @return The profile that was unfollowed
     */
    public ProfileInterface unfollow(){
		
		ProfileInterface removeMe = friendList.remove();
		return removeMe;
	}

    /**
     * Returns this profile's most recent follows
     * @param howMany The number of profiles to return
     * @return An array of size howMany, containing the most recently-followed profiles
     */
    public ProfileInterface[] following(int howMany){
		
	    int i = friendList.size();
		//you can't make a new array thats len is < 1
		if(howMany < 1 ){
			return null;
		}
		//if the person has no friends 
		if(i == 0){
			return null;
		}
		//if howMany is bigger than the size of the array
		if(howMany > i ){
			howMany = i;
			System.out.println("Only "+i+" friend(s) exist.");
		}
		
		//gaining access to profiles we want
		ProfileInterface[] recentFollows = new ProfileInterface[i];
		Object[] recentCast = friendList.topItems(howMany);
		int j = 0;
	    for (Object o: recentCast){
			Profile profile = (Profile)o;
			recentFollows[j] = profile;
			j++;
		}

		return recentFollows;
		
	}

    /**
     * Recommend a profile for this profile to follow. For now, this can return any arbitrary
     * profile followed by one of this profile's followed profiles. For example, if this profile is
     * Alex, and Alex follows Bart, and Bart follows Crissy, this method might return Crissy. You
     * may implement more intelligent selection (e.g., ensuring that the selection is not already
     * followed), but it is not required.
     * @return The profile to suggest, or null if no suitable profile is found.
     */
    public ProfileInterface recommend(){
		int i = friendList.size();
		int count = 0;
		
		//making the new friends list
		Object[] friendsCast = friendList.topItems(i);
		Profile[] friends = new Profile[i];
		
		for (Object o: friendsCast){
			Profile profile = (Profile)o;
			friends[count] = profile;
			
			count++;
		}
		
		
		
		Random randInt = new Random();
		int position = randInt.nextInt(i);
		//basically, the recommend chooses a random friend to pick a recommendation from 
		Profile randomFriend = friends[position];
		
		SimpleStack<ProfileInterface> theirFriends = randomFriend.getFriendList();
		
		//if that friend's friend list is empty, they don't have a friend to recommend
		if(theirFriends.isEmpty()){
			return null;
		}
		
		ProfileInterface recommend = null;
		
		int j = theirFriends.size();
		//if they don't have any friends, they don't have anything to recommend
		if(j == 0){
			return recommend;
		}
				
		//getting access to the recommendation 
		Object[] randFriendsCast = theirFriends.topItems(j);
		Profile[] randFriends = new Profile[j];
		count = 0;

		for (Object o: randFriendsCast){
			Profile profile = (Profile)o;
			randFriends[count] = profile;
			count++;
		}
		
		
		//random friend of that friend's friend list is the recommendation
		int positionRec = randInt.nextInt(j);
		recommend = randFriends[positionRec];
		
	    return recommend;
	}
}

import java.io.*;
public class SimpleStack<T> implements SimpleStackInterface<T>,java.io.Serializable{
	
	private T[] contents;  //the stack, implemented as an array
	private int tracker; //keeps track of the next place where you could "put" an item better access time
	private final int length; //final variable to keep track of the total length of the array
	
	public SimpleStack(int length){
		this.length = length;
		contents =(T[])new Object[length];
	    tracker = 0;
	}
	
	public SimpleStack(){
		contents = (T[])new Object[250];
		length = 1000;
		tracker = 0;
	}
	
 /**
     * Adds a new String object to the top of the stack
     * @param item The item to be added
     * @return True if successful, false if there is no more room
     */
    public boolean add(T item){
		boolean result;
		
		if(isFull()){
			result = false;
		}
		else{
			contents[tracker] = item;
			tracker++;
			result = true;	
		}
		
		return result;
		
	}

    /**
     * Removes the top T object
     * @return The item that is removed, or null if no item can be removed
     */
    public T remove(){
		T item;
		
		if(isEmpty()){
			item = null;
		}
		else{
			item = contents[tracker-1];
			tracker = (tracker - 1);
		}
		
		return item;
	}
		

    /**
     * Returns an array of size n containing the top n items from the stack. The most recently-added
     * items should be listed first.
     * @param howMany The number of items to return
     * @return An array of the most recently-added items, or null if the stack does not contain
     * enough items
     */
    public T[] topItems(int howMany){
		
		T[] newList = (T[])new Object[howMany];
		int iteration = 0;
		int arrayItem = tracker-1;
		
		if(howMany >(tracker)){
			newList = null;
		}
		else{
			while (iteration < howMany){
				newList[iteration] = contents[arrayItem];
				iteration++;
				arrayItem --;
			}
		}
		
		return newList;
	}

    /**
     * Determines if the stack contains the specified item
     * @param item The item in question
     * @return True if the item is contained in the stack, false otherwise
     */
    public boolean contains(T item){
		boolean match = false;
		if(tracker > 0){
			for(int i=0; i< contents.length; i++){
			//for(T element: contents){
				T element = contents[i];
				if(element.equals(item)){
					match = true; 
				}
			}
		}
		
		return match;
	}

    /**
     * Determines if the stack is empty
     * @return True if the stack is empty, false otherwise
     */
    public boolean isEmpty(){
		boolean ret = true;
		if(tracker == 0){
			ret = true;
		}
		else{
			ret = false;
		}
		return ret;
	}

    /**
     * Determines if the stack is full (at capacity)
     * @return True if the stack is full, false otherwise
     */
    public boolean isFull(){
		boolean full = true;
		if(tracker == length){
			full = true;
		}
		else{
			full = false;
		}
		return full;
	}

    /**
     * Determines the size of the stack (the current number of objects in the stack, NOT the
     * capacity)
     * @return The number of objects in the stack
     */
    public int size(){
		return tracker;
	}
	
}
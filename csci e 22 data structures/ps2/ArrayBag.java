/*
 * ArrayBag.java
 *
 * Computer Science E-22
 *
 * Modified by: <your name>, <your e-mail address>
 */

import java.util.Scanner;

/**
 * An implementation of the Bag ADT using an array.
 */
public class ArrayBag implements Bag {
    /**
     * The array used to store the items in the bag.
     */
    private Object[] items;

    /**
     * The number of items in the bag.
     */
    private int numItems;

    public static final int DEFAULT_MAX_SIZE = 50;

    /**
     * Constructor with no parameters - creates a new, empty ArrayBag with
     * the default maximum size.
     */
    public ArrayBag() {
        this.items = new Object[DEFAULT_MAX_SIZE];
        this.numItems = 0;
    }

    /**
     * A constructor that creates a new, empty ArrayBag with the specified
     * maximum size.
     */
    public ArrayBag(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize must be > 0");
        }
        this.items = new Object[maxSize];
        this.numItems = 0;
    }

    /**
     * numItems - accessor method that returns the number of items
     * in this ArrayBag.
     */
    public int numItems() {
        return this.numItems;
    }

    /**
     * add - adds the specified item to this ArrayBag. Returns true if there
     * is room to add it, and false otherwise.
     * Throws an IllegalArgumentException if the item is null.
     */
    public boolean add(Object item) {
        if (item == null) {
            throw new IllegalArgumentException("item must be non-null");
        } else if (this.numItems == this.items.length) {
            return false;    // no more room!
        } else {
            this.items[this.numItems] = item;
            this.numItems++;
            return true;
        }
    }

    /**
     * remove - removes one occurrence of the specified item (if any)
     * from this ArrayBag.  Returns true on success and false if the
     * specified item (i.e., an object equal to item) is not in this ArrayBag.
     */
    public boolean remove(Object item) {
        for (int i = 0; i < this.numItems; i++) {
            if (this.items[i].equals(item)) {
                // Shift the remaining items left by one.
                for (int j = i; j < this.numItems - 1; j++) {
                    this.items[j] = this.items[j + 1];
                }
                this.items[this.numItems - 1] = null;

                this.numItems--;
                return true;
            }
        }

        return false;  // item not found
    }

    /**
     * contains - returns true if the specified item is in the Bag, and
     * false otherwise.
     */
    public boolean contains(Object item) {
        for (int i = 0; i < this.numItems; i++) {
            if (this.items[i].equals(item)) {
                return true;
            }
        }

        return false;
    }

    /**
     * containsAll - does this ArrayBag contain all of the items in
     * otherBag?  Returns false if otherBag is null or empty.
     */
    public boolean containsAll(Bag otherBag) {
        if (otherBag == null || otherBag.numItems() == 0) {
            return false;
        }

        Object[] otherItems = otherBag.toArray();
        for (int i = 0; i < otherItems.length; i++) {
            if (!this.contains(otherItems[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * grab - returns a reference to a randomly chosen item in this ArrayBag.
     */
    public Object grab() {
        if (this.numItems == 0) {
            throw new IllegalStateException("the bag is empty");
        }

        int whichOne = (int) (Math.random() * this.numItems);
        return this.items[whichOne];
    }

    /**
     * toArray - return an array containing the current contents of the bag
     */
    public Object[] toArray() {
        Object[] copy = new Object[this.numItems];

        for (int i = 0; i < this.numItems; i++) {
            copy[i] = this.items[i];
        }

        return copy;
    }

    /**
     * toString - converts this ArrayBag into a string that can be printed.
     * Overrides the version of this method inherited from the Object class.
     */
    public String toString() {
        String str = "{";

        for (int i = 0; i < this.numItems; i++) {
            str = str + this.items[i];
            if (i != this.numItems - 1) {
                str += ", ";
            }
        }

        str = str + "}";
        return str;
    }

    /**
     * capacity - reports on the maximum capacity of the ArrayBag.
     */
    public int capacity() {
        return this.items.length;
    }

    /**
     * isFull - reports on whether the ArrayBag cannot store any further items.
     */
    public boolean isFull() {
        return this.numItems == this.capacity();
    }

    /**
     * increaseCapacity - increases the capacity of the ArrayBag by the specified amount.
     */
    public void increaseCapacity(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("maxSize must be positive");
        } else if (amount > 0) {
            Object[] newBagArr = new Object[this.capacity() + amount];
            for (int i = 0; i < this.items.length; i++) {
                newBagArr[i] = this.items[i];
            }
            this.items = newBagArr;
        }
    }

    /**
     * removeItems - removes all occurrences of the items (if any)
     * from the parameter ArrayBag.  Returns true if any removal took place and false if not.
     */
    public boolean removeItems(Bag other) {
        if (other == null) {
            throw new IllegalArgumentException("parameter must be non-null");
        } else if (other.numItems() == 0) {
            return false;
        } else {
            Object[] otherItems = other.toArray();
            int prevItems = this.numItems;

            for (int i = 0; i < otherItems.length; i++) {
                this.remove(otherItems[i]);
            }

            return (!(prevItems == this.numItems));
        }
    }

    /**
     * unionWith - returns a new bag containing unique occurrences of the items (if any)
     * from the parameter Bag and current object Bag.
     */
    public Bag unionWith(Bag other) {
        if (other == null) {
            throw new IllegalArgumentException("parameter must be non-null");
        } else {
            Object[] otherItems = other.toArray();
            ArrayBag unionBag = new ArrayBag(this.items.length + otherItems.length);
            if (this.numItems == 0 && other.numItems() == 0) {
                return unionBag;
            }

            for (int i = 0; i < this.numItems; i++) {
                if (!unionBag.contains(this.items[i])) {
                    unionBag.add(this.items[i]);
                }
            }
            for (int i = 0; i < otherItems.length; i++) {
                if (!unionBag.contains(otherItems[i])) {
                    unionBag.add(otherItems[i]);
                }
            }

            return unionBag;
        }
    }


    /* Test the ArrayBag implementation. */
    public static void main(String[] args) {
        // Create a Scanner object for user input.
        Scanner scan = new Scanner(System.in);

        // Create an ArrayBag named bag1.
        System.out.print("size of bag 1: ");
        int size = scan.nextInt();
        ArrayBag bag1 = new ArrayBag(size);
        scan.nextLine();    // consume the rest of the line

        // Read in strings, add them to bag1, and print out bag1.
        String itemStr;
        for (int i = 0; i < size; i++) {
            System.out.print("item " + i + ": ");
            itemStr = scan.nextLine();
            bag1.add(itemStr);
        }
        System.out.println("bag 1 = " + bag1);
        System.out.println();

        // Select a random item and print it.
        Object item = bag1.grab();
        System.out.println("grabbed " + item);
        System.out.println();

        // Iterate over the objects in bag1, printing them one per
        // line.
        Object[] items = bag1.toArray();
        for (int i = 0; i < items.length; i++) {
            System.out.println(items[i]);
        }
        System.out.println();

        // Get an item to remove from bag1, remove it, and reprint the bag.
        System.out.print("item to remove: ");
        itemStr = scan.nextLine();
        if (bag1.contains(itemStr)) {
            bag1.remove(itemStr);
        }
        System.out.println("bag 1 = " + bag1);
        System.out.println();

        // Check that capacity of the ArrayBag reports correctly.
        System.out.println(bag1.capacity());

        // Check that whether the ArrayBag is full reports correctly
        System.out.println(bag1.isFull());

        // Increase capacity of the array bag. Checks that it's taken place, and that the original items are intact.
        System.out.print("amount to increase capacity of bag by: ");
        bag1.increaseCapacity(scan.nextInt());
        scan.nextLine();
        System.out.println("new capacity: " + bag1.capacity());
        System.out.println("bag 1 = " + bag1.toString());
        System.out.println("num items: " + bag1.numItems);
        System.out.println();

        // Testing removeItems
        System.out.println("Removing all instances of Bag 1 within Bag 2");
        // Create an ArrayBag named bag2.
        System.out.print("size of bag 2: ");
        int size2 = scan.nextInt();
        ArrayBag bag2 = new ArrayBag(size2);
        scan.nextLine();    // consume the rest of the line

        // Read in strings, add them to bag2, and print out bag2.
        String itemStr2;
        for (int i = 0; i < size2; i++) {
            System.out.print("item " + i + ": ");
            itemStr2 = scan.nextLine();
            bag2.add(itemStr2);
        }
        System.out.println("any items removed? " + bag1.removeItems(bag2));
        System.out.println("result: " + bag1);

        // Testing unionWith
        System.out.println("Returning the union of bag 1 and bag 3");
        // Create an ArrayBag named bag3.
        System.out.print("size of bag 3: ");
        int size3 = scan.nextInt();
        ArrayBag bag3 = new ArrayBag(size3);
        scan.nextLine();    // consume the rest of the line

        // Read in strings, add them to bag1, and print out bag1.
        String itemStr3;
        for (int i = 0; i < size3; i++) {
            System.out.print("item " + i + ": ");
            itemStr3 = scan.nextLine();
            bag3.add(itemStr3);
        }
        System.out.println("union: " + bag1.unionWith(bag3));
    }
}

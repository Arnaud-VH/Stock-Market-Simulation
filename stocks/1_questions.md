# Question 1

Suppose you are developing a similar (if not identical) project for a company. One teammate poses the following:

> "We do not have to worry about logging. The application is very small and tests should take care of any potential bugs. If we really need it, we can print some important data and just comment it out later."

Do you agree or disagree with the proposition? Please elaborate on your reason to agree or disagree. (~50-100 words)

___
**Answer**:

___

# Question 2

Suppose you have the following `LinkedList` implementation:

![LinkedList](images/LinkedList.png)

How could you modify the `LinkedList` class so that the value could be any different data type? Preferably, provide the code of the modified class in the answer.
___

**Answer**:
This can be done by using a class level generic. By using a class level generic we make it so that our Linked List can be re-used for any data type instead of making new LinkedList and Node methods for all data types we want.
Before we could only store integers in our nodes but now we can store any data type in our nodes: String, Double etc... because our generic methods are not type dependent. 
```java
public class Node<T> {
    private T value;
    private Node<T> next;

    public Node(T value) {
        this.value = value;
    }

    public Node getNext() {
        return next;
    }
}

public class LinkedList<T> {
    private Node<T> head;
    private int size;
    
    public LinkedList(Node<T> head) {
        this.head = head;
        this.size = 1;
    }
    
    void insert(T value);
    
    void delete(T value);
    
    int getSize() {
        return size;
    }
}


```

___

# Question 3

How is Continuous Integration applied to (or enforced on) your assignment? (~30-100 words)

___

**Answer**:
Continuous Integration happens in our assignment as we systematically clean compile the project and run all the unit tests written for the program.
By doing this we get instant feedback on the code written so that bugs can be fixed systematically without bug-infested code being pushed onto the git repository. As well, 
by continuous integration we can find bugs in our code early and isolate these bugs, preventing them from having a big impact on the project as a whole. Lastly, by enforcing 
continuous integration it makes collaboration the project easier, as less buggy code is shared between developers. 

___

# Question 4

One of your colleagues wrote the following class:

```java
import java.util.*;

public class MyMenu {

    private Map<Integer, PlayerAction> actions;

    public MyMenu() {
        actions = new HashMap<>();
        actions.put(0, DoNothingAction());
        actions.put(1, LookAroundAction());
        actions.put(2, FightAction());
    }

    public void printMenuOptions(boolean isInCombat) {
        List<String> menuOptions = new ArrayList<>();
        menuOptions.add("What do you want to?");
        menuOptions.add("\t0) Do nothing");
        menuOptions.add("\t1) Look around");
        if(isInCombat) {
            menuOptions.add("\t2) Fight!");
        }
        menuOptions.forEach(System.out::println);
    }

    public void doOption() {
        int option = getNumber();
        if(actions.containsKey(option)) {
            actions.get(option).execute();
        }
    }

    public int getNumber() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }
}
```
List at least 2 things that you would improve, how it relates to test-driven development and why you would improve these things. Provide the improved code below.

___

**Answer**:

-
-

Improved code:

```java

```
___
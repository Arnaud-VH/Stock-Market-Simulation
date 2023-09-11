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

```java
// write the code of the modified class here
```

___

# Question 3

How is Continuous Integration applied to (or enforced on) your assignment? (~30-100 words)

___

**Answer**:

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
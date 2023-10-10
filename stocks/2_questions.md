# Question 1

In the assignment, you had to create a `MessageHandler` interface. Please answer the following two questions:

1. Describe the benefits of using this `MessageHandler` interface. (~50-100 words)
2. Instead of creating an implementation of `MessageHandler` that invokes a command handler, we could also pass the command handler to the client/server directly without the middle man of the `MessageHandler` implementation. What are the implications of this? (~50-100 words)

___

**Answer**:
___

# Question 2

One of your colleagues wrote the following class:

```java
public class RookieImplementation {

    private final Car car;

    public RookieImplementation(Car car) {
        this.car = car;
    }

    public void carEventFired(String carEvent) {
        if("steer.left".equals(carEvent)) {
            car.steerLeft();
        } else if("steer.right".equals(carEvent)) {
            car.steerRight();
        } else if("engine.start".equals(carEvent)) {
            car.startEngine();
        } else if("engine.stop".equals(carEvent)) {
            car.stopEngine();
        } else if("pedal.gas".equals(carEvent)) {
            car.accelerate();
        } else if("pedal.brake".equals(carEvent)) {
            car.brake();
        }
    }
}
```

This code makes you angry. Briefly describe why it makes you angry and provide the improved code below.

___

**Answer**:
This code makes me angry because the developer missed an opportunity to clean up their code, specifically their large if statement, by using a Design Pattern. The Design Pattern they could have used is the Command Pattern. 
Here, the developer should implement the Command interface in their Car Class. The Command interface has an `execute()` method that they have to define. The second part to cleaning up their code is to map the command's names, like `steer.left`, `engine.stop`, etc.. to a Map that contains these commands. Every Command in this Map can be accessed by a Key, which is the command's name, and 
the command can then be executed using the `execute()` method that every Command has. 

Improved code:

```java
public interface Command {
    void execute();
}
```

```java
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class RookieImplementation {

    private final Car car;
    private Map<String, Command> commandMap;

    public RookieImplementation(Car car) {
        this.car = car;
        commandMap = new HashMap<>();
        commandMap.put("steer.left", new SteerLeftCommand(car));
        commandMap.put("steer.right", new SteerRightCommand(car));
        commandMap.put("engine.start", new EngineStartCommand(car));
        commandMap.put("engine.stop", new EngineStopCommand(car));
        commandMap.put("pedal.gas", new PedalGasCommand(car));
        commandMap.put("pedal.brake" , new PedalBreakCommand(car));        
    }

    public void carEventFired(String carEvent) {
        if (commandMap.containsKey(carEvent)) {
            commandMap.get(carEvent).execute();
        } else {
            log.info("This command does not exist");
        }
    }
}

//Below is an example of what the Command classes should look like that implement the Command interface.
```

```java
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SteerLeftCommand implements Command {
    private final Car car;

    public SteerLeftCommand(Car car) {
        this.car = car;
    }

    @Override
    public void execute() {
        //The code that should be executed when the Steer Left command is called
        car.setX(car.getX() - 10);
        log.info("Car steered left");
    }
}
```
___

# Question 3

You have the following exchange with a colleague:

> **Colleague**: "Hey, look at this! It's super handy. Pretty simple to write custom experiments."

```java
class Experiments {
    public static Model runExperimentA(DataTable dt) {
        CommandHandler commandSequence = new CleanDataTableCommand()
            .setNext(new RemoveCorrelatedColumnsCommand())
            .setNext(new TrainSVMCommand())

        Config config = new Options();
        config.set("broadcast", true);
        config.set("svmdatatable", dt);

        commandSequence.handle(config);

        return (Model) config.get("svmmodel");
    }

    public static Model runExperimentB() {
        CommandHandler commandSequence = new CleanDataTableCommand()
            .setNext(new TrainSGDCommand())

        Config config = new Options();
        config.set("broadcast", true);
        config.set("sgddatatable", dt);

        commandSequence.handle(config);

        return (Model) config.get("sgdmodel");
    }
}
```

> **Colleague**: "I could even create this method to train any of the models we have. Do you know how Jane did it?"

```java
class Processor {
    public static Model getModel(String algorithm, DataTable dt) {
        CommandHandler commandSequence = new TrainSVMCommand()
            .setNext(new TrainSDGCommand())
            .setNext(new TrainRFCommand())
            .setNext(new TrainNNCommand())

        Config config = new Options();
        config.set("broadcast", false);
        config.set(algorithm + "datatable", dt);

        commandSequence.handle(config);

        return (Model) config.get(algorithm + "model");
    }
}
```

> **You**: "Sure! She is using the command pattern. Easy indeed."
>
> **Colleague**: "Yeah. But look again. There is more; she uses another pattern on top of it. I wonder how it works."

1. What is this other pattern? What advantage does it provide to the solution? (~50-100 words)

2. You know the code for `CommandHandler` has to be a simple abstract class in this case, probably containing four methods:
- `CommandHandler setNext(CommandHandler next)` (implemented in `CommandHandler`),
- `void handle(Config config)` (implemented in `CommandHandler`),
- `abstract boolean canHandle(Config config)`,
- `abstract void execute(Config config)`.

Please provide a minimum working example of the `CommandHandler` abstract class.

___

**Answer**:

1.

2.
	```java

	```
___

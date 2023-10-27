<br />
<p align="center">
  <h1 align="center">Stock Market Simulation</h1>

  <p align="center">
    Simulation of a Stock Exchange where trading bots continuously place bids and asks onto the stock exchange using Java Networking.
  </p>
</p>

## Table of Contents

* [About the Project](#about-the-project)
  * [Built With](#built-with)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
  * [Running](#running)
* [Modules](#modules)
* [Notes](#notes)
* [Evaluation](#evaluation)
* [Extras](#extras)

## About The Project
The Stock Market Simulation project creates a virtual stock exchange to which traders (bots) can connect and simulate the trading of stocks. 
The Exchange is hosted as a server and the bots connect to this server as clients. Communication between exchange and the bots happens through java networking. 
The bots can place buy and sell orders (asks and bids). These are sent across the network in the form of commands which are handled on the server side. 
The simulation also comes with a view that updates in real time to show how stock prices and traders their portfolio's change as they trade. 


## Getting Started

To get a local copy up and running follow these simple steps:

1. Go to the github repository and copy the link under `<code>`
2. In your terminal, type: git clone "url", with the URL you just copied. 
3. Open intelli (or another IDE) through the POM.xml that you got from cloning the github repository. 
### Prerequisites

* [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or higher
* [Maven 3.6](https://maven.apache.org/download.cgi) or higher

### Installation

1. Navigate to the `stocks` directory
2. Clean and build the project using:
```sh
mvn install
```

### Running
After having cloned the github repository and 

1. To run the Stock Simulation. 
    1. Run the `MainStockApplication` in the Stock-Application module
    2. Run the `MainTraderApplication` in the Trader-Application module

By following these steps you will be able to run the Stock Simulation with the view and see how the traders interact with the exchange.

<!--
Describe how to run your program here. These should be a few very simple steps.
-->

## Modules

- ####command Module
The `command` module contains two interfaces the `Command` interface and the `CommandHandler`. The command interface is used to implement the Command Pattern throughout the project. It defines the contract for all Command classes, so that every Command class must override the `execute()` method. The `CommandHandler` interface defines the contract for the class that handle incoming commands. It takes the commandKey as a parameter and then maps this commandKey onto the corresponding command and executes it. An example of where the `CommandHandler` is used is in the ExchangeServer where we create a `ExchangeServerCommandHandler`.
These two interfaces allow for a modular and extensible design. Allowing us to easily add new Commands and handle these commands accordingly using the CommandHandlers. As well, these interfaces work well together with the factory pattern to create new commands. 


- ####market Module
The `market` Module contains all the logic for the trader and the exchange. This logic is network independent as we created this module to act as a library, that can be used whenever you want to implement a stock exchange simulation, regardless of the implementations you use. 
In this module we have all our fundamental classes, like the `Stock`, `Trader`, `Transaction`, `Ask`, `Bid` and `Exchange` class. The Exchange class has the logic for resolving bids and asks that are placed by traders. As well, it validates trades and ensures that the traders who are placing bids or asks are placing valid bids and asks. 
For example, it validates that a trader does not create a bid (Sell order) for a stock it doesn't own. The market module defines the central logic for the interaction of the exchange with the traders.  


- ####message-queue Module
The `message-queue` module is a library like module that we use in the networking module. The `message-queue` module has a variety of queues that can be used to store messages. The messages it stores are commands that are sent across the network by the client. 
A message queue consumer, in our case the server, then constantly polls the message queue to see what messages it has received. 

- ####network-market Module
The `network-market` Module takes our `market` module and adds networking features by it. The Exchange because and ExchangeServer and the Trader becomes a TraderClient. As this uses networking the ExchangeServer class and the TraderClient class contain networking method like `start()` and `terminate()`. 
As well, in this module we have the commandHandlers for the `ExchangeSever` and for the `TraderClient`. These command Handlers are central to the functionality of the program. Commands by the ExchangeServer and the TraderClient are sent across the network as strings containing a header and a body. The command handlers turn these strings into the commands and execute them accordingly.
To implement these commands we use the Command Pattern. As well to create the commands, such as `PlaceAskCommand` and `PlaceBidCommand` we use the factory pattern. Overall, the `network-market` module is used to apply trader-exchange communication over the network through the use of commands while using the `market` module to handle the logic of trading stocks.

- ####networking Module
The `networking` Module  

- ####stock-application Module
The `stock-application` Module is the module where the Exchange hosted on the Server is initialised by running the `public static void main`. When the `public static void main` is run it instantiates the view using the appropriate classes from the `stock-market-ui` module. To initialise the ExchangeServer the data from the `stocks.yaml` and `traders.yaml` files is needed.
We need this data as this is where we convert the input data (`stocks.yaml` and `traders.yaml` data) into the data that fits our Exchange model (More on this in the evaluation). 

The `convertmarket` package contains the classes: `MakeStocks` and `MakeTraders` which convert the input data into the data that fits our model. This Module also contains the `updating` package. 
This package contains the `updateAll` class which contains a `ScheduledExecutorService` that executes the `update` method in the `UpdateStocks` and `UpdateTraders` classes every 2 seconds. These update methods will try to update the data used by the view in the `stock-market-ui` module every 2 seconds. By doing this we ensure that the View updates in real time with the updated stock and traders data. 


- ####stock-market-ui Module
The `stock-market-ui` Module contains all the classes that are required for the User Interface to display the data of the Stocks that are in the Exchange and the information about all traders. The view continuously updates as there are changes to the Stock's their data and to the Trader's their data. 
In the `model` package we also have the classes that are used by the `YamlLoader` class to load in the data that is stored in the `stocks.yaml` and `traders.yaml` files. These classes implement the relevant model interfaces like the `StockDataModel` and `TraderDataModel` interfaces which define a contract of methods that the `StockData` and `TraderData` classes must implement. 
These methods defined in the interfaces allow the tables in the view to continuously get the new updated data and display this new data. 

- ####trader-application Module
The `trader-application` module is the module where the traders that connect to the Exchange are created in and send trades to the Exchange in. The traders are initialised when the `public static void main` is ran. In this module we have a `TraderBot` class. This class represents the bots that place orders onto the Exchange. 
The orders that are placed on the exchange are randomly generated by the `RandomOrderGenerator` class. The `RandomOrderGenerator` class has method with randomly produces either an ask or a bid and places it over the network, using its `TraderClient` field. The `TraderBot` calls the `RandomOrderGenerator` every 2 seconds to place an order by using a 
`ScheduledExecuterService` that runs the `placeOrder` method every 2 seconds. 


- ####util Module

The Util module is a module that contains the utility class `YamlLoader`. 
The `YamlLoader` class allows us to load in the data that is stored in the `stocks.yaml` and `traders.yaml` files into the classes that require this data.
We do this by creating an instance of the `YamlLoader` class with the appropriate file that has to be loaded as an argument: 
```Java
YamlLoader loader = new YamlLoader(Path.of("data/stocks.yaml"))
```
After creating this instance of the call we call the `load` method to load in the data stored in the `.yaml` file. 
We use this module in the `stock-application` and `trader-application` module to load in the stock data and construct the Stock Exchange and to construct the traders. 
<!--

    
Describe each module in the project, what their purpose is and how they are used in your program. Try to aim for at least 100 words per module.
-->

## Design

<!--
List all the design patterns you used in your program. For every pattern, describe the following:
- Where it is used in your application.
- What benefit it provides in your application. Try to be specific here. For example, don't just mention a pattern improves maintainability, but explain in what way it does so.
-->

## Evaluation

###Stability of Implementation
Our implementation of the Stock Market Simulation has a high degree of stability. When running both the `stock-application` and the `trader-application`
there will be no crashes. The core functionality of the project, including the client-server network communication, sending buy and sell orders, handling the commands (both on the client and server side), and the live updating view of the stock trading simulation, all operate without any critical bugs. 

###Successful Aspects
- ####Design and Modular implementation
We focused a lot on having a modular and de-coupled design planned out, by drawing a diagram, before we started writing out our code. This made the implementation process a lot easier for most parts of the project. By implementing a modular design we broke the project down into smaller, more manageable, sub problems such as: the exchange logic, the networking between the exchange and the traders, the loading of the data and the updating of the Simulation UI, as examples.
The modular design also meant we had reusable components reducing the code duplication in our project. This also made bug fixing and changes easier to manage as fixing a problem in one class meant this fix automatically propagated to all places this module/class was used. Overall, the modular design improves the maintainability of project.
By having modules that focused on specific parts of the project we could test these modules in isolation and ensure that the module works as intended. Then, to see if our modules worked well together we wrote some integration tests. We focused a lot on testing the logic of the `ExchangeSever` and the `TraderClient` as these are the central parts of the project, testing edge cases trying to get full test coverage. 

### Improvements 
- ####Model and Input Data
Even though we planned ahead and drew a diagram to help us with our implementation we did not look at how the data (`stocks.yaml` and `traders.yaml`) that was given to us was formatted. This meant we created a market module, that handles all the logic of our project: handling bids and asks, creating transactions, updating traders, creating stocks etc... without the input data in mind. 
This meant we had a model, the market module, that was fully functional in testing, but didn't match the input data. This lead to us needing to either fully change our market module, and the logic that goes with it, or convert the input data into data that fit our model. We choose the latter. 
Although this works, this is not an ideal implementation. If we could re-do the project we would keep in mind the input data when creating the logic and model that use the input data. 

- #### Serialisation
To send commands over the network the commands must be changed into strings. By an oversight we do this with two different methods, standard java serialisation and Json. This is not ideal. An improvement would be to only use Json so that converting objects into strings is uniform throughout the project. 
Because our `ExchagneServer` and `TraderClient` both require the `MarketSerializer` class they are currently in the same module. Ideally, if we only used Json for converting objects into strings to send across the network, we would be able to seperate the `ExchangeServer` and `TraderClient` into two seperate modules. The `TraderClient` would be in the `trader-application` module, seperate from the `ExchangeServer`. 


<!--
Discuss the stability of your implementation. What works well? Are there any bugs? Is everything tested properly? Are there still features that have not been implemented? Also, if you had the time, what improvements would you make to your implementation? Are there things which you would have done completely differently? Try to aim for at least 250 words.
-->

## Extras

<!--
If you implemented any extras, you can list/mention them here.
-->

___


<!-- Below you can find some sections that you would normally put in a README, but we decided to leave out (either because it is not very relevant, or because it is covered by one of the added sections) -->

<!-- ## Usage -->
<!-- Use this space to show useful examples of how a project can be used. Additional screenshots, code examples and demos work well in this space. You may also link to more resources. -->

<!-- ## Roadmap -->
<!-- Use this space to show your plans for future additions -->

<!-- ## Contributing -->
<!-- You can use this section to indicate how people can contribute to the project -->

<!-- ## License -->
<!-- You can add here whether the project is distributed under any license -->


<!-- ## Contact -->
<!-- If you want to provide some contact details, this is the place to do it -->

<!-- ## Acknowledgements  -->

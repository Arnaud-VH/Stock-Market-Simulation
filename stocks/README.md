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
The command module holds the Command interface and the Command Handler interface. 

Complete this with more information. 

- ####market Module

- ####message-queue Module

- ####network-market Module

- ####networking Module

- ####stock-application Module

- ####stock-market-ui Module

- ####trader-application Module

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

# JDemetra+ plugins tutorial

This project provides examples of plugins for [JDemetra+](https://github.com/jdemetra/jdemetra-app).

## Getting started

1. Download the latest source code
2. Build it: `mvn clean install -DskipTests=true`
3. Go to the tutorial application: `cd tutorial-app`
3. Run the tutorial to see it in action: `mvn nbm:run-platform`

## Technologies involved

* [JDK 7](http://www.oracle.com/technetwork/java/javase/overview/index.html)
* [Maven](https://maven.apache.org/) 
* [Netbeans platform](https://netbeans.org/features/platform/)

## Structure

### tutorial-app
A simple application that allows to test the plugins of the tutorial.
### tutorial-plugin-dstat
A plugin that generates some descriptive statistics of time series by using a context menu in the providers window.
### tutorial-plugin-random
A plugin that adds a new time series provider. This provider generates pseudo-random time series using Arima.

### tutorial-plugin-various
A plugin that adds several features to JDemetra+ such as:

* A custom report in X13 seasonal ajustment
* A basic color scheme
* A new action in the context menu of the providers window
* An improved dialog for adding spreadsheets as data sources

### tutorial-plugin-basic-output
A plugin that adds two simple text outputs to JDemetra+:

* A single report for all the series
* A text output for each series

### tutorial-plugin-output
A plugin that adds two advanced outputs to JDemetra+:

* A csv file containing various seasonality tests
* An xml file that provides some results of SEATS (canonical decomposition...)

### QualityReport
A plugin that creates a skeleton for the SA quality report of JDemetra+:



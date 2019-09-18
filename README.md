# Testing Framework For Testing Salesforce CRM 

This is a framework template for end-to-end testing of salesforce platform built with Java, Selenium and Apache HTTP Client. This is full-fledged data-driven framework with sample tests for Web, API and Integration tests. 

# Pre-Requisites For Using The Framework
* The user should have a Salesforce Dev account and have credentials to access the Salesforce lightening platform and API's.

# Framework Features
* This a framework created with page-object-model
* Sample tests cases are located in the path src/test/java. We have sample tests for Web, API and Web and API Integrated tests.
* Test cases extend a common BaseTest class which has methods to make HTTP Requests, Getting Access Tokens and Diver Initialization.
* All the Environment Properties are read from src/main/resources/manifest.json, it supports having parameters for multiple environments.
* API test data(end point, request, headers etc) comes from an excel file from src/main/resources/salesforce_dev_api.xlsx, which increases the maintainability.
* Easy to add test cases: framework implements data providers to read data from excel and JSON files, add test cases in excel and use out of the box methods to create TestNG test cases. 
* Maven is used for building the project.
* Has a configurable out of the box logger with config details in manifest.json file.
* Creates a JSON report and Maven extent report along with the TestNG HTML report 

    Note:
    Need to add IP into Trusted IPs in login.salesforce.com, Security Controls -> Network Access
    Reference: https://salesforce.stackexchange.com/questions/120531/is-there-any-way-around-verification-code-i-am-using-selenium-and-java-to-auto/127259#127259

## Code of Conduct


## Copyright

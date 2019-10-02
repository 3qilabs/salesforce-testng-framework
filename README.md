# Framework For Testing Salesforce CRM 
* Now a days most companies either small of small size or large size are using Salesforce CRM. This is an open source framework template for end-to-end testing of Salesforce platform which makes automation easy. This is full-fledged data-driven framework with sample tests for Web, API and Integration (Web and API) tests. 

# Pre-Requisites For Using The Framework
* The user should have a Salesforce Dev account and have credentials to access the Salesforce lightening platform and API's.

# Libraries/Tools
* Maven
* Selenium WebDriver
* TestNG
* REST Assured
* Apache HTTP Client
* Apache POI
* Log4j

# Framework Features
* This a framework created with page-object-model design pattern.
* Sample tests cases are located in the path src/test/java. We have sample tests for Web, API and Integrated (Web and API) tests.
* API Test cases extend a BaseClass class which has methods to make HTTP Requests, Getting Access Tokens.
* Web and Integrated (Web and API) test cases extend a BaseUiClass which has methods for Diver Initialization and also extends a BaseClass.
* All the Environment Properties are read from resources/env1/env.properties, it supports having parameters for multiple environments.
* API test data(end point, request, headers etc) comes from an excel file from resources/salesforce_dev_api.xlsx, which increases the maintainability.
* Easy to add test cases: framework implements data providers to read data from excel and JSON files, add test cases in excel and use out of the box methods to create TestNG test cases. 
* Has a Java Bean class to get data for API tests, which has a @Randomizer function to generate random value based on specified fields
* Maven is used for building the project.
* Has a configurable out of the box logger with config details in log4j.properties file.
* Creates a JSON report and Maven extent report along with the TestNG HTML report 

    Note:
    Need to add IP into Trusted IPs in login.salesforce.com, Security Controls -> Network Access
    Reference: https://salesforce.stackexchange.com/questions/120531/is-there-any-way-around-verification-code-i-am-using-selenium-and-java-to-auto/127259#127259

## Code of Conduct


## Copyright

# Framework For Testing Salesforce CRM 
* Now a days most companies either of small size or large size are using Salesforce CRM. This is an open source framework template for end-to-end testing of Salesforce platform which makes automation easy. This is full-fledged data-driven framework with sample tests for Web, API and Integration (Web and API) tests. 

# Pre-Requisites For Running the Sample Tests in the Framework
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
* Sample tests cases are located in the path src/test/java/testsuits. We have sample tests for Web, API and Integrated (Web and API) tests.
* The framework has out of the box methods to make API calls using three different libraries: RestAssured, Apache HttpClient, HttpURLConnection. These methods are part of the BaseClass which can be extended in the API test cases. These methods also take care of Authentication by getting the access token using the credentials from env1.properties (or any other property file inside the env-resources-directory selected in application.properties with the key:env.resources).
* Framework also has data beans as another data source for tests which can be created using an annotation which fills random data, data from SQL queries(Has a built in MYSQL DB connection) or from property files in the resources directory. Multiple resource directories can be added for different environments and a particular directory can be selected by mentioning environment in application.properties. 
* All the Environment Properties are read from resources/env1/env.properties, it supports having parameters for multiple environments with switch in application.properties.
* The framework has a Data Provider which reads data(end point, request, headers etc) from excel in the location resources/salesforce_dev_api.xlsx which increases the maintainability.  
* Web and Integrated (Web and API) test cases extend a BaseUiClass which has methods for Diver Initialization and also extends the BaseClass.
* Easy to add test cases: Framework provides many important Utility methods and data provider to read data from multiple sources.
* Maven is used for building the project.
* Has a configurable out of the box logger with config details in log4j.properties file.
* Creates a JSON report and Maven extent report along with the TestNG HTML report 

    Note:
    After creating Salesforce Dev portal account, if "Enforce IP Restrictions" is set under connected app settings, we need to add IP into Trusted IPs in login.salesforce.com, Security Controls -> Network Access
    Reference: https://salesforce.stackexchange.com/questions/120531/is-there-any-way-around-verification-code-i-am-using-selenium-and-java-to-auto/127259#127259

## Code of Conduct


## Copyright

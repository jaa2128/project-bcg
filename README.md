# U-Fund:  _____ _replace with your particular fundraising type_ _____
# Modify this document to expand any and all sections that are applicable for a better understanding from your users/testers/collaborators (remove this comment and other instructions areas for your FINAL release)

An online U-Fund system built in Java 17=> and ___ _replace with other platform requirements_ ___
  
## Team

- Shaher Naser
- Matt Zobbi
- Julian Alvia
- Brandon Santore
- Alexander DiMartino


## Prerequisites

- Java 11=>17 (Make sure to have correct JAVA_HOME setup in your environment)
- Maven
-  _add any other tech stack requirements_


## How to run it

1. Clone the repository and go to the root directory.
2. Execute `mvn compile exec:java`
3. Open in your browser `http://localhost:8080/`
4.  _add any other steps required or examples of how to use/run_

**List of sample cURL commands for testing**

curl.exe -i -X POST -H 'Content-Type:application/json' 'http://localhost:8080/needs' -d '{\"name\": \"Help us save the world\", \"description\": \"Test Description 1\", \"type\": \"Test Type 1\",\"targetQuantity\": 1.0}'

curl.exe -i -X POST -H 'Content-Type:application/json' 'http://localhost:8080/needs' -d '{\"name\": \"Help us fund event XYZ\", \"description\": \"Test Description 2\", \"type\": \"Test Type 2\",\"targetQuantity\": 2.0}'

curl.exe -i -X POST -H 'Content-Type:application/json' 'http://localhost:8080/needs' -d '{\"name\": \"Help us save the world\", \"description\": \"Test Description 1\", \"type\": \"Test Type 1\",\"targetQuantity\": 1.0}'

curl.exe -i -X GET 'http://localhost:8080/needs/1'

curl.exe -i -X GET 'http://localhost:8080/needs'

curl.exe -i -X GET 'http://localhost:8080/needs/999999'

curl.exe -i -X PUT -H 'Content-Type:application/json' 'http://localhost:8080/needs' -d '{\"id\": 1, \"name\": \"Help us save the world, again\", \"description\": \"testdescription\", \"type\": \"testtype\",\"targetQuantity\": 100.0}'

curl.exe -i -X GET 'http://localhost:8080/needs'

curl.exe -i -X PUT -H 'Content-Type:application/json' 'http://localhost:8080/needs' -d '{\"id\": 1, \"name\": \"Help us fund event XYZ\", \"description\": \"testdescription\", \"type\": \"testtype\",\"targetQuantity\": 100.0}'

curl.exe -i -X GET 'http://localhost:8080/needs/?name=save'

curl.exe -i -X GET 'http://localhost:8080/needs/?name=testneed'

curl.exe -i -X DELETE 'http://localhost:8080/needs/1'

curl.exe -i -X DELETE 'http://localhost:8080/needs/2'

curl.exe -i -X GET 'http://localhost:8080/needs'

curl.exe -i -X DELETE 'http://localhost:8080/needs/999999'

curl.exe -i -X GET 'http://localhost:8080/needs'

curl.exe -i -X PUT -H "Content-Type: application/json" -d "100.0" "http://localhost:8080/needs/1"

curl.exe -i -X PUT -H "Content-Type: application/json" -d "-100.0" "http://localhost:8080/needs/1"

curl.exe -i -X PUT -H "Content-Type: application/json" -d "100.0" "http://localhost:8080/needs/99999"

## Known bugs and disclaimers
(It may be the case that your implementation is not perfect.)

Document any known bug or nuisance.
If any shortcomings, make clear what these are and where they are located.

1. Case Sensitivity with Searching Needs: When using the cURL GET command to find needs that match a specific search query, the results were case-sensitive. This was located in NeedFileDAO.java during v1.0 but was shortly fixed in v1.1.
2. Updating Need with Same Name: When using the cURL PUT command to update an existing need without changing its name, the command fails since the program thinks that the new need will cause a naming conflict. This was located in NeedController.java during v1.0 but was shortly fixed in v1.1.
3. Creating Needs with Blank Fields: When using the cURL POST command to create a new need, the command succeeds even if one of the fields was omitted. For string fields, they could also be set to null or the empty string, and the targetQuantity could be set to a nonpositive number. This was located in NeedController.java during v1.0 but was shortly fixed in v1.1.

## How to test it

The Maven build script provides hooks for run unit tests and generate code coverage
reports in HTML.

To run tests on all tiers together do this:

1. Execute `mvn clean test jacoco:report`
2. Open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/index.html`

To run tests on a single tier do this:

1. Execute `mvn clean test-compile surefire:test@tier jacoco:report@tier` where `tier` is one of `controller`, `model`, `persistence`
2. Open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/{controller, model, persistence}/index.html`

To run tests on all the tiers in isolation do this:

1. Execute `mvn exec:exec@tests-and-coverage`
2. To view the Controller tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`
3. To view the Model tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`
4. To view the Persistence tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`

*(Consider using `mvn clean verify` to attest you have reached the target threshold for coverage)
  
  
## How to generate the Design documentation PDF

1. Access the `PROJECT_DOCS_HOME/` directory
2. Execute `mvn exec:exec@docs`
3. The generated PDF will be in `PROJECT_DOCS_HOME/` directory


## How to setup/run/test program 
1. Tester, first obtain the Acceptance Test plan
2. IP address of target machine running the app
3. Execute ________
4. ...
5. ...

## License

MIT License

See LICENSE for details.

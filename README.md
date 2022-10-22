# points
### Setup local dev
*Install OpenJDK 8+
*Install IntelliJ Community
*Install Git
*Install Postman

### start project
*clone the git
*open the project with IntelliJ
*run DemoApplication on the top right corner

### test project
*run Postman
*set up a get request with url http://localhost:8080/balance and send it. The return value should be an empty list since there is no value in the database
*set up a Post request with url http://localhost:8080/transaction and three params: payer, points and timestamp (the timestamp should in format of Timestamp class in java)
*send the get request with url http://localhost:8080/balance and send it, you will say the new transaction have already be wrote down.
*repeat the transaction post request as much as you want
*set up a Post request with url http://localhost:8080/spend and one params: points, which is the points you want to spend.
*repeat the balance get request and say the result

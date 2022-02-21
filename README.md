# time-off-module
Time off module with Oauth 2.0 
The following implementation provides a time off module, 
in which time offs and time off requests are handled. By using 
Oauth 2.0, security is incorporated.

-------------------------------How to make it run-------------------------------------------

Tools you'll need to run and test the container:
Docker
Docker Desktop
Postman
Additionally:
Spring tool suite
Postgres and PgAdmin

1. Download the project

2. Run as an administrator the following commands:
netsh int ip add addr 1 10.5.0.4/32 st=ac sk=tr
netsh int ip add addr 1 10.5.0.5/32 st=ac sk=tr
netsh int ip add addr 1 10.5.0.6/32 st=ac sk=tr
netsh int ip add addr 1 10.5.0.7/32 st=ac sk=tr

3. Then in the folder where the downloaded project is, open a command
propmt and run "docker compose up"

4. Verify the container is running in Docker Desktop

5. Enter to http://localhost:5050 to get access to pgadmin,
then create a new server if there is not one, with 
hostname: app-db,
username: itk,
password: itk2022

6. Then, create the two databases needed:
1. authorization-db, and
2. time-off-db

7. Consequently, add the test data from the sql files by using
the query tool. Run data from:
"create-tables-security.sql" for "authorization-db", and
"create-time-off-tables.sql" for "time-off-db"

8. Check if all images are running, if not, run the missing ones.

9. Go to http://localhost:8080 to login as:
A USER: julio.vargas@theksquaregroup.com, Password: user
A MANAGER: guillermo.ceme@theksquaregroup.com, Password: manager
AN ADMIN: carlos.reyes@theksquaregroup.com, Password: admin

10. To test the endpoints, go to postman and login.
Choose the POST method and enter:
http://10.5.0.6:9000/login?username=username&password=password,
where username is one of the emails given.
Example:
http://10.5.0.6:9000/login?username=julio.vargas@theksquaregroup.com&password=use

11. Now test the endpoints, starting with "http://10.5.0.5:8080/endpoint":

---TIME OFFS---
GET /timeOffs
GET /timeOffs/{id}
POST /timeOffs
PATCH /timeOffs/{id}

---TIME OFF REQUESTS---
GET /managers/{managerID}/timeOffRequests
GET /managers/{managerID}/employees/{employeeID}/timeOffRequests/{timeOffRequestID}
DELETE /employees/{employeeID}/timeOffRequests/{timeOffRequestID
PATCH /managers/{managerID}/employees/{employeeID}/timeOffRequests/{timeOffRequestID}
POST /managers/{managerID}/employees/{employeeID}/timeOffRequests
GET /employees/{employeeID}/availableTimes

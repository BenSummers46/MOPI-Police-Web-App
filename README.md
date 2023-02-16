# Important instructions for use

## Requirements
- Ensure that you have Docker installed on your system.   
- Ensure that you have resolved Maven dependencies in the project.

## Steps for use
1. Within the project folder, open the Windows Powershell or any alternative console.
2. Within the console write the command: 
    >docker-compose up 
                                             
    ***Note: please leave the console open***  
3. Open the project in IntelliJ and navigate to the path: 
    >src/test/java/com.team05.codebotiics.mopi_webapp   

    And run the tests to populate the database:
    >PoliceRepositoryTests.testCreateUser
    >
    >PoliceRepositoryTests.testCreateSuspect
    > 
    >PoliceRepositoryTests.testAddLicenses.                                                                                                                                                                                             

4. Once that has ran navigate to the path:
   >/src/main/java/com.team05.codebotiics.mopi_webapp
                                              
   And run the class:
   >MopiWebappApplication   
  
5. Open in Chrome or any other compatible browser and enter:
    >localhost:8080
                          
   ***NOTE:*** **Known compatibility issues with Firefox and Safari. datetime-local
   input fields are not supported in these browsers!**
                                                 
6. On the login page please log in with the credentials:
    >Badge Number: 2
    >
    >Password: Password2@
                                                            
    This will give you access to a "**supervisor**" account which has access
    to all aspects of the program.
    
    Manager and User accounts **DO NOT** have access to police registration functionality.
    
## Database login credentials within the IntelliJ database viewer

>Name: mopi@localhost
>
>Host: localhost
>
>Port: 33333
>
>User: user
>
>Password: password
>
>Database: mopi
>
>URL: jdbc:mysql://localhost:33333/mopi
                                                 
## Steps after use
1. After finishing with the project please close the tab and stop the application
from within the IDE.

2. Open up the console from before and press:
    >CTRL + C
                                                 
    And then type the command:
    >docker-compose down
    >                                                                                                                                           
    >docker-compose down --rmi all
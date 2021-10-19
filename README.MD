## rectangle-api
A Spring Boot based Rest Api for creating and managing rectangles and performing operations such as finding intersection, 
containment and adjacency.

### Libraries
MySQL db, Lombok and Mapstruct for boilerplate code, Hibernate validation for api's, Jupiter for unit testing, 
rest-assured and hamcrest for api testing

### Deployment
EC2 and MySQL RDS instances available from AWS Beanstalk

### Endpoints
Through this api it's possible to perform a full CRUD over Rectangle objects which are defined by both their bottom left 
and top right coordinates (x, y) in a cartesian plane where both x, y > 0,0

GET     /api/rectangle                  -   fetches all existing rectangles in the db
GET     /api/rectangle/{id}                 
POST    /api/rectangle                  -   {
                                                "bottomLeftX": 2,
                                                "bottomLeftY": 2,
                                                "topRightX": 5,
                                                "topRightY": 4
                                            }          
PUT     /api/rectangle                  -   {
                                                "id": 1,
                                                "bottomLeftX": 2,
                                                "bottomLeftY": 2,
                                                "topRightX": 5,
                                                "topRightY": 4
                                            }  
DELETE  /api/rectangle/{id}

it's also possible to calculate the following operations providing the respective id's of two existing Rectangle objects  

GET    /api/rectangle/intersection/id?=26,27   
GET    /api/rectangle/containment/id?=26,27   
GET    /api/rectangle/adjacency/id?=26,27

for a faster experience with these operations both Rectangle's coordinates can also be directly sent through the following 
api's

POST    /api/rectangle/intersection     -   [{
                                                "bottomLeftX": 1,
                                                "bottomLeftY": 1,
                                                "topRightX": 5,
                                                "topRightY": 3
                                            },
                                            {
                                                "bottomLeftX": 2,
                                                "bottomLeftY": 0,
                                                "topRightX": 4,
                                                "topRightY": 4
                                            }]
POST    /api/rectangle/containment   
POST    /api/rectangle/adjacency

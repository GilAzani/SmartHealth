# SmartHealth Super App

## 1. Introduction
“SmartHealth” is a super app that connects professional dietitians with potential clients. It offers two mini-applications to cater to different user needs: "DietitiansHelper" and "Healthy."

### 1.1. Purpose of System
The purpose of “SmartHealth” is to help people improve their overall health and support dietitians in delivering professional services. The system provides access to nutritional information about various ingredients, allowing people to make informed choices. It also enables dietitians to create customized recipes based on individual needs and preferences.

### 1.2. Scope of System
The "SmartHealth" system primarily functions as a back-end application that provides infrastructure for front-end applications such as "Healthy" and "DietitiansHelper." The system uses Spoonacular API to provide nutritional information and functionality to support dietitians in their professional services. It also utilizes MongoDB as a database technology and provides efficient storage using pagination.

## 2. Actors and Goals
**Primary actors:**
- Dietitians: Can build recipes.
- Customers: Can subscribe to dietitians to view their creations.

**Support actors:**
- Time: Required for proper documentation and calorie tracking.
- Spoonacular API: Provides data for each ingredient.
- MongoDB: Database technology for storing data.

## 3. Functional Requirements
### User Functions:
- User (Dietitian/Customer) can create an account with username, email, password, and avatar.
- User (Dietitian/Customer) can perform authentication.
- User (Dietitian/Customer) can edit their data, including avatar and username.

### Dietitian Functions:
- Dietitian can create, delete, modify, and get recipes.
- Dietitian can search recipes by recipe name, ingredient, or specific diet.

### Customer Functions:
- Customer can follow a dietitian.
- Customer can unfollow a dietitian.
- Customer can set their health diets (e.g., Gluten Free, Vegan, Ketogenic, Vegetarian).
- Customer can track their daily food intake using a food diary and calorie counter.
- Customer can provide feedback and ratings through a rating system and testimonials.

### Admin Functions:
- Admin has access to regular administrative tasks.

## 3.1. Use Case Diagram
### Use Case: Login
Actor: User, MongoDB
1. The user requests to login to the system.
2. The system opens the login screen.
3. The user enters an email.
4. The system validates credentials and checks if a user exists in MongoDB under “USERS”.
5. The user grants access to the system.
6. The system opens the main application screen.

Alternative Flow A - User doesn’t exist
At step 4, if the user doesn’t exist, the system notifies the user.
The user enters a correct email.

### Use Case: Create User
Actor: User, MongoDB
1. The user requests to create a user.
2. The system opens the “create user” screen.
3. The user enters their credentials and clicks on the create user button.
4. The system validates credentials and checks if a user exists in MongoDB under “USERS”.
5. The system saves the user data in MongoDB under “USERS”.
6. The system informs the user that they have successfully entered the system.
7. The user grants access to the system.
8. The system opens the main application screen.

Alternative Flow A - User already exists
At step 4, if the user already exists, the system notifies the user.
The user enters a correct email.

Alternative Flow B - Wrong inputs
After step 6, the system detects that the user misfiled the credentials and notifies the user.
The user refills the needed values and resends the form.

### Use Case: Update User
Actor: User, MongoDB
1. The user requests to update their details.
2. The system opens the “update user” screen.
3. The user fills in the wanted fields to update.
4. The system modifies only the fields that can be edited.
5. The system saves the modified user data.

Alternative Flow A - Wrong inputs
After step 4, the system detects that the user misfiled the credentials and notifies the user.
The user refills the needed values and resends the form.

## Miniapps:
### Use Case: Create Recipe
Actor: Dietitian (user), MongoDB, Spoonacular API
1. The user requests to create a recipe.
2. The system opens the “create recipe” screen.
3. The system validates permissions.
4. The user enters a recipe name and selects ingredients for the recipe from Spoonacular API, with the option to add a 1. photo.
5. The system validates the command request.
6. The system creates the recipe based on the ingredients list.
7. The system updates the changes in MongoDB under “SUPER_APP_OBJECTS” and creates a command.
8. The system presents the new recipe.

Alternative Flow A - User doesn’t have permissions
At step 3, if the user doesn’t have permissions, the system notifies the user.
The user exits the “create recipe” screen.

Alternative Flow B - Recipe name already exists/empty
At step 4, if the recipe name already exists or is empty, the system notifies the user.
The user enters a new recipe name.

Alternative Flow C - Command doesn’t exist
At step 5, if the command name isn’t “CREATE_RECIPE”, the system notifies the user.
The user enters a correct command.

### Use Case: Find Recipe
Actor: Dietitian (user), MongoDB, Spoonacular API
1. The user requests to find a recipe.
2. The system opens the “find recipe” screen.
3. The system validates permissions.
4. The user provides a recipe name, ingredient, or specific diet to search for recipes.
5. The system validates the command request.
6. The system retrieves the recipe from Spoonacular API and adds it to the recipes list.
7. The system updates the changes in MongoDB under “SUPER_APP_OBJECTS” and creates a command.
8. The system presents the recipe.

Alternative Flow A - User doesn’t have permissions
At step 3, if the user doesn’t have permissions, the system notifies the user.
The user exits the “find recipe” screen.

Alternative Flow B - Command doesn’t exist
At step 5, if the command name isn’t “FIND_RECIPE”, the system notifies the user.
The user enters a correct command.

### Use Case: Modify Recipe
Actor: Dietitian (user), MongoDB
1. The user requests to modify a recipe.
2. The system opens the “modify recipe” screen.
3. The system validates permissions.
4. The user enters the name of the recipe they wish to modify.
5. The system validates the command request.
6. The system checks if the recipe exists and displays it.
7. The user fills in the wanted fields to update.
8. The system updates the changes in MongoDB under “SUPER_APP_OBJECTS” and creates a command.
9. The system presents the recipe.

Alternative Flow A - User doesn’t have permissions
At step 3, if the user doesn’t have permissions, the system notifies the user.
The user exits the “modify recipe” screen.

Alternative Flow B - Recipe doesn’t exist
At step 5, if the recipe doesn’t exist, the system notifies the user.
The user enters a new recipe name.

Alternative Flow C - Command doesn’t exist
At step 5, if the command name isn’t “MODIFY_RECIPE”, the system notifies the user.
The user enters a correct command.

### Use Case: Delete Recipe
Actor: Dietitian (user), MongoDB
1. The user requests to delete a recipe.
2. The system opens the “delete recipe” screen.
3. The system validates permissions.
4. The user enters the name of the recipe they wish to delete.
5. The system validates the command request.
6. The system checks if the recipe exists and deletes it.
7. The system updates the changes in MongoDB under “SUPER_APP_OBJECTS” and creates a command.

Alternative Flow A - User doesn’t have permissions
At step 3, if the user doesn’t have permissions, the system notifies the user.
The user exits the “delete recipe” screen.

Alternative Flow B - Recipe doesn’t exist
At step 5, if the recipe doesn’t exist, the system notifies the user.
The user enters a new recipe name.

Alternative Flow C - Command doesn’t exist
At step 5, if the command name isn’t “DELETE_RECIPE”, the system notifies the user.
The user enters a correct command.

### Use Case: Get Recipe
Actor: User (Dietitian/Customer), MongoDB
1. The user requests to get a recipe.
2. The system opens the “get recipe” screen.
3. The system validates permissions.
4. The user enters the name of the recipe they wish to get.
5. The system validates the command request.
6. The system checks if the recipe exists and displays it.
7. The system creates a command.

Alternative Flow A - User doesn’t have permissions
At step 3, if the user doesn’t have permissions, the system notifies the user.
The user exits the “get recipe” screen.

Alternative Flow B - Command doesn’t exist
At step 5, if the command name isn’t “GET_RECIPE”, the system notifies the user.
The user enters a correct command.

Alternative Flow C - Recipe doesn’t exist
At step 5, if the recipe doesn’t exist, the system notifies the user.
The user enters a new recipe name.

### Use Case: Get Random Recipe
Actor: User (Dietitian), MongoDB, Spoonacular API
1. The user requests to get a random recipe.
2. The system opens the “get random recipe” screen.
3. The system validates permissions.
4. The system validates the command request.
5. The system uses the Spoonacular API to retrieve a random recipe or recipes based on the specified quantity in the command.
6. The system presents the recipes.

Alternative Flow A - User doesn’t have permissions
At step 3, if the user doesn’t have permissions, the system notifies the user.
The user exits the “get random recipe” screen.

Alternative Flow B - Command doesn’t exist
At step 4, if the command name isn’t “GET_RANDOM_RECIPE”, the system notifies the user.
The user enters a correct command.

### Use Case: Follow Dietitian
Actor: User (Dietitian/Customer), MongoDB
1. The user requests to follow a dietitian.
2. The system opens the “follow a dietitian” screen.
3. The system adds the user to the list of authorized viewers in the dietitian.
4. The system updates the changes in MongoDB under “SUPER_APP_OBJECTS” and creates a command.


## Tech Doc
The following is a list of tools and frameworks that were utilized during the project:

### Frameworks and Environments:
- Java Spring Framework
- Spring Tools
- IntelliJ IDEA

### Database:
- MongoDB
- Mongo Express

### Project Management and Teamwork:
- Trello
- Google Meet
- Teams
- Slack
- WhatsApp

### Design:
- Figma
- Canva

### Containerization:
- Docker

### Version Control:
- Bitbucket

### Diagramming:
- Lucidchart
- sequencediagram.com

### Tests:
- Postman

### Client Side:
- React Native
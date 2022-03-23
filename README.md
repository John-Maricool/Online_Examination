# Online Examination
## Introduction
This application is an Online Examination Native android application built for Teachers and Students. The applications are two one for the admin (Teacher) and the other for 
the Student. The Admin app enables the Admin to View Students registered for an Exam, add exam questions to local database and upload to Cloud for the registered students to
access. The admin is also able to unregister a registered student and activate a Student (Make the student eligible to take test). 

The Student application is easy. The student is able to sign in/up, register for test using the Admin id, take test and view his/her result immediately. The Student is also
able to add the quiz results to local database. 

## Technologies and Architecture
### Technologies
Android, Kotlin, Firebase API, Modular Programming
### Architecture
Model-View-ViewModel (MVVM)
### Backend Features
- Firebase Authentication
- Firebase Cloud Firestore
- Firebase Storage. 
### Android Architecture Components
 - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
 - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
 - [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
 - [View Binding](https://developer.android.com/topic/libraries/view-binding)
 - [Navigation Components](https://developer.android.com/guide/navigation/navigation-getting-started)
 - [Room](https://developer.android.com/jetpack/androidx/releases/room)
 ## Features For Admin
#### Start: Login/create account.
#### Registered Students: Able to view Registered students list, profile, activate, deactivate and unregister studennt as the case may be.
#### Create Test: Able to create a new Test, with questions, options, and correct answer. 
#### Modify test: Able to modify (change) the test details.
#### Upload Quiz: Able to upload quiz to database for students to access with time. 
#### Profile: Update and change user profile details.
#### Call: Able to call a student.
 ## Features For Student
#### Start: Login/create account.
#### Register: Able to register for a quiz with the Admin ID. 
#### Test: Able to take Online examination.
#### Results: Able to view Results immediately after the quiz
#### Save Results: Able to save result to local database to keep track of them. 
#### Profile: Update and change user profile details.
### Requirements
Basic Knowledge of Android Studio, Kotlin and Firebase
### Project
- Download and open the project in Android Studio
- Connect your Android phone or use the emulator to start the application
## Screeshots

### Profile
<img src="https://user-images.githubusercontent.com/60844538/159584379-4a240b44-f8b3-4ffd-a8d4-875ced939884.png" width="250">    <img src="https://user-images.githubusercontent.com/60844538/159584537-a983bd6b-91c4-4ae3-a37a-d1a8b4166636.png" width="250"> 
         <img src="https://user-images.githubusercontent.com/60844538/159584566-40c4e55c-4a81-42a7-97e2-733e1914a0e9.png" width="250">

### Quiz (Student)

<img src="https://user-images.githubusercontent.com/60844538/159584957-ffb30446-ed32-4c96-9e04-e309fff5a86e.png" width="250"> <img src="https://user-images.githubusercontent.com/60844538/159584971-635118db-33ec-4a3f-9ec8-863520b19b13.png" width="250">  <img src="https://user-images.githubusercontent.com/60844538/159585748-15d23cee-a1de-401a-b0f9-485a546817a8.png" width="250">

### Registered User (Admin
<img src="https://user-images.githubusercontent.com/60844538/159586007-85b6fe0f-bd2b-48d3-84ff-f8942a4f9c88.png" width="250"> <img src="https://user-images.githubusercontent.com/60844538/159586034-bc68ed50-7c00-4a22-9c6f-898ede86f809.png" width="250">
<img src="https://user-images.githubusercontent.com/60844538/159586201-d385f8ca-6c1e-4a9a-a0c5-91f92136a073.png" width="250">

### Create test and quiz list (Admin)

<img src="https://user-images.githubusercontent.com/60844538/159586570-b2c9347c-fc0b-4859-959c-edf19237f281.png" width="250"> <img src="https://user-images.githubusercontent.com/60844538/159586584-8ca0fd88-900a-4d72-ad29-d222c3df8f9c.png" width="250">
<img src="https://user-images.githubusercontent.com/60844538/159586593-484693d8-a381-4d00-a9b0-bb4e058aad67.png" width="250">

### Features not added (Reccommendations)
#### Cloud Messaging: Ablilty of Admin to send push notifications to all the students or to a student.
#### UI: UI is very simple and little design was used. 
#### Image: Ability of Admin to add image as part of the objective questions. 

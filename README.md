# AttendMe - Teacher's Version

AttendMe is an Android application designed to streamline and organize attendance tracking for teachers and students. This app allows teachers to create classes, share unique class codes with students, and manage attendance efficiently. The app emphasizes preventing proxy attendance through dynamic QR code generation.

## Features

- **Teacher Registration & Authentication**: Teachers can sign up and log in using Firebase Authentication.
- **Class Creation**: Teachers can create their classes and receive a unique class code to share with students for easy joining.
- **Attendance Tracking**: Teachers can track students' attendance in an organized manner.
- **Proxy-Free Attendance**: The app generates a dynamic QR code that changes every 5 seconds, making it difficult to forge attendance.
- **QR Code Generation**: Teachers can generate a QR code that students can scan from a website to mark their attendance.

<br/>
<a href="https://youtu.be/HqsKadtjtLI">Youtube Video Link</a>
<br/>

## Screenshots

<div style="display:flex;flex-wrap:wrap;">
  <img src ="https://github.com/user-attachments/assets/5397bde6-8827-40f3-8329-2b2c5a9658d4" width="300px" height="620px"/>
  <img src ="https://github.com/user-attachments/assets/563f1a3f-962f-4415-8e59-9e30021f41be" width="300px" height="620px"/>
  <img src ="https://github.com/user-attachments/assets/ac0aa8d1-dbaf-48d0-a0a6-6819da31ea5e" width="300px" height="620px"/>
  <img src ="https://github.com/user-attachments/assets/dcef5c6d-8a5e-4ca5-bc14-0dbd595bab93" width="300px" height="620px"/>
</div>



## Tech Stack

- **Firebase Authentication**: For teacher registration and login.
- **Firebase Firestore**: For storing class and attendance data.
- **Jetpack Compose**: Used for building the user interface.
- **Dynamic QR Code**: The QR code changes every 5 seconds to prevent proxy attendance.

## Getting Started

### Prerequisites

- Android Studio installed on your development machine.
- A Firebase project set up with Authentication and Firestore enabled.

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/RohitLakra299/AttendMe.git
2. **Open the project in Android Studio.**
3. **Build and run the project on an Android device or emulator.**
  

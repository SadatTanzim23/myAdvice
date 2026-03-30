# <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Telegram-Animated-Emojis/main/Objects/Books.webp" alt="Books" width="40" height="40" /> myAdvice — Student Advising System
### University of Windsor | School of Computer Science
A full-stack Java-based Student Advising System built for the University of Windsor's School of Computer Science. myAdvice is designed to advise undergraduate Computer Science students on aspects of their program while enabling students, faculty, and staff to perform key functions, including course planning, timetabling, booking advising appointments, managing transcripts, and maintaining the system — all through a unified desktop interface.

---

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/Bookmark%20Tabs.png" alt="Bookmark Tabs" width="35" height="35" /> Table of Contents 
<!-- - [Team](#team) -->
- [Project Overview](#project-overview)
- [Tech Stack](#tech-stack)
- [System Architecture](#system-architecture)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Database Setup](#database-setup)
- [Running Locally](#running-locally)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Deployment](#deployment)

---

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Hand%20gestures/Eye.png" alt="Eye" width="35" height="35" /> Project Overview

myAdvice is the pilot implementation of the **Student Advising System (SAS)** for the University of Windsor's School of Computer Science. Developed by **Student Advising on Demand Inc. (SAD)**, the system provides five core modules:

| Module | Description |
|---|---|
| Curriculum Advising | Course planning, prerequisites, and completion tracking |
| Scheduling | Timetable building, section browsing, and room assignments |
| Bookings | Advising appointment booking, cancellation, and rescheduling |
| System Administration | Course management, transcript records, and user profiles |
| Reports & Dashboards | Student/faculty lists, appointment analytics, and enrolment stats |

<!--
---
## Team

| Name | Role |
|---|---|
| Sadat Tanzim | Project Manager & GUI Developer |
| Ali Al Fattouhi | Backend Developer (Spring Boot) |
| Hady Darwiche Figueredo | Backend Developer (Bookings & Scheduling) |
| Ife Adegbite | Database Engineer |
---
-->

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/Hammer%20and%20Wrench.png" alt="Hammer and Wrench" width="35" height="35" /> Tech Stack

### Frontend
- **Java Swing / AWT** — Desktop GUI framework
- **CardLayout** — Multi-panel navigation
- **Java HTTP Client** — REST API communication
- **org.json** — JSON parsing
- **Google Gson** — JSON serialization/deserialization

### Backend
- **Java 21** — Core language
- **Spring Boot 4.0.4** — REST API framework
- **Spring Data JPA** — Data access layer
- **Hibernate 7.2** — ORM (Object-Relational Mapping)
- **Apache Tomcat 11** — Embedded web server (port 8080)
- **Maven** — Build and dependency management

### Database
- **MySQL 9.6** — Primary database (local)
- **MariaDB** — Compatible alternative (UWindsor CS server)

### DevOps & Tools
- **GitHub** — Version control and collaboration
- **VS Code** — Primary IDE
- **Microsoft Azure** — Cloud deployment (App Service)

---

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/Triangular%20Ruler.png" alt="Triangular Ruler" width="35" height="35" /> System Architecture

```
┌─────────────────────────────────────────────────────┐
│                   Java Swing GUI                     │
│         (ActionCard, ModuleScreen, MenuPanel)        │
│              Runs on developer machine               │
└──────────────────────┬──────────────────────────────┘
                       │ HTTP REST (localhost:8080)
┌──────────────────────▼──────────────────────────────┐
│              Spring Boot Backend                     │
│   Controllers → Services → Repositories → Models    │
│         AdminController  /admin/**                   │
│         BookingController /bookings/**               │
│         AdvisingController /advising/**              │
│         ScheduleController /schedule/**              │
│         ReportController /reports/**                 │
└──────────────────────┬──────────────────────────────┘
                       │ JDBC / JPA
┌──────────────────────▼──────────────────────────────┐
│                MySQL Database                        │
│            myadvicedatabase                          │
│  Tables: student, faculty, course, appointment,      │
│          schedule, section, transcript,              │
│          enrollment, program, prerequisite           │
└─────────────────────────────────────────────────────┘
```

---

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Activities/Crystal%20Ball.png" alt="Crystal Ball" width="35" height="35"/> Features

### Curriculum Advising
- View remaining courses required for degree completion
- Browse course prerequisite chains
- Track completed vs outstanding credits
- View available faculty advisors

### Scheduling
- Browse all course sections with instructor and room info
- View timetable by day of week
- View schedules by course

### Bookings
- Book new advising appointments with faculty
- View upcoming appointments by student
- View faculty availability
- Cancel existing appointments
- Reschedule appointments to a new date/time

### System Administration
- View and manage all courses
- View and manage prerequisite structures
- View all students and faculty profiles
- View and filter transcript records by student

### Reports & Dashboards
- Full student list with program and contact info
- Full faculty list with department info
- Most active students by appointment count
- Busiest faculty advisors by appointment load
- Course enrolment statistics

---
## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/Bow%20and%20Arrow.png" alt="Bow and Arrow" width="35" height="35"/> Getting Started

### Step 1 — Clone the Repository
```bash
git clone https://github.com/SadatTanzim23/myAdvice.git
cd myAdvice
```
### Step 2 — Verify the project structure
After cloning you should see:
```
myAdvice/
├── ActionCard.java
├── ModuleScreen.java
├── MainApp.java
├── myAdvice.java
├── MenuPanel.java
├── json.jar
├── gson.jar
├── run.sh
├── myAdvice/               ← Spring Boot backend
│   ├── pom.xml
│   └── src/
└── myadvicedatabasewithvalues/
    └── myadvice_full_export.sql
```
### Step 3 — Make the Maven wrapper executable (macOS/Linux only)
```bash
chmod +x myAdvice/mvnw
```
Now follow the Prerequisites, Database Setup, and Running Locally sections below.

---

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/Memo.png" alt="Memo" width="35" height="35"/> Prerequisites

Make sure the following are installed before running the project:

| Tool | Version | Download |
|---|---|---|
| Java JDK | 21 (LTS) | [Adoptium Temurin 21](https://adoptium.net/) |
| MySQL | 8.0+ | [MySQL Downloads](https://dev.mysql.com/downloads/) |
| Maven | Included via `mvnw` | — |
| Git | Any | [git-scm.com](https://git-scm.com/) |

---

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/Card%20File%20Box.png" alt="Card File Box" width="35" height="35"/> Database Setup

### Step 1 — Start MySQL
```bash
mysql.server start         # macOS (Homebrew)
sudo service mysql start   # Linux
# Or start via MySQL Workbench on Windows
```

### Step 2 — Create the database and user
```sql
mysql -u root -p

CREATE DATABASE myadvicedatabase;
CREATE USER 'usermyadvice'@'localhost' IDENTIFIED BY 'Comp2800!passWord!';
GRANT ALL PRIVILEGES ON myadvicedatabase.* TO 'usermyadvice'@'localhost';
FLUSH PRIVILEGES;
exit;
```

### Step 3 — Load the database schema and sample data
```bash
# Fix collation compatibility if using MySQL (not MariaDB)
python3 fix_collation.py   # optional, only if import fails

mysql -u usermyadvice -p'Comp2800!passWord!' myadvicedatabase < myadvicedatabasewithvalues/myadvice_full_export.sql
```

### Step 4 — Verify the tables loaded
```bash
mysql -u usermyadvice -p'Comp2800!passWord!' myadvicedatabase
```
```sql
SHOW TABLES;
-- Expected: appointment, course, enrollment, faculty, prerequisite,
--           program, schedule, section, student, transcript
SELECT COUNT(*) FROM student;   -- Should return 8
SELECT COUNT(*) FROM faculty;   -- Should return 9
SELECT COUNT(*) FROM course;    -- Should return 10
```

---

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/Desktop%20Computer.png" alt="Desktop Computer" width="35" height="35"/> Running Locally

The application requires **three components** running simultaneously:

### Terminal 1 — Start MySQL
```bash
mysql.server start
```

### Terminal 2 — Start Spring Boot Backend

**macOS/Linux:**
```bash
cd myAdvice
export JAVA_HOME=$(/usr/libexec/java_home -v 21)   # macOS only
chmod +x mvnw
./mvnw spring-boot:run
```

**Windows:**
```bash
cd myAdvice
mvnw.cmd spring-boot:run
```

Wait until you see:
```
Started MyAdviceApplication in X seconds
Tomcat started on port 8080
```

### Terminal 3 — Run the GUI
```bash
# From project root (where ActionCard.java, ModuleScreen.java etc. are)
export JAVA_HOME=$(/usr/libexec/java_home -v 21)   # macOS only
javac -cp .:json.jar:gson.jar *.java
java -cp .:json.jar:gson.jar myAdvice
```

**Windows:**
```bash
javac -cp .;json.jar;gson.jar *.java
java -cp .;json.jar;gson.jar myAdvice
```

### Quick Start Script (macOS/Linux)
```bash
./run.sh
```

---

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/Card%20Index%20Dividers.png" alt="Card Index Dividers" width="35" height="35"/> Database Configuration

The backend connects to the database using `myAdvice/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/myadvicedatabase
spring.datasource.username=usermyadvice
spring.datasource.password=Comp2800!passWord!
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> **Note for MariaDB users:** Change `jdbc:mysql` to `jdbc:mariadb` and update the driver class to `org.mariadb.jdbc.Driver` in `pom.xml`.

---

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/Chains.png" alt="Chains" width="35" height="35"/> Project Structure

```
myAdvice/
├── ActionCard.java              # Clickable module action card component
├── ApiClient.java               # HTTP client for REST API communication
├── BackButton.java              # Navigation back button component
├── MainApp.java                 # Main application window (CardLayout)
├── MenuCard.java                # Main menu module card component
├── MenuPanel.java               # Main menu panel with 5 module cards
├── ModuleScreen.java            # Module screen with action cards & API wiring
├── myAdvice.java                # Entry point, constants, and module definitions
├── json.jar                     # org.json library
├── gson.jar                     # Google Gson library
├── run.sh                       # Quick start script
│
├── myAdvice/                    # Spring Boot Maven project
│   ├── pom.xml
│   └── src/main/java/com/myadvice/
│       ├── MyAdviceApplication.java
│       ├── controller/
│       │   ├── AdminController.java
│       │   ├── AdvisingController.java
│       │   ├── BookingController.java
│       │   ├── ReportController.java
│       │   └── ScheduleController.java
│       ├── service/
│       │   ├── AdminService.java
│       │   ├── AdvisingService.java
│       │   ├── BookingService.java
│       │   ├── ReportService.java
│       │   └── ScheduleService.java
│       ├── repository/
│       │   ├── AppointmentRepository.java
│       │   ├── CourseRepository.java
│       │   ├── FacultyRepository.java
│       │   ├── ScheduleRepository.java
│       │   ├── SectionRepository.java
│       │   ├── StudentRepository.java
│       │   └── TranscriptRepository.java
│       ├── model/
│       │   ├── Appointment.java
│       │   ├── Course.java
│       │   ├── Enrollment.java
│       │   ├── Faculty.java
│       │   ├── Schedule.java
│       │   ├── Section.java
│       │   ├── Student.java
│       │   └── Transcript.java
│       └── dto/
│           ├── BookingRequest.java
│           └── EnrollmentRequest.java
│
└── myadvicedatabasewithvalues/
    └── myadvice_full_export.sql  # Full database export with sample data
```

---

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/DNA.png" alt="DNA" width="35" height="35"/> API Endpoints

### Bookings `/bookings`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/bookings/book` | Book a new appointment |
| GET | `/bookings/student/{id}` | Get appointments by student |
| GET | `/bookings/faculty/{id}` | Get appointments by faculty |
| DELETE | `/bookings/cancel/{id}` | Cancel an appointment |
| PUT | `/bookings/reschedule/{id}` | Reschedule an appointment |

### Admin `/admin`
| Method | Endpoint | Description |
|---|---|---|
| GET | `/admin/courses` | Get all courses |
| POST | `/admin/courses/add` | Add a new course |
| PUT | `/admin/courses/edit/{id}` | Edit a course |
| DELETE | `/admin/courses/delete/{id}` | Delete a course |
| GET | `/admin/courses/{id}/prerequisites` | Get course prerequisites |
| POST | `/admin/courses/{id}/prerequisites/add/{prereqId}` | Add prerequisite |
| GET | `/admin/prerequisites/all` | Get all prerequisite relationships |
| GET | `/admin/transcripts` | Get all transcripts |
| GET | `/admin/transcripts/student/{id}` | Get transcripts by student |
| GET | `/admin/students` | Get all students |
| GET | `/admin/faculty` | Get all faculty |

### Advising `/advising`
| Method | Endpoint | Description |
|---|---|---|
| GET | `/advising/remaining/{studentId}` | Get remaining courses |
| GET | `/advising/completed/{studentId}` | Get completed courses |
| GET | `/advising/prerequisites/{studentId}/{courseId}` | Check prereq eligibility |

### Schedule `/schedule`
| Method | Endpoint | Description |
|---|---|---|
| GET | `/schedule/all` | Get all schedules |
| GET | `/schedule/course/{courseId}` | Get schedule by course |
| GET | `/schedule/day/{dayOfWeek}` | Get schedule by day |

### Reports `/reports`
| Method | Endpoint | Description |
|---|---|---|
| GET | `/reports/students` | Get all students |
| GET | `/reports/faculty` | Get all faculty |
| GET | `/reports/students/most-appointments` | Most active students |
| GET | `/reports/faculty/most-appointments` | Busiest faculty advisors |

---

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Travel%20and%20places/Rocket.png" alt="Rocket" width="35" height="35"/> Deployment

### Local Deployment
The application is designed to run locally with MySQL. See [Running Locally](#running-locally) above.

### Azure Deployment
The Spring Boot backend has been deployed to **Microsoft Azure App Service**:
- **Platform:** Azure App Service (Java 21, Linux)
- **Region:** East US
- **Plan:** Free F1

> **Note:** The Swing GUI is a desktop application and runs locally. The backend API can be hosted on Azure and accessed remotely by updating `BASE_URL` in `ModuleScreen.java` from `http://localhost:8080` to the Azure App Service URL.

### Deployment Notes
The University of Windsor SCS VM was explored as a deployment option however the shared hosting environment (`myweb.cs.uwindsor.ca`) runs Apache/PHP and does not support persistent Java processes required for Spring Boot. As per the course instructor's guidance, the application was successfully deployed locally and on Microsoft Azure App Service. Full deployment instructions are documented above.

---

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Symbols/Double%20Exclamation%20Mark.png" alt="Double Exclamation Mark" width="35" height="35"/> Known Limitations

- The Swing GUI connects to `localhost:8080` by default — update `BASE_URL` in `ModuleScreen.java` to point to a remote server for non-local use
- Free F1 Azure tier has a 60-minute CPU limit per day
- The application does not implement user authentication — all users access all modules

---
### SNEAK PEAK <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Hand%20gestures/Eyes.png" alt="Eyes" width="40" height="40" />

<img width="1582" height="1282" alt="image" src="https://github.com/user-attachments/assets/85baeb0e-d828-41fd-bb92-1db92a8c8f70"/>
<img width="1588" height="1282" alt="image" src="https://github.com/user-attachments/assets/b54dfa0c-26c4-407a-8f11-1b2b7c0a8bdd"/>
<img width="1740" height="1296" alt="image" src="https://github.com/user-attachments/assets/3afaa9d0-e176-4c89-8b90-2472ca8cd0b4"/>
<img width="798" height="639" alt="Screenshot 2026-03-29 at 5 05 32 PM" src="https://github.com/user-attachments/assets/b33bb28b-225a-4f31-a87d-d0e0344a971b"/> <img width="797" height="644" alt="Screenshot 2026-03-29 at 5 06 09 PM" src="https://github.com/user-attachments/assets/d574dedf-c678-4769-b70c-f87cadd9bfbb"/> <img width="798" height="646" alt="Screenshot 2026-03-29 at 5 06 42 PM" src="https://github.com/user-attachments/assets/625b9fa3-c880-4aa0-bb81-9702fc82c6d8"/> <img width="795" height="648" alt="Screenshot 2026-03-29 at 5 07 18 PM" src="https://github.com/user-attachments/assets/72d31098-d54a-484a-9f7b-8b703d5a01a7"/>












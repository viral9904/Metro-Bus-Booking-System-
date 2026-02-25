\# Bus \& Metro Online Booking Platform



A full-stack web application for managing Bus \& Metro ticket bookings with role-based authentication, admin management, and passenger booking flow.





**##  Tech Stack**



\### Backend

\- Java 21

\- Spring Boot

\- Spring Data JPA (Hibernate)

\- Spring Security

\- MySQL

\- Maven



\### Frontend

\- Thymeleaf

\- Bootstrap 5



---



**##  User Roles**



\###  Passenger (USER)

\- Register / Login

\- Search routes (source, destination, date)

\- View available schedules

\- Select seat class

\- Add passenger details

\- Make payment (UPI / Cash)

\- View booking history



**###  Admin** 

\- Secure login

\- Admin dashboard

\- Manage:

&nbsp; - Stops (CRUD)

&nbsp; - Routes (CRUD)

&nbsp; - Vehicles (CRUD)

&nbsp; - Schedules (CRUD)

&nbsp; - Seat Classes (CRUD)

\- View all bookings

\- View payment status



---



**##  Database Modules**



\- Users

\- Stops

\- Routes

\- Vehicles

\- Schedules

\- Seat Classes

\- Bookings

\- Passengers

\- Payments



---



**##  Security Features**



\- Role-based authentication \& authorization

\- BCrypt password encryption

\- URL access restrictions:

&nbsp; - `/admin/\*\*` → ADMIN only

&nbsp; - `/user/\*\*` → USER only

&nbsp; - `/auth/\*\*` → Public access



---



**##  Payment Flow**



\- UPI → Payment SUCCESS → Booking CONFIRMED

\- Cash → Payment PENDING\_CASH → Booking CONFIRMED (Pay at counter)



---





---



**##  How to Run the Project**



\###  Create Database



```sql

CREATE DATABASE bm\_booking;



**Configure application.properties**





spring.datasource.url=jdbc:mysql://localhost:3306/bm\_booking

spring.datasource.username=your\_username

spring.datasource.password=your\_password

spring.jpa.hibernate.ddl-auto=update





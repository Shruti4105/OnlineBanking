# Online Banking System

This is a full-stack Online Banking System built with **Java Spring Boot**, **Plain JDBC**, **MySQL**, and **HTML/CSS**.

## Project Features
- **MVC Architecture**: Clear separation of concern using Models, strictly JDBC based Data Access Objects (DAO), Services, and Controllers.
- **Object-Oriented Programming (OOP)**: Incorporates Abstract classes (`Account`), Inheritance (`SavingsAccount`, `CurrentAccount`), Encapsulation, and Polymorphism.
- **Frontend**: Clean and structured pages built purely with HTML5 and Inline CSS, avoiding external CSS/JS libraries in adherence to project requirements.
- **Security & Session**: Safe authentication structure and standard HTTP session management.
- **Banking Features**: Funds deposit, withdrawal, transfers across accounts, real-time transaction history recording, and Admin reporting.

---

## 🚀 Step-by-Step Instructions to Run

### 1. Database Setup
1. Ensure you have **MySQL Workbench** or MySQL Server running on `localhost:3306`.
2. Open your MySQL client and run the provided SQL script:
   - Import the `database.sql` file.
   - Run the script. This will create the `banking_system` database, tables, and a default Admin user (`username: admin`, `password: admin123`).
3. Ensure the username `root` and password `Shruti18` are valid for your MySQL installation. If your local credentials differ, update `src/main/resources/application.properties`.

### 2. Loading the Project in an IDE

#### Using IntelliJ IDEA:
1. Open IntelliJ IDEA.
2. Go to **File -> Open** and select the `BankingSystem` folder containing the `pom.xml`.
3. IntelliJ will automatically detect it as a Maven Project and start resolving dependencies.
4. Open the `BankingSystemApplication.java` file (located in `src/main/java/com/banking/`).
5. Click the green "Run" arrow next to the class name or `main` method.

#### Using Eclipse:
1. Open Eclipse.
2. Go to **File -> Import -> Maven -> Existing Maven Projects**.
3. Click "Browse" and select the `BankingSystem` folder. Check the `pom.xml` in the Projects window to ensure it's loaded.
4. Click **Finish**. Eclipse will download all necessary Maven dependencies.
5. In the Package Explorer, locate `BankingSystemApplication.java` in `src/main/java/com/banking/`.
6. Right-click the file -> **Run As -> Java Application**.

### 3. Accessing the Application
Once the Spring Boot application says "Started BankingSystemApplication" in the terminal console:
1. Open up your web browser (Chrome, Firefox, Safari).
2. Go to: **[http://localhost:8080](http://localhost:8080)**

### 4. Testing Default Features
- **Admin Login**: 
  - Click Login, use username: `admin` and password: `admin123`.
  - Explore user management and CSV reporting via the admin dropdown.
- **Customer Account**: 
  - Go to Register.
  - Complete the sign up form to naturally create an account and immediately access the deposit console.

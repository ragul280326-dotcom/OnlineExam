# Online Examination System (Java Full Stack)

A beginner-friendly web application built with Java 17, Spring Boot, Thymeleaf, HTML, CSS, and JavaScript. It contains a student name entry page, 10 multiple-choice questions, progress tracking, automatic scoring, and answer review.

## Requirements
- JDK 17 or newer
- Maven 3.6+ (or use Maven support in VS Code)
- VS Code
- Recommended VS Code extensions: Extension Pack for Java and Spring Boot Extension Pack

## Run in VS Code (Windows)
1. Extract the ZIP file.
2. Open VS Code.
3. Select **File → Open Folder** and choose `online-examination-system`.
4. Ensure JDK 17+ is installed. In the VS Code terminal, run:
   ```powershell
   java -version
   mvn -version
   ```
5. In the terminal at the project root, run:
   ```powershell
   mvn spring-boot:run
   ```
6. Wait until the terminal says the application has started. Open:
   `http://localhost:8080`
7. Enter a student name, start the exam, answer questions, and submit.

## If `mvn` is not recognized
Install Maven and add its `bin` directory to Windows PATH, then restart VS Code. Alternatively, open the folder in an IDE with Maven support and run `OnlineExaminationApplication.java`.

## Project structure
```text
online-examination-system/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/com/ragul/exam/
        │   ├── OnlineExaminationApplication.java
        │   ├── ExamController.java
        │   └── Question.java
        └── resources/
            ├── static/style.css
            └── templates/
                ├── index.html
                ├── exam.html
                └── result.html
```

## Notes
- This demo stores questions in Java code and uses an HTTP session for the student name. It does not need MySQL.
- It is intended as a learning/demo project, not a production exam platform. For production, add persistent storage, authenticated student/admin accounts, server-side exam timing, stronger security, and audit logging.
- Internet is only used to load optional Google Fonts. The app itself can run without them.

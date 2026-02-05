# 🤖 AI SQL Assistant

This is a Java program that helps you talk to a Database using plain English. Instead of typing complex code, you just ask a question, and the **AI (Google Gemini)** writes the SQL query for you!

## 🌟 What it Does
* **Login & Register**: Secure entry to the app.
* **English to SQL**: Turn "Show me all users" into `SELECT * FROM users`.
* **Safe Database**: It connects to a MySQL database to store and show information.
* **Modern UI**: A clean window design made with Java Swing.

## 🛠️ How to Setup
1. **Database**: Open your MySQL Workbench and run the script inside the `sql` folder.
2. **Libraries**: Add the `.jar` files from the `lib` folder to your Java project.
3. **API Key**: Put your Google Gemini API key inside the `AISQLAssistant.java` file.
4. **Run**: Start the project by running `Login.java`.

## 📚 Tools Used
* **Java**: The main language.
* **MySQL**: To store data.
* **Google Gemini API**: The "brain" that understands English.

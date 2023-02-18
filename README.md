# Spring Boot Cassandra Thymeleaf Mail App

This is a sample Spring Boot web application that uses Apache Cassandra as a database, Thymeleaf for templating, and provides basic email functionality. It is designed to be highly available and scalable, and can be downloaded and run on `localhost:8080`. The application also includes authentication via GitHub login.

## Features

- User registration and login via GitHub
- Compose and send email messages
- View inbox, sent messages

## Technologies

- Spring Boot
- Apache Cassandra
- Thymeleaf
- Spring Security
- GitHub OAuth

## Requirements

- JDK 8 or higher
- Apache Cassandra 3.x or higher
- A DataStrax account
- A registered GitHub OAuth application with a client ID and secret.

## Installation

1. Clone the repository.
2. Create a GitHub OAuth application and obtain a client ID and secret.
3. Set the client ID and secret as environment variables in your operating system. For example:

export GITHUB_CLIENT_ID=YOUR_CLIENT_ID
export GITHUB_CLIENT_SECRET=YOUR_CLIENT_SECRET

4. Start Apache Cassandra by running your app in your Data Starx account.
5. Run the application by running the command `mvn spring-boot:run`.
6. Access the application by visiting `localhost:8080` in your web browser.

## Usage

1. Log in to the application using your GitHub account.
2. Compose and send email messages by clicking on the "Compose" button.
3. View your inbox, sent messages, and draft messages by clicking on the corresponding tabs.

## Contributors

- [Vandana Gupta](https://github.com/Gupta-Vandana)

Feel free to contribute to this project by submitting pull requests or creating issues. If you have any questions, please contact the contributors listed above.

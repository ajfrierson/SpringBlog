# SpringBlog
# StudyEasy SpringBlog

A full-stack blogging platform built with Spring Boot and Thymeleaf, featuring user authentication, rich text post creation, and role-based access control.

---

## Features

- User registration and login with Spring Security
- Role-based access control (User, Editor, Admin)
- Create, edit, and view blog posts
- Rich text editor powered by CKEditor 5
- Responsive design with Bootstrap 5
- Thymeleaf templating with reusable fragments

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java, Spring Boot |
| Frontend | Thymeleaf, Bootstrap 5, HTML/CSS |
| Rich Text Editor | CKEditor 5 (CDN) |
| Security | Spring Security |
| Database | Spring Data JPA |
| Build Tool | Maven |

---

## Project Structure

```
src/
└── main/
    ├── java/org/studyeasy/SpringBlog/
    │   ├── controller/
    │   │   ├── AccountController.java
    │   │   └── PostController.java
    │   ├── models/
    │   │   ├── Account.java
    │   │   └── Post.java
    │   ├── services/
    │   │   ├── AccountService.java
    │   │   └── PostService.java
    │   └── security/
    │       └── WebSecurityConfig.java
    └── resources/
        ├── static/
        │   ├── css/
        │   ├── js/
        │   └── images/
        └── templates/
            ├── fragments/
            │   ├── head.html
            │   ├── header.html
            │   └── footer.html
            ├── post_views/
            │   ├── post.html
            │   ├── post_add.html
            │   └── post_edit.html
            └── account_views/
                ├── register.html
                └── profile.html
```

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+

### Installation

1. Clone the repository:
```bash
git clone https://github.com/your-username/SpringBlog.git
cd SpringBlog
```

2. Copy the properties template and fill in your values:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

3. Configure `application.properties`:
```properties
spring.datasource.url=your_database_url
spring.datasource.username=your_username
spring.datasource.password=your_password
```

4. Run the application:
```bash
./mvnw spring-boot:run
```

5. Navigate to `http://localhost:8080`

---

## Usage

### Registering an Account
Navigate to `/register` and fill in your email, password, name, age, date of birth, and gender.

### Creating a Post
Log in and navigate to `/posts/add`. Enter a title and use the CKEditor rich text editor to write your post body, then click **Add Post**.

### Editing a Post
Navigate to any post you own and click the edit button. Only the post owner can edit their posts.

---

## Security

- Passwords are encrypted using BCrypt
- Routes are protected by Spring Security
- Post ownership is verified server-side using the authenticated `Principal` — client-side hidden fields are not trusted for ownership

### Protected Routes

| Route | Access |
|---|---|
| `/` | Public |
| `/register` | Public |
| `/login` | Public |
| `/posts/**` | Authenticated |
| `/profile/**` | Authenticated |
| `/admin/**` | Admin only |
| `/editor/**` | Admin or Editor |

---

## Environment Variables

Never commit `application.properties` to version control. Use the provided example file as a template:

```properties
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=
```

---

## Acknowledgements

- [CKEditor 5](https://ckeditor.com/ckeditor-5/) for the rich text editor
- [Bootstrap 5](https://getbootstrap.com/) for responsive UI components
- [StudyEasy](https://studyeasy.org/) for the original project template

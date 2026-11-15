# University Management System (UMS)

A simple University Management System web application built with **Java Spring Boot**, featuring three user roles: **Admin**, **Teacher**, and **Student** — with full CRUD REST APIs, Spring Security, and Docker support.

> **Course:** CSE 3230 – Software Engineering and Project Management Lab  
> **Student ID:** 2207040

---

## Suchipotro (Table of Contents)

1. [Project ta ki?](#1-project-ta-ki)
2. [Project er folder structure](#2-project-er-folder-structure)
3. [Application Flow — Kivabe kaj kore](#3-application-flow--kivabe-kaj-kore)
4. [Model Layer — Database er table gulo](#4-model-layer--database-er-table-gulo)
5. [Repository Layer — Database theke data ana/pathano](#5-repository-layer--database-theke-data-anapathano)
6. [Security Layer — Login, Password, ar Access Control](#6-security-layer--login-password-ar-access-control)
7. [Controller Layer — URL gulo kivabe kaj kore](#7-controller-layer--url-gulo-kivabe-kaj-kore)
8. [Config Layer — App start hoye ki kore](#8-config-layer--app-start-hoye-ki-kore)
9. [URL gulo kivabe toiri hoy ar Frontend e connect hoy](#9-url-gulo-kivabe-toiri-hoy-ar-frontend-e-connect-hoy)
10. [Frontend (index.html) — Login page ar Dashboard](#10-frontend-indexhtml--login-page-ar-dashboard)
11. [Dockerfile — Protita line er mane](#11-dockerfile--protita-line-er-mane)
12. [compose.yaml — Protita line er mane](#12-composeyaml--protita-line-er-mane)
13. [How to Run](#13-how-to-run)
14. [API Endpoints Summary](#14-api-endpoints-summary)
15. [Sample cURL Commands](#15-sample-curl-commands)

---

## 1. Project ta ki?

Eta ekta **University Management System** — mane university te 3 dhoroner user ache:

| Role | Ki korte pare |
|------|---------------|
| **Admin** | Shob kichu manage korte pare — admin, teacher, student shob dekha, add, edit, delete |
| **Teacher** | Shudhu teacher list ta manage korte pare — teacher add, edit, delete |
| **Student** | Shudhu student list ta manage korte pare — student add, edit, delete |

**Ki ki technology use kora hoyeche:**
- **Java 17** — programming language
- **Spring Boot 3.2.5** — Java er framework, web app toiri korar jonno
- **Spring Security** — login/logout ar permission check korar jonno
- **Spring Data JPA + Hibernate** — Java object ke database table e convert kore (SQL likha lage na)
- **H2 Database** — locally run korar jonno choto in-memory database (kono install lage na)
- **MySQL 8.0** — Docker e real database use kore
- **HTML/CSS/JavaScript** — frontend UI (browser e dekhay)
- **Docker + Docker Compose** — pura app ke container e bhore run korano

---

## 2. Project er folder structure

```
2207040_project/
├── compose.yaml              ← Docker Compose file — app + MySQL ekshathe chalay
├── Dockerfile                ← Docker image banay — app ke container e dhoray
├── pom.xml                   ← Maven config — ki ki library (dependency) lagbe shob ekhane
├── README.md                 ← Project er documentation (eta)
└── src/main/
    ├── java/com/example/ums/
    │   ├── UmsApplication.java        ← MAIN FILE — app ekhane theke start hoy
    │   ├── config/
    │   │   ├── DataInitializer.java   ← App start hole default user create kore
    │   │   └── SecurityConfig.java    ← Security rules — ke kon URL access korte parbe
    │   ├── controller/
    │   │   ├── AdminController.java   ← /api/admins URL handle kore
    │   │   ├── TeacherController.java ← /api/teachers URL handle kore
    │   │   └── StudentController.java ← /api/students URL handle kore
    │   ├── model/
    │   │   ├── BaseUser.java          ← Common fields — id, username, password, name, role
    │   │   ├── Admin.java             ← Admin er extra field — department
    │   │   ├── Teacher.java           ← Teacher er extra field — subject
    │   │   └── Student.java           ← Student er extra field — studentId
    │   ├── repository/
    │   │   ├── AdminRepository.java   ← Admin table theke data anay/pathay
    │   │   ├── TeacherRepository.java ← Teacher table theke data anay/pathay
    │   │   └── StudentRepository.java ← Student table theke data anay/pathay
    │   └── security/
    │       └── CustomUserDetailsService.java ← Login er somoy DB theke user khunje ber kore
    └── resources/
        ├── application.yml            ← App er settings (database connection, port)
        └── static/
            └── index.html             ← Frontend — login page + dashboard
```

---

## 3. Application Flow — Kivabe kaj kore

Dhoro tumi browser e giye `/api/teachers` e request pathale, tarpor ki ki hoy:

```
Step 1:  Tumi browser/frontend theke request pathao
             ↓
Step 2:  Spring Security check kore —
         - Login koreche ki? (username + password thik ache?)
         - Role ki ei URL access korte pare?
             ↓
Step 3:  Allowed hole → Controller er method call hoy
         (jemon TeacherController.getAllTeachers())
             ↓
Step 4:  Controller Repository ke bole data ante
         (teacherRepository.findAll())
             ↓
Step 5:  Repository automatically SQL generate kore
         ("SELECT * FROM teachers") ar database e run kore
             ↓
Step 6:  Data ase → Controller JSON banaye return kore
             ↓
Step 7:  Frontend (index.html) JSON ta receive kore table e dekhay
```

**Simple bhabe:** User click kore → Security check kore → Controller process kore → Repository database theke anay → JSON return hoy → Frontend table e dekhay.

---

## 4. Model Layer — Database er table gulo

Model mane hocche **Java class je database table represent kore**. JPA annotation use kore bole dilam kon field kon column e jabe.

### BaseUser.java — Shob user er parent class

Ei class ta hocche shob user er "baba". Admin, Teacher, Student — shobai etar theke inherit kore.

```java
@Entity                                          // "Ei class ta ekta database table"
@Inheritance(strategy = InheritanceType.JOINED)   // "Child der jonno alag table banao, foreign key diye connect koro"
@Table(name = "users")                            // Table er naam "users"
public abstract class BaseUser {
    @Id @GeneratedValue(strategy = IDENTITY)      // Auto-increment primary key (1, 2, 3...)
    private Long id;

    @Column(nullable = false, unique = true)      // Username unique, null hole na
    private String username;

    private String password;                      // BCrypt diye hashed thake
    private String name;                          // Full name
    private String role;                          // ROLE_ADMIN / ROLE_TEACHER / ROLE_STUDENT
}
```

### Admin.java — BaseUser + department
### Teacher.java — BaseUser + subject
### Student.java — BaseUser + studentId

**Database e table gulo:**
```
users:     id=1, username="admin",   password="$2a$10$...", name="System Admin",    role="ROLE_ADMIN"
admins:    id=1 (FK → users.id), department="IT"
teachers:  id=2 (FK → users.id), subject="Computer Science"
students:  id=3 (FK → users.id), student_id="2207040"
```

JOINED inheritance mane — **users** table e common data thake, ar **admins/teachers/students** table e extra data, same `id` diye connected.

---

## 5. Repository Layer — Database theke data ana/pathano

Repository hocche **database er shathe kotha bolay**. SQL likha lage na — Spring Data JPA automatically generate kore.

```java
public interface AdminRepository extends JpaRepository<Admin, Long> { }
// Bas eitukui code! Spring ei method gulo automatic dey:
```

| Method | Ki kore | SQL |
|--------|---------|-----|
| `findAll()` | Shob record anay | `SELECT * FROM admins` |
| `findById(5)` | ID 5 er record | `SELECT * FROM admins WHERE id = 5` |
| `save(admin)` | Insert ba update | `INSERT INTO...` / `UPDATE...` |
| `deleteById(5)` | Delete kore | `DELETE FROM admins WHERE id = 5` |
| `count()` | Total record count | `SELECT COUNT(*) FROM admins` |

**UserRepository** (login er jonno):
```java
interface UserRepository extends JpaRepository<BaseUser, Long> {
    Optional<BaseUser> findByUsername(String username);
    // Spring method naam theke bujhe ney: SELECT * FROM users WHERE username = ?
}
```

---

## 6. Security Layer — Login, Password, ar Access Control

### CustomUserDetailsService.java — Login er brain

Login er somoy ki hoy step by step:

```
1. User login kore: username "admin", password "admin123"
2. Spring Security → CustomUserDetailsService.loadUserByUsername("admin") call kore
3. Ei method → UserRepository.findByUsername("admin") call kore → DB theke user khujje
4. User paoa gele return kore: username, hashed password, role
5. Spring Security password compare kore BCrypt diye
6. Match korle → Login successful ✅  |  Na korle → 401 Unauthorized ❌
```

### SecurityConfig.java — Ke ki korte parbe

**3 ta important jinish:**

**1) Password Encoder:**
```java
new BCryptPasswordEncoder()
```
Password ke hash kore — "admin123" hoy "$2a$10$xYz...". Plain text kabhu store hoy na. BCrypt one-way — hash theke original password ber kora jay na.

**2) Access Rules:**
```java
.requestMatchers("/", "/index.html").permitAll()              // Login page shobai dekhte parbe
.requestMatchers("/api/admins/**").hasRole("ADMIN")           // ADMIN only
.requestMatchers("/api/teachers/**").hasAnyRole("ADMIN","TEACHER")  // ADMIN ba TEACHER
.requestMatchers("/api/students/**").hasAnyRole("ADMIN","STUDENT")  // ADMIN ba STUDENT
.anyRequest().authenticated()                                  // Baki shob e login lagbe
```

**3) HTTP Basic Auth:**
```java
.httpBasic(Customizer.withDefaults())   // Protita request e username:password header e pathate hobe
```

### RBAC Summary:

| URL | ADMIN | TEACHER | STUDENT | Login chara |
|-----|:-----:|:-------:|:-------:|:-----------:|
| `/` (login page) | ✅ | ✅ | ✅ | ✅ |
| `/api/admins/**` | ✅ | ❌ 403 | ❌ 403 | ❌ 401 |
| `/api/teachers/**` | ✅ | ✅ | ❌ 403 | ❌ 401 |
| `/api/students/**` | ✅ | ❌ 403 | ✅ | ❌ 401 |

---

## 7. Controller Layer — URL gulo kivabe kaj kore

Controller hocche **URL ke Java method er shathe map kore**. 

### URL mapping:

```java
@RestController                     // REST API handle korbe (JSON return korbe)
@RequestMapping("/api/teachers")    // Base URL: /api/teachers

    @GetMapping              // GET /api/teachers       → shob teacher er list
    @GetMapping("/{id}")     // GET /api/teachers/5     → ID 5 er teacher
    @PostMapping             // POST /api/teachers      → notun teacher create
    @PutMapping("/{id}")     // PUT /api/teachers/5     → ID 5 er teacher update
    @DeleteMapping("/{id}")  // DELETE /api/teachers/5  → ID 5 delete
```

**Important annotations:**
- `@PathVariable Long id` — URL er `{id}` part ta variable hisebe nay (jemon `/api/teachers/5` → id=5)
- `@RequestBody Teacher teacher` — Request er body (JSON) ke Teacher object e convert kore
- `ResponseEntity` — HTTP response control kore (status code + data)

### Tin controller:

| Controller | URL | Access |
|-----------|-----|--------|
| AdminController | `/api/admins/**` | ADMIN only |
| TeacherController | `/api/teachers/**` | ADMIN ba TEACHER |
| StudentController | `/api/students/**` | ADMIN ba STUDENT |

**Response e password kabhu return hoy na** — `toResponse()` method shudhu safe fields return kore.

---

## 8. Config Layer — App start hoye ki kore

### DataInitializer.java

App prothom bar start hole database e kono user nai — tai ei class automatically 3 ta default user create kore:

```
adminRepo.count() == 0?  → Hya → admin/admin123 create koro (password BCrypt diye hash kore)
teacherRepo.count() == 0? → Hya → teacher/teacher123 create koro
studentRepo.count() == 0? → Hya → student/student123 create koro
```

### application.yml — 2 ta profile ache:

- **local profile** (IntelliJ e run korle) → H2 in-memory database — kono MySQL lage na
- **docker profile** (Docker e run korle) → MySQL database use kore

---

## 9. URL gulo kivabe toiri hoy ar Frontend e connect hoy

### 3 ta step:

**Step 1 — Controller e URL define:** `@RequestMapping("/api/teachers")` + `@GetMapping`

**Step 2 — Security protect kore:** `requestMatchers("/api/teachers/**").hasAnyRole("ADMIN","TEACHER")`

**Step 3 — Frontend JavaScript call kore:**
```javascript
fetch('/api/teachers', { headers: { 'Authorization': authHeader } })
```

### Full Connection Map:

```
FRONTEND (index.html)                    BACKEND (Spring Boot)
─────────────────────                    ──────────────────────
login() → fetch('/api/admins')      →    AdminController.getAllAdmins()
loadData() → fetch('/api/teachers') →    TeacherController.getAllTeachers()
saveRecord() → POST /api/teachers   →    TeacherController.createTeacher()
saveRecord() → PUT /api/teachers/5  →    TeacherController.updateTeacher(5)
editRecord() → GET /api/teachers/5  →    TeacherController.getTeacherById(5)
deleteRecord() → DELETE /api/teachers/5 → TeacherController.deleteTeacher(5)
logout() → POST /api/logout         →    Spring Security LogoutHandler
```

---

## 10. Frontend (index.html) — Login page ar Dashboard

`src/main/resources/static/index.html` — Spring Boot automatically `static/` folder serve kore.

### Login Process:

```
1. User username/password dey
2. JavaScript Base64 encode kore: authHeader = 'Basic ' + btoa('admin:admin123')
3. Role detect kore — GET /api/admins try kore:
   - 200 OK → ADMIN
   - 403 → try /api/teachers → 200 OK → TEACHER
   - 403 → try /api/students → 200 OK → STUDENT
4. Dashboard dekhay role onujayi tabs diye
```

### Role-based tab dekhay:
- **Admin** → 3 tab (Admins, Teachers, Students)
- **Teacher** → 1 tab (Teachers only)
- **Student** → 1 tab (Students only)

---

## 11. Dockerfile — Protita line er mane

Multi-stage build — prothome build kore, tarpor choto image e run kore.

### Stage 1: BUILD

```dockerfile
FROM maven:3.9-eclipse-temurin-17 AS build
# Maven + Java 17 er image nao, naam dao "build"

WORKDIR /app
# Container er /app folder e jao

COPY pom.xml .
# pom.xml copy koro (dependency list)

RUN mvn dependency:go-offline -B
# Shob library download koro
# Alag kora karon Docker cache kore — code change holeo dependency redownload hobe na

COPY src ./src
# Source code copy koro

RUN mvn clean package -DskipTests -B
# Code compile koro ar .jar file toiri koro target/ folder e
# -DskipTests → test skip koro (faster)
```

### Stage 2: RUN

```dockerfile
FROM eclipse-temurin:17-jre
# Notun choto image — shudhu Java JRE (JDK na, karon compile already hoye geche)

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
# Build stage theke compiled .jar file ta ei image e copy koro

EXPOSE 8080
# Documentation — app port 8080 e chale

ENTRYPOINT ["java", "-jar", "app.jar"]
# Container start hole ei command run hoy — app start hoy
```

**Keno multi-stage?** Final image e Maven, JDK, source code kichui nai — shudhu JRE + JAR. Image size ~800MB theke ~300MB e komay.

---

## 12. compose.yaml — Protita line er mane

2 ta container ekshathe chalay — MySQL + Spring Boot app.

### MySQL Service:

```yaml
mysql:
  image: mysql:8.0                    # Official MySQL 8.0 image use koro
  container_name: ums-mysql           # Container er naam
  environment:
    MYSQL_ROOT_PASSWORD: root         # Root password set koro
    MYSQL_DATABASE: ums_db            # Automatically "ums_db" database create koro
  ports:
    - "3307:3306"                     # Host 3307 → Container 3306 (MySQL er port)
  volumes:
    - mysql_data:/var/lib/mysql       # Data persist koro — container delete korleo data thakbe
  healthcheck:
    test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
    interval: 10s                     # 10 sec por por check koro MySQL ready ki na
    retries: 5                        # 5 bar try koro
```

### App Service:

```yaml
app:
  build: .                            # Dockerfile diye image build koro
  container_name: ums-app
  ports:
    - "8080:8080"                     # Browser e localhost:8080 e access korte parba
  environment:
    SPRING_PROFILE: docker            # Docker profile activate koro (MySQL use korbe)
    MYSQL_HOST: mysql                 # "mysql" = uporer MySQL service er naam
                                      # Docker Compose network e ei naam diye access hoy
    MYSQL_PORT: 3306
    MYSQL_DB: ums_db
    MYSQL_USER: root
    MYSQL_PASSWORD: root
  depends_on:
    mysql:
      condition: service_healthy      # MySQL READY na hole app START hobei na!
                                      # Eta na dile app crash korbe
```

### Docker Networking:

```
Docker Network er bhitore:
  mysql container (port 3306) ←→ app container (port 8080)
  (app "mysql" naam diye MySQL ke khunje pay)

Bairer theke (tomar browser):
  localhost:8080 → app container
  localhost:3307 → mysql container
```

---

## 13. How to Run

### IntelliJ IDEA (Local):
1. Project open koro IntelliJ te
2. JDK 17 set koro
3. Maven reload koro
4. `UmsApplication.java` run koro (play button)
5. Browser: `http://localhost:8080`

### Docker Compose:
```bash
docker compose up --build
```
Browser: `http://localhost:8080`

### Default Login:

| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| Teacher | teacher | teacher123 |
| Student | student | student123 |

---

## 14. API Endpoints Summary

### Admin (`/api/admins`) — ADMIN only
| Method | URL | Ki kore |
|--------|-----|---------|
| GET | `/api/admins` | Shob admin dekhay |
| GET | `/api/admins/{id}` | Ekta admin dekhay |
| POST | `/api/admins` | Notun admin banay |
| PUT | `/api/admins/{id}` | Admin update kore |
| DELETE | `/api/admins/{id}` | Admin delete kore |

### Teacher (`/api/teachers`) — ADMIN ba TEACHER
| Method | URL | Ki kore |
|--------|-----|---------|
| GET | `/api/teachers` | Shob teacher dekhay |
| GET | `/api/teachers/{id}` | Ekta teacher dekhay |
| POST | `/api/teachers` | Notun teacher banay |
| PUT | `/api/teachers/{id}` | Teacher update kore |
| DELETE | `/api/teachers/{id}` | Teacher delete kore |

### Student (`/api/students`) — ADMIN ba STUDENT
| Method | URL | Ki kore |
|--------|-----|---------|
| GET | `/api/students` | Shob student dekhay |
| GET | `/api/students/{id}` | Ekta student dekhay |
| POST | `/api/students` | Notun student banay |
| PUT | `/api/students/{id}` | Student update kore |
| DELETE | `/api/students/{id}` | Student delete kore |

---

## 15. Sample cURL Commands

```bash
# Admin diye shob teacher dekho
curl -u admin:admin123 http://localhost:8080/api/teachers

# Notun student create koro (admin hisebe)
curl -u admin:admin123 -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"username":"rakib","password":"rakib123","name":"Rakib Hasan","studentId":"2207041"}'

# Student diye student list dekho
curl -u student:student123 http://localhost:8080/api/students

# Student diye admin access korte gele 403 pabe
curl -u student:student123 http://localhost:8080/api/admins
```

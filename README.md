# ecommerceapp

A Spring Boot backend for a vacation shop: customers browse vacation packages, add optional
excursions, and check out. It exposes the data model as a REST (Representational State Transfer)
API over MySQL, plus one hand-written checkout endpoint that turns a cart into a persisted order.

This is a learning project. I built it to get hands-on with JPA (Jakarta Persistence API) relationship
mapping and Spring Data REST rather than to run in production, and I've called out the shortcuts
below instead of hiding them.

---

## Highlights

- **Seven JPA entities with a full relational graph**, including a many-to-many join table
  (`excursion_cartitem`) between cart items and excursions. Getting that relationship to actually
  persist was the hardest part of the project and took several passes.
- **Most of the API is generated, not written.** Seven repository interfaces extend `JpaRepository`
  and Spring Data REST turns each one into a paginated HAL (Hypertext Application Language) resource
  with full CRUD (Create, Read, Update, Delete) and navigable association links. There are no
  hand-written controllers for those — the repository interfaces are literally one line each.
- **One hand-written controller where the generated API wasn't enough.** Checkout has to assemble a
  customer, a cart, and a set of cart items in a single transaction and hand back a tracking number,
  which isn't something you can express as CRUD on a single resource. That's `POST /api/checkout/purchase`.
- **Checkout is transactional and generates a UUID order tracking number** so the front end has
  something to show the user immediately after purchase.
- **Non-default Spring Data REST config**: entity IDs are exposed in JSON payloads (off by default),
  and pagination is effectively disabled so the front end can pull whole collections in one request.

---

## Tech stack

| | |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.4.5 |
| Persistence | Spring Data JPA / Hibernate |
| API layer | Spring Data REST |
| Database | MySQL 8 |
| Boilerplate | Lombok 1.18.36 |
| Build | Maven (wrapper included) |

There's no Spring Security, no Flyway/Liquibase, and no Docker setup — see
[Limitations](#limitations-and-known-rough-edges).

---

## Running it

### 1. Prerequisites

- Java 17 (JDK)
- MySQL 8 running on `localhost:3306`

Maven is not required — the repo includes the Maven wrapper (`./mvnw`).

### 2. Create the database and user

The app connects as user `ecommerceapp` to a schema named `full-stack-ecommerce`:

```sql
CREATE DATABASE `full-stack-ecommerce`;
CREATE USER 'ecommerceapp'@'localhost' IDENTIFIED BY 'ecommerceapp';
GRANT ALL PRIVILEGES ON `full-stack-ecommerce`.* TO 'ecommerceapp'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Create the tables

**The schema is not in this repo and the app will not create it for you.** `application.properties`
sets `spring.jpa.hibernate.ddl-auto=none`, so Hibernate validates nothing and generates nothing —
the tables have to exist before startup.

You need these tables, matching the column names in `src/main/java/.../entities/`:

`countries`, `divisions`, `customers`, `carts`, `cart_items`, `vacations`, `excursions`, and the
join table `excursion_cartitem`.

The startup data loader also expects a row in `divisions` with `division_id = 42` (see step 5), so
seed `countries` and `divisions` before the first run.

### 4. Adjust connection settings if needed

Everything lives in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/full-stack-ecommerce?useSSL=false&...
spring.datasource.username=ecommerceapp
spring.datasource.password=ecommerceapp
spring.data.rest.base-path=/api
```

The credentials are committed in plain text. That's fine for a local learning project and wrong for
anything real — see [Limitations](#limitations-and-known-rough-edges).

### 5. Start it

```bash
./mvnw spring-boot:run
```

The app starts on the default port **8080**, and the API is served under **`/api`**. Hit
`http://localhost:8080/api` to get the HAL discovery document listing every exposed resource.

On every startup, `BootStrapData` (a `CommandLineRunner`) inserts five hardcoded customers. It
inserts them again on each restart — it has no "already seeded" check — so expect duplicates if you
restart repeatedly.

### 6. Front end

This is backend-only. It was built to serve an Angular app running on `http://localhost:4200`
(that's the origin allowed by the CORS config on the checkout controller), and that app is not part
of this repo.

---

## Technical walkthrough

### Domain model

```
Country 1──* Division 1──* Customer 1──* Cart 1──* CartItem *──1 Vacation
                                                      │              │
                                                      │              1
                                                      *──────────────*
                                                          Excursion
```

- **Vacation** — a vacation package: title, description, `travel_fare_price`, image URL.
- **Excursion** — an optional add-on belonging to exactly one vacation: title, price, image URL.
  So a vacation owns a list of excursions, and you can only pick excursions offered by the vacation
  you're buying.
- **CartItem** — one vacation in a cart, plus the set of excursions chosen for it. The
  vacation-to-cart-item link is many-to-one; the excursion-to-cart-item link is many-to-many through
  `excursion_cartitem`, because the same excursion can appear in many different people's cart items.
- **Cart** — the order itself: `order_tracking_number`, `package_price`, `party_size`, and a
  `status` enum (`pending`, `ordered`, `canceled`) persisted as a string via `@Enumerated(EnumType.STRING)`
  rather than as an ordinal int, so the values stay readable in the database and don't break if I
  reorder the enum.
- **Customer** — name, address, postal code, phone, and a link to a Division.
- **Country / Division** — location reference data (e.g. country → state/province), used to
  normalize customer addresses instead of storing free-text location strings on the customer.

Every entity carries `create_date` and `last_update` columns managed by Hibernate's
`@CreationTimestamp` and `@UpdateTimestamp`, so I never set audit timestamps by hand.

Lombok's `@Getter`/`@Setter`/`@NoArgsConstructor`/`@AllArgsConstructor` handle the boilerplate. JPA
requires a no-arg constructor, which is why `@NoArgsConstructor` is on most entities.

### Why Spring Data REST instead of hand-written controllers

The front end needed plain CRUD over seven tables. Writing seven `@RestController` classes with five
methods each would have been ~35 methods of identical delegate-to-repository code, and every one of
them is a place to introduce a typo.

Spring Data REST reads the repository interfaces and generates the HTTP layer at runtime, including
association subresources — so `GET /api/vacations/1/excursions` works without me writing anything.
The entire persistence + API layer for a table is:

```java
@CrossOrigin
public interface VacationRepository extends JpaRepository<Vacation, Long> {}
```

The tradeoff is real: the API shape is the database shape. There's no DTO (Data Transfer Object)
layer, so entity fields are the JSON contract and a column rename is a breaking API change. There's
also nowhere to put business rules — which is exactly why checkout had to be written by hand.
For a project this size I'd make the same call again; for anything with real business logic I'd want
explicit controllers and DTOs.

### Why MySQL

The schema is genuinely relational — five foreign key relationships and a join table — and orders
need to be written atomically, so a relational database with transactions was the right fit. MySQL
specifically because it's what I was set up with locally; nothing here is MySQL-specific beyond the
dialect and driver, so Postgres would be a driver swap and a dialect change.

### Spring Data REST configuration

`RestDataConfig` implements `RepositoryRestConfigurer` and changes two defaults:

```java
config.exposeIdsFor(Country.class);
config.exposeIdsFor(Customer.class);
config.exposeIdsFor(Division.class);
config.exposeIdsFor(Excursion.class);
config.exposeIdsFor(Vacation.class);
config.setDefaultPageSize(Integer.MAX_VALUE);
config.setMaxPageSize(Integer.MAX_VALUE);
```

- **`exposeIdsFor(...)`** — by default Spring Data REST hides primary keys and expects clients to
  navigate by HAL link. The front end needed the numeric IDs in the JSON body, so I opted those five
  entities in. Note `Cart` and `CartItem` are deliberately not in that list: those are written
  through checkout, not read by ID.
- **Pagination disabled** — the front end wanted full lists (all vacations, all excursions, all
  countries) in one call rather than paging. Setting the page size to `Integer.MAX_VALUE` is the
  blunt way to do that. It's fine at reference-data scale and would fall over on a real `carts`
  table; proper pagination is the correct fix.

### Endpoints

Spring Data REST exposes each repository under the `/api` base path, pluralized from the entity name:

| Resource | Methods |
|---|---|
| `/api/vacations` | GET, POST, PUT, PATCH, DELETE |
| `/api/excursions` | GET, POST, PUT, PATCH, DELETE |
| `/api/customers` | GET, POST, PUT, PATCH, DELETE |
| `/api/countries` | GET, POST, PUT, PATCH, DELETE |
| `/api/divisions` | GET, POST, PUT, PATCH, DELETE |
| `/api/carts` | GET, POST, PUT, PATCH, DELETE |
| `/api/cartItems` | GET, POST, PUT, PATCH, DELETE |

Each collection also supports `/api/{resource}/{id}` and association subresources for the entity's
relationships, for example:

```
GET /api/vacations/1/excursions     excursions offered by a vacation
GET /api/customers/1/carts          a customer's carts
GET /api/carts/1/cartItems          items in a cart
GET /api/cartItems/1/excursions     excursions chosen for a cart item
GET /api/divisions/1/country        the country a division belongs to
```

`GET /api/profile` is the entry point to the generated ALPS (Application-Level Profile Semantics) metadata, with per-resource descriptors at `/api/profile/{resource}`.

Because every repository is annotated with a bare `@CrossOrigin`, these endpoints accept
cross-origin requests from **any** origin. That was convenience during development, not a decision
I'd ship.

### The one hand-written endpoint: checkout

```
POST /api/checkout/purchase
```

Request body (`PurchaseData`): a `customer`, a `cart`, and a set of `cartItems`.
Response (`PurchaseResponse`): `{ "orderTrackingNumber": "<uuid>" }`.

`CheckoutServicesImpl.checkout()` is annotated `@Transactional` and does the following in order:

1. **Null out the incoming cart ID.** The Angular front end sends `id: 0` for a new cart, which JPA
   would interpret as "update cart 0" rather than "insert." Setting it to `null` forces MySQL's
   `AUTO_INCREMENT` to assign a real one. This is commented inline in the code because it's the kind
   of thing that looks like a mistake six months later.
2. **Generate a tracking number** — `UUID.randomUUID().toString()`, set on the cart.
3. **Wire up both sides of the relationships.** `Cart.add(CartItem)` and `Customer.add(Cart)` are
   helper methods on the entities that set the child's back-reference at the same time they add to
   the parent's collection. Skipping that back-reference is what caused the foreign keys to come out
   null the first time I wrote this.
4. **Set the cart status to `ordered`.**
5. **Save customer, then cart, then all cart items**, in that order, so parent rows exist before the
   children that reference them.

The whole thing is one transaction, so a failure partway through doesn't leave an order half-written.

`CheckoutServices` is an interface with a single implementation, and `CheckoutController` takes it
via constructor injection. That's more indirection than a project this size needs, but it keeps the
controller unaware of the persistence code.

Unlike the repositories, the checkout controller restricts CORS to a single origin:
`@CrossOrigin("http://localhost:4200")`.

---

## Limitations and known rough edges

Being straight about what's not done here:

- **No schema in the repo.** `ddl-auto=none` with no `schema.sql` and no migration tool means you
  can't clone this and run it — you have to reconstruct the tables from the entity classes. Adding
  Flyway is the single highest-value fix.
- **Database credentials are committed** in `application.properties` instead of coming from
  environment variables or a secrets store.
- **No authentication or authorization.** Spring Security isn't on the classpath, so every endpoint,
  including `DELETE /api/customers/1`, is open to anyone who can reach the port.
- **No request validation.** `Customer` has `nullable = false, length = 50` column constraints,
  which are database-level, not application-level. There's no Bean Validation
  (`@NotBlank`, `@Size`) and no `spring-boot-starter-validation`, so bad input fails as a database
  error rather than a clean 400.
- **No error handling on checkout.** There's no `@ControllerAdvice` and no validation of the
  purchase payload, so a malformed request surfaces as a stack trace.
- **The seed data loader is not idempotent** and hardcodes `division_id = 42`. It duplicates five
  customers on every restart and throws if division 42 doesn't exist. It should be behind a Spring
  profile at minimum.
- **`Division` maps `country_id` twice** — once as a read-only `@ManyToOne` association
  (`insertable = false, updatable = false`) and once as a raw `Long` column. Relatedly,
  `Country.divisions` uses `mappedBy = "country_id"`, pointing at that raw column rather than at the
  `country` association. It works for the reads the front end does, but it's not how the mapping
  should be expressed and it's on my list to clean up.
- **Both sides of the CartItem↔Excursion many-to-many declare `@JoinTable`** on `excursion_cartitem`
  instead of one side using `mappedBy`. That means neither side is the inverse side and Hibernate
  can attempt to write the same join row from both directions.
- **Cart totals are not calculated server-side.** `package_price` is whatever the client sends,
  which means the client decides the price. Real checkout would recompute it from the vacation and
  excursion prices on the server.
- **Tests are effectively absent.** `EcommerceappApplicationTests` only asserts that the Spring
  context loads, and even that needs a live MySQL instance. Checkout — the only real logic in the
  project — has no test coverage.

## Project layout

```
src/main/java/me/wsehsatnoen/ecommerceapp/
├── EcommerceappApplication.java   entry point
├── bootstrap/BootStrapData.java   CommandLineRunner seed data
├── config/RestDataConfig.java     Spring Data REST customization
├── controllers/                   CheckoutController (the only controller)
├── dao/                           7 JpaRepository interfaces
├── entities/                      7 JPA entities + StatusType enum
└── services/                      checkout service, request/response objects
```

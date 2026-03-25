# Online Food Delivery System - Microservices Architecture

## Overview
This is a complete **service-based microservices architecture** for an Online Food Delivery Platform built with Spring Boot, MariaDB, and ReactJS.

## Architecture Components

### 1. **User Service** (Port: 8081)
Manages user authentication, profiles, and user-related operations.

**Database:** `user_db`

**Key Endpoints:**
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/email/{email}` - Get user by email
- `GET /api/users` - Get all users
- `GET /api/users/type/{userType}` - Get users by type
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

**User Types:** customer, restaurant_owner, delivery_person

---

### 2. **Restaurant Service** (Port: 8082)
Manages restaurants and their menu items (dishes).

**Database:** `restaurant_db`

**Entities:**
- `Restaurant` - Restaurant information
- `Dish` - Menu items

**Key Endpoints:**

**Restaurants:**
- `GET /api/restaurants/{id}` - Get restaurant by ID
- `GET /api/restaurants` - Get all active restaurants
- `GET /api/restaurants/owner/{ownerId}` - Get restaurants by owner
- `POST /api/restaurants` - Create restaurant
- `PUT /api/restaurants/{id}` - Update restaurant
- `DELETE /api/restaurants/{id}` - Delete restaurant

**Dishes:**
- `GET /api/dishes/{id}` - Get dish by ID
- `GET /api/dishes/restaurant/{restaurantId}` - Get dishes by restaurant
- `GET /api/dishes/restaurant/{restaurantId}/available` - Get available dishes
- `GET /api/dishes/category/{category}` - Get dishes by category
- `POST /api/dishes` - Create dish
- `PUT /api/dishes/{id}` - Update dish
- `DELETE /api/dishes/{id}` - Delete dish

---

### 3. **Order Service** (Port: 8083)
Manages customer orders and order tracking.

**Database:** `order_db`

**Entities:**
- `Order` - Order information
- `OrderItem` - Individual items in an order

**Key Endpoints:**

**Orders:**
- `GET /api/orders/{id}` - Get order by ID
- `GET /api/orders/customer/{customerId}` - Get orders by customer
- `GET /api/orders/restaurant/{restaurantId}` - Get orders by restaurant
- `GET /api/orders/status/{status}` - Get orders by status
- `POST /api/orders` - Create order
- `PUT /api/orders/{id}` - Update order status
- `DELETE /api/orders/{id}` - Cancel order

**Order Items:**
- `GET /api/order-items/{id}` - Get order item
- `GET /api/order-items/order/{orderId}` - Get items in order
- `POST /api/order-items` - Create order item
- `PUT /api/order-items/{id}` - Update order item
- `DELETE /api/order-items/{id}` - Remove order item

**Order Status Values:** pending, confirmed, preparing, ready, delivering, delivered, cancelled

---

### 4. **Frontend (ReactJS)**
Modern React application for user interaction.

**Key Features:**
- Restaurant listing
- Menu browsing
- Order placement
- Responsive design

**Main Components:**
- `RestaurantList` - Display all restaurants
- `Menu` - Show dishes for selected restaurant
- `OrderForm` - Order placement form
- `apiService` - Axios-based API client

---

## Technology Stack

| Component | Technology |
|-----------|-----------|
| Backend Framework | Spring Boot 2.7.0 |
| Database | MariaDB 10.3+ |
| Frontend | ReactJS 18.2 |
| Build Tool | Maven |
| API Client | Axios |
| Routing | React Router v6 |

---

## Project Structure

```
food-delivery-system/
├── pom.xml                          # Parent POM
├── user-service/                    # User microservice
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/fooddelivery/user/
│       │   ├── UserServiceApplication.java
│       │   ├── entity/User.java
│       │   ├── repository/UserRepository.java
│       │   ├── service/UserService.java
│       │   ├── controller/UserController.java
│       │   └── dto/UserDTO.java
│       └── resources/application.yml
│
├── restaurant-service/              # Restaurant microservice
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/fooddelivery/restaurant/
│       │   ├── RestaurantServiceApplication.java
│       │   ├── entity/
│       │   │   ├── Restaurant.java
│       │   │   └── Dish.java
│       │   ├── repository/
│       │   │   ├── RestaurantRepository.java
│       │   │   └── DishRepository.java
│       │   ├── service/
│       │   │   ├── RestaurantService.java
│       │   │   └── DishService.java
│       │   ├── controller/
│       │   │   ├── RestaurantController.java
│       │   │   └── DishController.java
│       │   └── dto/
│       │       ├── RestaurantDTO.java
│       │       └── DishDTO.java
│       └── resources/application.yml
│
├── order-service/                   # Order microservice
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/fooddelivery/order/
│       │   ├── OrderServiceApplication.java
│       │   ├── entity/
│       │   │   ├── Order.java
│       │   │   └── OrderItem.java
│       │   ├── repository/
│       │   │   ├── OrderRepository.java
│       │   │   └── OrderItemRepository.java
│       │   ├── service/
│       │   │   ├── OrderService.java
│       │   │   └── OrderItemService.java
│       │   ├── controller/
│       │   │   ├── OrderController.java
│       │   │   └── OrderItemController.java
│       │   └── dto/
│       │       ├── OrderDTO.java
│       │       └── OrderItemDTO.java
│       └── resources/application.yml
│
├── frontend/                        # React frontend
│   ├── package.json
│   ├── public/
│   │   └── index.html
│   └── src/
│       ├── App.jsx
│       ├── App.css
│       ├── index.js
│       ├── index.css
│       ├── services/
│       │   └── apiService.js
│       └── components/
│           ├── RestaurantList.jsx
│           ├── RestaurantList.css
│           ├── Menu.jsx
│           ├── Menu.css
│           ├── OrderForm.jsx
│           └── OrderForm.css
│
└── database/
    ├── schema.sql                   # Database schemas
    └── sample-data.sql              # Sample test data
```

---

## Setup Instructions

### Prerequisites
- Java 11 or higher
- Apache Maven 3.6+
- MariaDB 10.3+
- Node.js 14+ and npm

### 1. Create Databases

```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < database/sample-data.sql
```

### 2. Build Backend Services

```bash
# Parent project
mvn clean install

# Individual services (optional)
cd user-service && mvn clean package
cd ../restaurant-service && mvn clean package
cd ../order-service && mvn clean package
```

### 3. Run Backend Services

```bash
# Terminal 1 - User Service
cd user-service
mvn spring-boot:run

# Terminal 2 - Restaurant Service
cd restaurant-service
mvn spring-boot:run

# Terminal 3 - Order Service
cd order-service
mvn spring-boot:run
```

### 4. Setup Frontend

```bash
cd frontend
npm install
npm start
```

The application will open at `http://localhost:3000`

---

## API Communication Flow

```
┌──────────────────┐
│   React App      │
│  (Port 3000)     │
└────────┬─────────┘
         │
    ┌────┴──────────────┬──────────────────┬─────────────────┐
    │                   │                  │                 │
    ▼                   ▼                  ▼                 ▼
┌─────────────┐  ┌──────────────┐  ┌──────────────┐  ┌────────────────┐
│User Service │  │Restaurant    │  │Order Service │  │MariaDB         │
│(8081)       │  │Service(8082) │  │(8083)        │  │                │
├─────────────┤  ├──────────────┤  ├──────────────┤  ├────────────────┤
│  user_db    │  │restaurant_db │  │  order_db    │  │ 3 Databases    │
└─────────────┘  └──────────────┘  └──────────────┘  └────────────────┘
```

---

## Database Configuration

Each service has its own database and datasource configured in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mariadb://localhost:3306/{service_db}
    username: root
    password: root
```

**Databases:**
- `user_db` - User information
- `restaurant_db` - Restaurants and dishes
- `order_db` - Orders and order items

---

## API Example Requests

### Create a User
```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newuser@example.com",
    "password": "password123",
    "fullName": "John User",
    "userType": "customer"
  }'
```

### Get All Restaurants
```bash
curl http://localhost:8082/api/restaurants
```

### Create an Order
```bash
curl -X POST http://localhost:8083/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "restaurantId": 1,
    "totalPrice": 500.00,
    "deliveryAddress": "123 Main St",
    "items": [
      {"dishId": 1, "quantity": 2, "price": 299.00}
    ]
  }'
```

---

## Features

✅ **User Management**
- User registration and profile management
- Multiple user types (customer, restaurant owner, delivery person)
- Email-based user lookup

✅ **Restaurant Management**
- Restaurant creation and management
- Menu management with multiple categories
- Dish availability tracking
- Rating system

✅ **Order Management**
- Complete order lifecycle (pending → delivered)
- Order item tracking
- Multiple order statuses
- Delivery address management

✅ **Frontend UI**
- Restaurant browsing
- Menu exploration
- Order placement
- Responsive design
- Real-time API communication

---

## Scaling Considerations

This microservices architecture allows:

1. **Independent Scaling** - Each service can be scaled independently
2. **Dedicated Databases** - Each service maintains its own data store
3. **Loose Coupling** - Services communicate via REST APIs
4. **Easy Deployment** - Each service can be deployed separately

---

## Future Enhancements

- [ ] API Gateway for unified endpoint
- [ ] Authentication/Authorization (JWT)
- [ ] Service discovery (Eureka)
- [ ] Inter-service communication (Feign client)
- [ ] Cache layer (Redis)
- [ ] Message queue (Kafka)
- [ ] Real-time notifications (WebSocket)
- [ ] Payment integration
- [ ] Admin dashboard

---

## Troubleshooting

### Port Already in Use
```bash
# Find process using port
lsof -i :8081
# Kill process
kill -9 <PID>
```

### Database Connection Failed
- Verify MariaDB is running
- Check credentials in application.yml
- Ensure databases are created

### Frontend API Errors
- Verify backend services are running
- Check CORS configuration
- Verify API endpoints in apiService.js

---

## License
MIT License

## Author
Food Delivery System - Microservices Architecture

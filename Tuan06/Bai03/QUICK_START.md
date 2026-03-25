# Quick Start Guide

## 5-Minute Setup

### Step 1: Database Setup (2 min)
```bash
# Open MariaDB
mysql -u root -p

# Execute schema and sample data
source database/schema.sql
source database/sample-data.sql

# Verify databases
SHOW DATABASES;
```

### Step 2: Start Backend Services (2 min)

**Terminal 1:**
```bash
cd user-service
mvn spring-boot:run
# Service will start at http://localhost:8081
```

**Terminal 2:**
```bash
cd restaurant-service
mvn spring-boot:run
# Service will start at http://localhost:8082
```

**Terminal 3:**
```bash
cd order-service
mvn spring-boot:run
# Service will start at http://localhost:8083
```

### Step 3: Start Frontend (1 min)

**Terminal 4:**
```bash
cd frontend
npm install
npm start
# Frontend opens at http://localhost:3000
```

---

## API Testing

### Test User Service
```bash
curl http://localhost:8081/api/users
```

### Test Restaurant Service
```bash
curl http://localhost:8082/api/restaurants
```

### Test Order Service
```bash
curl http://localhost:8083/api/orders
```

---

## Key Credentials

**Database:**
- Username: `root`
- Password: `root`

**Sample Users:**
- Email: `customer@example.com` (Customer)
- Email: `owner@restaurant.com` (Restaurant Owner)
- Email: `delivery@example.com` (Delivery Person)

---

## Sample Test Data

**Restaurants:**
- Pizza Palace (ID: 1)
- Burger Barn (ID: 2)
- Sushi Paradise (ID: 3)

**Users:**
- John Doe (ID: 1, Customer)
- Jane Smith (ID: 2, Owner)
- Alice Johnson (ID: 4, Customer)

**Sample Order:**
- Order ID: 1 (Delivered)
- Customer: John Doe
- Restaurant: Pizza Palace
- Total: ₹647

---

## Ports Reference

| Service | Port | URL |
|---------|------|-----|
| User Service | 8081 | http://localhost:8081 |
| Restaurant Service | 8082 | http://localhost:8082 |
| Order Service | 8083 | http://localhost:8083 |
| Frontend (React) | 3000 | http://localhost:3000 |
| MariaDB | 3306 | localhost:3306 |

---

## Common Commands

### View Service Logs
```bash
# User Service
tail -f user-service/logs/spring.log

# Check if service is running
curl http://localhost:8081/api/users
```

### Stop Services
```bash
# Press Ctrl+C in each terminal
```

### Build All Services
```bash
mvn clean install
```

### Build Individual Service
```bash
cd user-service
mvn clean package
java -jar target/user-service-1.0.0.jar
```

---

## Troubleshooting

### Port Already in Use
```bash
# Find what's using the port
lsof -i :8081

# Kill the process
kill -9 <PID>
```

### Database Not Responding
```bash
# Check if MariaDB is running
sudo service mysql status

# Start MariaDB
sudo service mysql start
```

### npm Dependencies Issue
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
```

---

## Next Steps

1. ✅ Setup complete!
2. Open http://localhost:3000 in browser
3. Browse restaurants
4. Select a restaurant to view menu
5. Place an order
6. Check order status

---

## File Structure Quick Reference

```
bai3/
├── user-service/        → User management (Port 8081)
├── restaurant-service/  → Restaurants & Menus (Port 8082)
├── order-service/       → Orders & Tracking (Port 8083)
├── frontend/            → React App (Port 3000)
├── database/
│   ├── schema.sql       → Create databases
│   └── sample-data.sql  → Test data
├── README.md            → Full documentation
├── DEPLOYMENT.md        → Production setup
└── QUICK_START.md       → This file
```

---

## API Examples

### Create a User
```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "fullName": "Test User",
    "userType": "customer"
  }'
```

### Get All Restaurants
```bash
curl http://localhost:8082/api/restaurants
```

### Get Menu Items
```bash
curl http://localhost:8082/api/dishes/restaurant/1
```

### Create Order
```bash
curl -X POST http://localhost:8083/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "restaurantId": 1,
    "totalPrice": 500.00,
    "deliveryAddress": "123 Street",
    "items": [
      {
        "dishId": 1,
        "quantity": 2,
        "price": 299.00
      }
    ]
  }'
```

---

## Features Overview

### 🔐 User Service
- User registration
- Profile management
- Multiple user types
- User lookup by email

### 🍽️ Restaurant Service
- Restaurant management
- Menu creation
- Dish availability control
- Category-based filtering

### 📦 Order Service
- Order placement
- Order tracking
- Status management
- Order history

### 💻 Frontend
- Restaurant browsing
- Menu exploration
- Order placement
- Responsive UI

---

## Need Help?

- Check **README.md** for detailed documentation
- Check **DEPLOYMENT.md** for production setup
- Review **database/schema.sql** for database structure
- Check application logs in each service

---

**You're all set! Happy coding! 🎉**

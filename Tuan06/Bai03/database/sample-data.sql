-- ============================================
-- FOOD DELIVERY SYSTEM - SAMPLE DATA
-- For shared database: food_delivery_db
-- ============================================

USE food_delivery_db;

-- Sample Users: customer, restaurant_owner, delivery_person
INSERT INTO users (email, password, full_name, phone, address, user_type, active) VALUES
('customer@example.com', 'password123', 'John Doe', '9876543210', '123 Main St', 'customer', TRUE),
('owner@restaurant.com', 'ownerpass', 'Jane Smith', '9876543211', '456 Business Ave', 'restaurant_owner', TRUE),
('delivery@example.com', 'deliverypass', 'Bob Wilson', '9876543212', '789 Delivery Rd', 'delivery_person', TRUE),
('customer2@example.com', 'password456', 'Alice Johnson', '9876543213', '321 Park Ave', 'customer', TRUE);

-- Sample Restaurants (owner_id references users table)
INSERT INTO restaurants (name, description, address, phone, rating, owner_id, active) VALUES
('Pizza Palace', 'Delicious Italian pizzas', '456 Business Ave', '0123456789', 4.5, 2, TRUE),
('Burger Barn', 'Classic American burgers', '789 Food Street', '0123456790', 4.2, 2, TRUE),
('Sushi Paradise', 'Fresh Japanese sushi', '101 Taste Ave', '0123456791', 4.8, 2, TRUE);

INSERT INTO dishes (name, description, price, category, restaurant_id, available) VALUES
-- Pizza Palace
('Margherita Pizza', 'Classic tomato, mozzarella, basil', 299.00, 'Pizza', 1, TRUE),
('Pepperoni Pizza', 'Pepperoni and cheese', 349.00, 'Pizza', 1, TRUE),
('Garlic Bread', 'Fresh garlic bread', 99.00, 'Appetizer', 1, TRUE),

-- Burger Barn
('Classic Burger', 'Beef patty with all toppings', 249.00, 'Burger', 2, TRUE),
('Cheese Burger', 'Double cheese burger', 299.00, 'Burger', 2, TRUE),
('French Fries', 'Crispy golden fries', 99.00, 'Sides', 2, TRUE),

-- Sushi Paradise
('California Roll', 'Crab, avocado, cucumber', 350.00, 'Sushi', 3, TRUE),
('Salmon Sushi', 'Fresh salmon and rice', 400.00, 'Sushi', 3, TRUE),
('Miso Soup', 'Traditional Japanese soup', 149.00, 'Soup', 3, TRUE);

-- Sample Orders (customer_id and restaurant_id reference users and restaurants tables)
INSERT INTO orders (customer_id, restaurant_id, total_price, delivery_address, order_status, notes) VALUES
(1, 1, 647.00, '123 Main St, City', 'delivered', 'Extra cheese please'),
(4, 2, 548.00, '321 Park Ave, City', 'pending', 'No onions'),
(1, 3, 900.00, '123 Main St, City', 'confirmed', 'Fresh ingredients');

INSERT INTO order_items (dish_id, quantity, price, order_id) VALUES
(1, 1, 299.00, 1),
(3, 1, 99.00, 1),
(5, 2, 249.00, 1),
(4, 2, 249.00, 2),
(6, 1, 99.00, 2),
(7, 1, 350.00, 3),
(8, 1, 400.00, 3),
(9, 1, 149.00, 3);

-- USERS
INSERT INTO users (id, username, password) VALUES
(1, 'admin', '$2a$10$7s8gZlCz0xg7y9hHc8vM7uK2UQHn1x0z2xQkq9J7mJ3Zz5o6z6p8m'),
(2, 'user',  '$2a$10$7s8gZlCz0xg7y9hHc8vM7uK2UQHn1x0z2xQkq9J7mJ3Zz5o6z6p8m');

-- STORES
INSERT INTO store (id, store_name, address, region, active) VALUES
(1, 'Chennai Central', 'Chennai', 'South', true),
(2, 'Bangalore Hub', 'Bangalore', 'South', true);

-- PRODUCTS
INSERT INTO product (id, name, price) VALUES
(1, 'Rice', 50.0),
(2, 'Wheat', 45.0);

-- INVENTORY LEVELS
INSERT INTO inventory_levels (id, product_id, store_id, quantity) VALUES
(1, 1, 1, 120),
(2, 1, 2, 40),
(3, 2, 1, 200);

-- DEMAND FORECAST
INSERT INTO demand_forecast (id, product_id, store_id, forecast_date, predicted_demand, confidence_score) VALUES
(1, 1, 1, CURRENT_DATE, 150, 0.85),
(2, 1, 2, CURRENT_DATE, 90, 0.78);

-- TRANSFER SUGGESTIONS
INSERT INTO transfer_suggestion (id, product_id, source_store_id, target_store_id, quantity, priority, status, suggested_at) VALUES
(1, 1, 1, 2, 50, 'HIGH', 'PENDING', CURRENT_TIMESTAMP);

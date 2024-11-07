DROP TABLE IF EXISTS coupons;
/* Both columns discount and minBasketValue have been updated from NUMBER type, to DECIMAL, this was a decision made in order for it to work properly with H2. */
CREATE TABLE coupons (
  id INT AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR(250) NOT NULL,
  discount DECIMAL(10,2) NOT NULL,
  minBasketValue DECIMAL(10,2) DEFAULT NULL
);

INSERT INTO coupons (code, discount, minBasketValue) VALUES
  ('TEST1', 10.00, 50.00),
  ('TEST2', 15.00, 100.00),
  ('TEST3', 20.00, 200.00);

# DLR_WS_FinalProject
Web Service for Java 3 Final Project

### Features:

    - Complete RESTful API deployed to an Apache Tomcat server 
    - Get customer data (name, email, phone, address)
    - Get Admin data (name, email, branch)
    - Get product info (name, description, price)
    - Get orders (products, price of each, total, customer address)
    - Get Inventory (products, quantity in each branch)
    - Add/edit/delete Customer (users, customers, address, passwords)
    - Add/edit/delete Admin (users, admins, passwords)
    - Add/edit/delete Orders (orders, product_order)
    - Add/edit/delete Product (products, inventory)

---

### API Endpoints

Base URL: `http://web-service.alexjreyes.com:8080/Ciscoware_WS-1.0/`

#### Orders `/orders`

`GET` Get all orders

```http://web-service.alexjreyes.com:8080/Ciscoware_WS-1.0/orders/```

`GET` Get order by order ID

```http://web-service.alexjreyes.com:8080/Ciscoware_WS-1.0/orders/{id}```

`GET` Get order by user ID

```http://web-service.alexjreyes.com:8080/Ciscoware_WS-1.0/orders/user/{id}```

`POST` Add order

```http://web-service.alexjreyes.com:8080/Ciscoware_WS-1.0/orders/```

`PUT` Update order by ID

```http://web-service.alexjreyes.com:8080/Ciscoware_WS-1.0/orders/{id}```

`DELETE` Delete order by ID

```http://web-service.alexjreyes.com:8080/Ciscoware_WS-1.0/orders/{id}```


#### Branches `/branches`

`GET` Get all branches

```http://web-service.alexjreyes.com:8080/Ciscoware_WS-1.0/branches/```

`GET` Get branch by ID

```http://web-service.alexjreyes.com:8080/Ciscoware_WS-1.0/branches/{id}```

`POST` Add new branch

```http://web-service.alexjreyes.com:8080/Ciscoware_WS-1.0/branches/```

`PUT` Update branch by ID

```http://web-service.alexjreyes.com:8080/Ciscoware_WS-1.0/branches/{id}```

#### Inventory `/inventory`

`GET` Get all inventory 

```http://web-service.alexjreyes.com:8080/Ciscoware_WS-1.0/inventory/```

`GET` Get inventory by Product ID

```http://web-service.alexjreyes.com:8080/Ciscoware_WS-1.0/inventory/{id}```

`POST` Add new inventory entry 

```http://web-service.alexjreyes.com:8080/Ciscoware_WS-1.0/inventory/```

`PUT` Update inventory by ID

```http://web-service.alexjreyes.com:8080/Ciscoware_WS-1.0/inventory/{id}```

`DELETE` Delete inventory by ID

```http://web-service.alexjreyes.com:8080/Ciscoware_WS-1.0/inventory/{id}```

#### Products `/products`

`GET` Get all products 

```http://web-service.alexjreyes.com:8080/Ciscoware_WS-1.0/products/```

`GET` Get product by ID

```http://web-service.alexjreyes.com:8080/Ciscoware_WS-1.0/products/{id}```

`POST` Add new product

```http://web-service.alexjreyes.com:8080/Ciscoware_WS-1.0/products/```

`PUT` Update product by ID

```http://web-service.alexjreyes.com:8080/Ciscoware_WS-1.0/products/{id}```

`DELETE` Delete product by ID

```http://web-service.alexjreyes.com:8080/Ciscoware_WS-1.0/products/{id}```

#### Customers `/customers`

#### Admins `/admins`

---

### Create/Edit JSON structure examples `POST`/`PUT`

Admin

```js
{
	"firstName": "afirst",
	"lastName": "aLast",
	"email": "POSTemail@test.com",
	"role": "admin",
	"phoneNumber": "777-3213",
	"password": "adminPassword",
	"branchId": "1"
}
```

Customer

```js
{
    "email": "POST@email.com",
    "firstName": "fPOST",
    "lastName": "lPOST",
    "phoneNumber": "654-7654",
    "city": "Quezon City",
    "country": "Philippines",
    "province": "NCR",
    "street": "123 Post Avenue",
    "zipCode": "1259",
    "password": "postpass",
    "role": "customer"
}
```

Product

```js
{
    "description": "Networking cable",
    "name": "CAT5 16\" 1GB",
    "price": "215.5"
}
```

#### Inventory

```js
{
    "productId": 1,
    "quantity": 250,
    "branchId": 3
}
```

#### Order (Must be created before adding productOrders)

```js
{
    "customerId": "2",
    "branchId": "1",
    "totalCost": "150.25"
}
```

#### ProductOrder (Must create a productOrder for each product of an order)

```js
{
	"productId": 1,
	"orderId": 3,
	"quantity": 3
}
```


---

### SQL Queries

#### Customer Data

```sql
    SELECT 
    u.id,
    u.last_name,
    u.first_name,
    u.email,
    u.role,
    c.phone_number,
    ca.street,
    ca.city,
    ca.province,
    ca.country,
    ca.zip_code
    FROM customers c
    LEFT JOIN
    users u
    ON c.user_id = u.id
    INNER JOIN
    customer_address ca
    ON ca.customer_id = c.id;
```


#### INSERT Customer

```sql
INSERT INTO users(email, first_name, last_name, role)
    VALUES ('[email@domain.com]', '[first-name]', '[last-name]', 'customer');

INSERT INTO customers(user_id, phone_number)
    VALUES (LAST_INSERT_ID(), '[phone]');

INSERT INTO customer_address(customer_id, street, city, province, zip_code, country)
    VALUES (
            LAST_INSERT_ID(),
            '[street]',
            '[city]',
            '[province]',
            '[zip]',
            '[country]'
           );

INSERT INTO passwords(user_id, content)
    VALUES (
            (SELECT c.user_id FROM customers c
             LEFT JOIN customer_address ca
             ON ca.customer_id = c.id
             WHERE ca.id = LAST_INSERT_ID()),
            '[password]'
           );
```


#### Admin Data

```sql
SELECT 
    u.id,
    u.last_name,
    u.first_name,
    u.email,
    u.role,
    b.name,
    ba.street,
    ba.city,
    ba.province,
    ba.country,
    ba.zip_code
FROM admin a
LEFT JOIN users u
ON a.user_id = u.id
INNER JOIN branches b
ON a.branch_id = b.id
INNER JOIN branch_address ba
ON ba.branch_id = b.id;
```


#### INSERT Admin

```sql
INSERT INTO users(email, first_name, last_name, role)
    VALUES ('[email@domain.com]', '[first-name]', '[last-name]', 'admin');

INSERT INTO admin(user_id, branch_id)
    VALUES (LAST_INSERT_ID(), 1);

INSERT INTO passwords(user_id, content)
    VALUES (LAST_INSERT_ID(), '[password]'); 
```


#### User logins

```sql
SELECT
    u.id,
    u.email,
    p.content
FROM passwords p
LEFT JOIN users u
ON p.user_id = u.id;
```

#### All Branches

```sql
SELECT
    b.id,
    b.name,
    ba.street, 
    ba.city, 
    ba.province,
    ba.country,
    ba.zip_code
    FROM branches b
    INNER JOIN branch_address ba
    ON ba.branch_id = b.id;
```


#### Orders data

```sql
SELECT 
    o.id,
    o.order_date,
    o.delivery_date,
    o.total_cost,
    p.name,
    p.description,
    po.quantity,
    p.price,
    c.user_id,
    u.email,
    c.phone_number
    FROM orders o
    LEFT JOIN product_order po
    ON po.order_id = o.id
    LEFT JOIN products p
    ON po.product_id = p.id
    LEFT JOIN customers c
    ON o.customer_id = c.id
    LEFT JOIN users u
    ON c.user_id = u.id;
```

#### INSERT Order

```sql
INSERT INTO orders(customer_id, branch_id, order_date, delivery_date, status)
    VALUES (1, 2, '2000-01-01 00:00:00', '2000-01-04', 'pending');

INSERT INTO product_order(product_id, order_id, quantity)
    VALUE (2, LAST_INSERT_ID(), 7);
```


#### Inventory data

```sql
SELECT
    i.product_id,
    p.name,
    p.description,
    p.price,
    i.quantity,
    i.branch_id,
    b.name
    FROM inventory i
    LEFT JOIN products p
    ON i.product_id = p.id
    LEFT JOIN branches b
    ON i.branch_id = b.id;
```


#### INSERT Product

```sql
INSERT INTO products(description, name, price)
    VALUES ('WIFI Router', 'TP-LINK TS110 5GHZ', 400.50);

INSERT INTO inventory(product_id, quantity, branch_id)
    VALUES (LAST_INSERT_ID(), 95, 1);
```

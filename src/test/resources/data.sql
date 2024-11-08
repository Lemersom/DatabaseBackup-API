INSERT INTO Customers (CustomerID, Name, BirthDate, Email, IsActive) VALUES
(1, 'Alice Johnson', '1990-05-24', 'alice.j@example.com', TRUE),
(2, 'Bob Smith', '1985-09-12', 'bob.smith@example.com', FALSE),
(3, 'Carlos Lopez', '2001-03-15', 'carlos.lopez@example.com', TRUE),
(4, 'Diana Prince', '1995-11-30', 'diana.prince@example.com', TRUE);

INSERT INTO Orders (OrderID, CustomerID, OrderDate, TotalAmount, Status) VALUES
(101, 1, '2024-10-14 10:30:00', 250.50, 'Completed'),
(102, 2, '2024-10-15 14:20:00', 75.99, 'Pending'),
(103, 3, '2024-10-15 16:45:00', 150.75, 'Shipped');

INSERT INTO Products (ProductID, Name, Description, Price, InStock, AddedDate) VALUES
(201, 'Laptop', 'High-performance laptop', 1200.99, TRUE, '2024-01-10'),
(202, 'Headphones', 'Wireless headphones', 99.95, TRUE, '2024-06-20'),
(203, 'Mouse', 'Ergonomic mouse', 35.00, FALSE, '2023-08-10'),
(204, 'Keyboard', 'Mechanical keyboard', 79.50, TRUE, '2024-03-05');

INSERT INTO OrderItems (OrderItemID, OrderID, ProductID, Quantity, UnitPrice) VALUES
(301, 101, 201, 1, 1200.99),
(302, 101, 202, 2, 99.95),
(303, 102, 203, 1, 35.00),
(304, 103, 204, 1, 79.50);

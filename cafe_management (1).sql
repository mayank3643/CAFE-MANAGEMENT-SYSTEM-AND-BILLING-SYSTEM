-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 20, 2026 at 03:33 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cafe_management`
--

-- --------------------------------------------------------

--
-- Table structure for table `billing`
--

CREATE TABLE `billing` (
  `bill_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `bill_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `amount` decimal(10,2) NOT NULL,
  `payment_method` varchar(30) NOT NULL,
  `payment_status` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `billing`
--

INSERT INTO `billing` (`bill_id`, `order_id`, `bill_date`, `amount`, `payment_method`, `payment_status`) VALUES
(1, 1, '2026-11-19 17:50:49', 298.00, 'UPI', 'Paid'),
(2, 2, '2026-11-19 17:50:49', 179.00, 'Cash', 'Pending'),
(3, 3, '2026-11-19 17:50:49', 1247.00, 'Card', 'Paid'),
(4, 4, '2026-11-19 17:50:49', 258.00, 'UPI', 'Paid');

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `customer_id` int(11) NOT NULL,
  `customer_name` varchar(100) NOT NULL,
  `mobile` varchar(15) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `city` varchar(60) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`customer_id`, `customer_name`, `mobile`, `email`, `city`, `address`) VALUES
(1, 'yash', '9874563211', 'xyz@gmail.com', 'rajkot', 'surat'),
(2, 'jeel', '7854333333', 'abc@gmaIi.com', 'atkot', 'atkot ak food mall'),
(3, 'preet', '7202546214', 'efg@gmail.com', 'ahm', 'gota'),
(4, 'Aarav Shah', '9876543210', 'aarav@gmail.com', 'Rajkot', 'Kalawad Road'),
(5, 'Riya Patel', '9876543211', 'riya@gmail.com', 'Rajkot', 'University Road'),
(6, 'Krish Mehta', '9876543212', 'krish@gmail.com', 'Ahmedabad', 'Navrangpura'),
(7, 'Anaya Joshi', '9876543213', 'anaya@gmail.com', 'Jamnagar', 'Patel Colony'),
(8, 'Dev Trivedi', '9876543214', 'dev@gmail.com', 'Rajkot', '150 Feet Ring Road');

-- --------------------------------------------------------

--
-- Table structure for table `menu`
--

CREATE TABLE `menu` (
  `menu_id` int(11) NOT NULL,
  `item_name` varchar(100) NOT NULL,
  `category` varchar(50) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `availability` varchar(30) NOT NULL DEFAULT 'Available'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `menu`
--

INSERT INTO `menu` (`menu_id`, `item_name`, `category`, `price`, `availability`) VALUES
(1, '1', 'Coffee', 50.00, 'Available'),
(2, 'Classic Cappuccino', 'Coffee', 149.00, 'Available'),
(3, 'Dark Mocha', 'Coffee', 179.00, 'Available'),
(4, 'Cold Coffee', 'Beverages', 129.00, 'Available'),
(5, 'Chocolate Brownie', 'Dessert', 99.00, 'Available'),
(6, 'Cheese Sandwich', 'Snacks', 139.00, 'Available'),
(7, 'Margherita Pizza', 'Pizza', 249.00, 'Available'),
(8, 'Veg Burger', 'Burger', 159.00, 'Available'),
(9, 'French Fries', 'Snacks', 89.00, 'Available'),
(10, 'Oreo Shake', 'Beverages', 169.00, 'Available'),
(11, 'Red Velvet Cake', 'Dessert', 199.00, 'Available'),
(12, 'Spanish Latte', 'Coffee', 449.00, 'Available'),
(13, 'Caramel Macchiato', 'Coffee', 499.00, 'Available'),
(14, 'Irish Cream Coffee', 'Coffee', 549.00, 'Available'),
(15, 'Affogato Deluxe', 'Coffee', 599.00, 'Available'),
(16, 'Sizzling Brownie', 'Dessert', 399.00, 'Available'),
(17, 'Lotus Biscoff Cheesecake', 'Dessert', 549.00, 'Available'),
(18, 'Belgian Chocolate Fondant', 'Dessert', 599.00, 'Available'),
(19, 'Blueberry Cheesecake', 'Dessert', 499.00, 'Available'),
(20, 'Truffle Mushroom Pizza', 'Pizza', 799.00, 'Available'),
(21, 'Peri Peri Paneer Pizza', 'Pizza', 699.00, 'Available'),
(22, 'Mexican Nachos Supreme', 'Snacks', 499.00, 'Available'),
(23, 'Garlic Bread Supreme', 'Snacks', 399.00, 'Available'),
(24, 'Crispy Paneer Burger', 'Burger', 250.00, 'Available'),
(25, 'Italian White Sauce Pasta', 'Pasta', 599.00, 'Available'),
(26, 'Pink Sauce Penne Pasta', 'Pasta', 649.00, 'Available'),
(27, 'Strawberry Mojito', 'Mocktail', 349.00, 'Available'),
(28, 'Blue Lagoon Mocktail', 'Mocktail', 399.00, 'Available'),
(29, 'Kiwi Mint Cooler', 'Mocktail', 399.00, 'Available'),
(30, 'Royal Oreo Shake', 'Shake', 499.00, 'Available'),
(31, 'Nutella Hazelnut Shake', 'Shake', 549.00, 'Available'),
(32, 'cafe nior special', 'Sandwich', 250.00, 'Available');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `order_date` date NOT NULL,
  `status` varchar(30) NOT NULL DEFAULT 'Pending',
  `total_amount` decimal(10,2) NOT NULL DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`order_id`, `customer_id`, `order_date`, `status`, `total_amount`) VALUES
(1, 1, '2026-11-19', 'Completed', 298.00),
(2, 2, '2026-11-19', 'Pending', 179.00),
(3, 3, '2026-11-19', 'Completed', 1247.00),
(4, 4, '2026-11-19', 'Completed', 258.00),
(5, 5, '2026-11-19', 'Pending', 39362.00);

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE `order_items` (
  `order_item_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `menu_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`order_item_id`, `order_id`, `menu_id`, `quantity`, `unit_price`, `subtotal`) VALUES
(1, 1, 1, 2, 149.00, 298.00),
(2, 2, 2, 1, 179.00, 179.00),
(3, 3, 6, 1, 249.00, 249.00),
(4, 4, 3, 2, 129.00, 258.00),
(5, 5, 15, 61, 599.00, 36539.00),
(6, 5, 5, 5, 99.00, 495.00),
(7, 5, 6, 1, 139.00, 139.00),
(8, 5, 11, 11, 199.00, 2189.00),
(10, 3, 19, 2, 499.00, 998.00);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `billing`
--
ALTER TABLE `billing`
  ADD PRIMARY KEY (`bill_id`),
  ADD KEY `order_id` (`order_id`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`customer_id`);

--
-- Indexes for table `menu`
--
ALTER TABLE `menu`
  ADD PRIMARY KEY (`menu_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `customer_id` (`customer_id`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`order_item_id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `menu_id` (`menu_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `billing`
--
ALTER TABLE `billing`
  MODIFY `bill_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `customers`
--
ALTER TABLE `customers`
  MODIFY `customer_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `menu`
--
ALTER TABLE `menu`
  MODIFY `menu_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `order_item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `billing`
--
ALTER TABLE `billing`
  ADD CONSTRAINT `billing_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`);

--
-- Constraints for table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  ADD CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`menu_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

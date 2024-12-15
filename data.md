Database Guest Table 
```mysql
CREATE TABLE guests (
    guest_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    nic VARCHAR(255) NOT NULL,
    gender VARCHAR(50) NOT NULL,
    nationality VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    emergency_contact VARCHAR(20),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);
```

Four sample guest json to test post
```json
{
    "firstName": "John",
    "lastName": "Doe",
    "nic": "123456789V",
    "gender": "Male",
    "nationality": "Sri Lankan",
    "dob": "1990-05-15",
    "phone": "+94123456789",
    "email": "john.doe@example.com",
    "address": "123 Main Street, Colombo",
    "emergencyContact": "+94198765432"
} 
```
```json
{
    "firstName": "Jane",
    "lastName": "Smith",
    "nic": "987654321V",
    "gender": "Female",
    "nationality": "Indian",
    "dob": "1985-11-20",
    "phone": "+919876543210",
    "email": "jane.smith@example.com",
    "address": "45 Central Avenue, Bangalore",
    "emergencyContact": "+914567890123"
}
```
```json
{
    "firstName": "Michael",
    "lastName": "Johnson",
    "nic": "A12345678",
    "gender": "Male",
    "nationality": "American",
    "dob": "1992-08-12",
    "phone": "+12125551234",
    "email": "michael.johnson@example.com",
    "address": "789 Park Avenue, New York",
    "emergencyContact": "+12124447788"
}
```
```json
{
  "firstName": "Anusha",
  "lastName": "Perera",
  "nic": "199358902V",
  "gender": "Female",
  "nationality": "Sri Lankan",
  "dob": "1993-05-20",
  "phone": "+94771234567",
  "email": "anusha.perera@example.com",
  "address": "23 Flower Road, Colombo 7",
  "emergencyContact": "+94772233456"
}
```

JSON for test update

```json
{
  "guestId": 1,
  "firstName": "John",
  "lastName": "Doe",
  "nic": "123456789V",
  "gender": "Male",
  "nationality": "Sri Lankan",
  "dob": "1990-05-15",
  "phone": "+94123456789",
  "email": "john.doe_updated@example.com",
  "address": "456 Updated Street, Colombo",
  "emergencyContact": "+94198765432"
}

```
# TravelX - Travel Management System

TravelX is a console-based Java travel management system. It supports customer booking, provider service management, payments, cancellations, refunds, notifications, and basic reports using an in-memory repository.

## Features

- Sign up and sign in as customer, service provider, or admin
- Search hotels by location, price, and room availability
- Search flights by source, destination, price, and seat availability
- Book hotels and flights
- View booking history with customer name, service name, booking date, and travel date
- Make payments for pending bookings
- View payment history
- Cancel confirmed bookings and request refunds
- Provider can add hotels and flights
- Provider can view services, bookings, payments, refund requests, and reports
- Admin can view all users and notifications

## Demo Accounts

The app starts with these demo users:

| Role | Email | Password |
| --- | --- | --- |
| Customer | `customer@travelx.com` | `customer123` |
| Provider | `provider@travelx.com` | `provider123` |
| Admin | `admin@travelx.com` | `admin123` |

## Requirements

- Java JDK 8 or higher
- Terminal or IDE with Java support

## Compile And Run

From the project root:

```bash
javac -d out $(find src -name '*.java')
java -cp out com.umaitpen.travelx.Main
```

If you are using IntelliJ IDEA, open the project and run:

`src/com/umaitpen/travelx/Main.java`

## Project Structure

```text
src/com/umaitpen/travelx/
├── Main.java
├── data/
│   ├── dto/             # User, Hotel, Flight, Booking, Payment, Cancellation
│   └── repository/      # In-memory TravelXDB data store
└── features/
    ├── flight/
    ├── hotel/
    ├── notification/
    ├── payment/
    ├── report/
    ├── signin/
    ├── signup/
    └── user/
```

## Validation Rules

- Name, email, password, and mobile number are required during signup
- Email must be valid
- Mobile number must contain exactly 10 digits
- Hotel name, location, price, and rooms are required
- Flight number, source, destination, price, and seats are required
- Prices, rooms, and seats must be greater than 0
- Travel dates cannot be in the past
- Flight arrival date cannot be before departure date
- Payment is allowed only for the booking owner
- Cancellation reason is required
- Providers can process refunds only for their own services

## Notes

- Data is stored in memory only, so bookings and users reset every time the app restarts.
- Payments are simulated and always succeed when the booking is valid and pending.
- Refund amount is calculated as 90% of the booking amount.

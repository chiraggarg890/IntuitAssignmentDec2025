# Producer–Consumer System Using BlockingQueue in Java

## 📌 Project Overview
This project demonstrates a **multi-producer, multi-consumer concurrent system** implemented in Java using a **BlockingQueue**.

It showcases:
- Concurrent programming
- Thread synchronization
- Blocking Queue behavior (`put()` and `take()`)
- Graceful thread interruption and shutdown

---

## 🎯 Aim of the Project
To simulate a realistic producer–consumer model where:
- Multiple producers generate unique messages.
- Multiple consumers process the messages.
- All threads safely share a bounded buffer without data corruption.
- BlockingQueue ensures thread-safe synchronization automatically.
- Threads support graceful interruption and controlled shutdown.

---

## ▶️ How to Run

### **1. Compile the project**

javac -d out  
src/main/java/org/intuit/Main.java
src/main/java/org/intuit/producer/Producer.java
src/main/java/org/intuit/consumer/Consumer.java


### **2. Run the program**

java -cp out org.intuit.Main


### **3. Enter inputs when prompted**
Example:

Enter number of producers: 2  
Enter number of consumers: 3  
Enter buffer size: 5  
Enter program run duration (in seconds): 10


---
## 🧪 Build the project

```bash
mvn clean install
```

--- 

## 🧪 Running Unit Tests

If using Maven:
```bash
mvn test
```

If using IntelliJ or Eclipse:  
Right-click → Run All Tests

---

## ✔ Features
- Multiple producers with unique message generation.
- Multiple consumers processing messages.
- Automatic blocking on full/empty buffer.
- Graceful interrupt handling.
- Clean and maintainable OOP design.
- 100% test coverage (Producer, Consumer, and Integration tests).
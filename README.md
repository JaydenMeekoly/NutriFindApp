# **NutriFindApp**

NutriFindApp is a modern mobile nutrition assistant designed to help users search for foods, view nutritional information, generate meal plans, and track healthy eating habits. The application includes secure authentication mechanisms such as **Google Single Sign-On (SSO)** and **Biometric Authentication**, providing a seamless and safe user experience.

---

##  **Features**

###  **Secure Authentication**

* **Google Single Sign-On (OAuth 2.0)**
  Fast, secure login using a user's existing Google account.
* **Biometric Authentication**
  Supports fingerprint and facial recognition for quick and secure access.

###  **Food Search & Nutrition Lookup**

* Search thousands of foods
* View calories, macronutrients, vitamins & minerals
* Fast, clean UI for quick nutritional insights

###  **Meal Plan Generation**

* Create daily or weekly meal plans
* Add foods to Breakfast, Lunch, Dinner, and Snacks
* Auto-calculates nutritional totals

###  **Favourites & Saved Foods**

* Save frequently used foods
* Quickly access items without searching again

###  **Clean and Responsive UI**

* Simple navigation
* High-contrast design
* Optimized for usability and accessibility

---

##  **Architecture Overview**

NutriFindApp follows a modular design consisting of:

### **Presentation Layer**

* UI pages, forms, navigation
* Biometric and Google login screens

### **Logic Layer**

* Authentication flow
* Meal plan generation
* Search filtering
* Nutrient calculations

### **Data Layer**

* Food models
* Meal plan data
* User session data
* Token storage

This structure makes the project easy to maintain, extend, and debug.

---

##  **Data Structures Used**

NutriFindApp makes deliberate use of efficient data structures to support performance and usability.

### **`List<T>`**

Used for:

* Search results
* Meal plan entries
* Saved foods

**Why:**
Dynamic, easy to sort/filter, ideal for frequently changing data.

---

### **`Dictionary<TKey, TValue>`**

Used for:

* Mapping nutrient names → values
* Fast retrieval of nutritional data

**Why:**
O(1) lookup time and better organization of nutrient sets.

---

### **Navigation Stack (Stack-like Behavior)**

Used for:

* Managing user navigation
* Handling the mobile “back” action

**Why:**
Mimics native mobile navigation flow.


---

### **Hash-Based Security Structures**

Used in:

* Google OAuth token handling
* Biometric encrypted key storage

**Why:**
Ensures secure, tamper-resistant authentication.

---

##  **How Data Structures Support Core Features**

| Feature         | Supporting Data Structure     | Contribution                        |
| --------------- | ----------------------------- | ----------------------------------- |
| Food Search     | List, Dictionary              | Fast filtering & nutrient retrieval |
| Meal Plans      | List                          | Flexible meal composition & totals  |
| Authentication  | Hash structures, OAuth tokens | Secure login & session management   |
| Navigation      | Stack-like flow               | Smooth user experience              |
| Food Categories | Tree-like hierarchy           | Organized browsing & grouping       |

---

##  **Challenges Encountered**

### **Google SSO Integration**

* Handling OAuth redirects
* Managing token validation
* Configuring SHA keys & client IDs

### **Biometric Authentication**

* Integrating platform APIs
* Handling fallback authentication options
* Managing secure local storage

### **State Management**

* Ensuring smooth navigation
* Persisting user sessions
* Handling asynchronous data loads

### **Nutrition Data Parsing**

* Ensuring consistency
* Efficient lookup for real-time use

---

##  **Key Learnings**

### **Technical**

* OAuth 2.0 authentication
* Biometric authentication methods
* Clean architecture design
* Mobile UI/UX best practices
* Efficient use of data structures

### **Problem-Solving**

* Breaking complex features into manageable tasks
* Debugging external authentication APIs
* Designing secure and scalable flows

### **Programming Techniques**

* Asynchronous programming
* Event-driven logic
* Secure local storage handling
* Defensive programming & error-handling patterns

---

##  **Tech Stack**

* **Kotlin Kts**
* **Google Firebase / Identity Platform**
* **Firebase (Biometrics)**

---

##  **Installation & Setup**

1. Clone the repository:

   ```bash
   git clone https://github.com/JaydenMeekoly/NutriFindApp.git 
   ```
2. Open the project in Android Studios
3. Sync with Gradle
4. Configure Google Sign-In credentials
5. Run on emulator or physical device

---

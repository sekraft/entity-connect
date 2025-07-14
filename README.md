# SpEntityConnect

[![Maven Central](https://img.shields.io/maven-central/v/com.sekraft/sp-entity-connect)](https://search.maven.org/artifact/com.sekraft/sp-entity-connect)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java Version](https://img.shields.io/badge/Java-21-blue.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)

A comprehensive Spring Boot framework for MySQL entity management with advanced auditing, custom ID generation, and lifecycle management capabilities.

## 🚀 Features

- **Advanced Auditing**: Complete audit trail with creation, modification, and actor tracking
- **Custom ID Generation**: Flexible ID generation strategies including UUID and database sequences
- **Entity Lifecycle Management**: Comprehensive lifecycle hooks for entity operations
- **Immutable/Mutable Entity Support**: Separate abstractions for different entity types
- **Spring Data Integration**: Seamless integration with Spring Data JPA
- **Hibernate Envers Integration**: Built-in versioning and audit history
- **MySQL Optimized**: Specifically designed for MySQL database operations

## 📋 Table of Contents

- [Installation](#installation)
- [Quick Start](#quick-start)
- [Architecture](#architecture)
- [Usage Examples](#usage-examples)
- [Configuration](#configuration)
- [Advanced Features](#advanced-features)
- [Best Practices](#best-practices)
- [Contributing](#contributing)
- [License](#license)

## 🛠️ Installation

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.sekraft</groupId>
    <artifactId>sp-entity-connect</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```gradle
implementation 'com.sekraft:sp-entity-connect:1.0.0'
```

## 🚀 Quick Start

### 1. Enable Auto-Configuration

The framework automatically configures itself through `@ComponentScan`. Ensure your main application class can discover the framework components:

```java
@SpringBootApplication
@EnableJpaAuditing // Required for auditing features
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

### 2. Create Your First Entity

```java
@Entity
@Table(name = "users")
public class User extends AbstractAuditableMutableDetail implements Detail {
    
    @Column(nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String email;
    
    // Constructors, getters, and setters
    
    @Override
    public String getComponentName() {
        return "USER";
    }
}
```

### 3. Database Configuration

Configure your MySQL database in `application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

# Auditing Configuration
spring.jpa.properties.hibernate.envers.audit_table_suffix=_audit
spring.jpa.properties.hibernate.envers.revision_field_name=revision_id
spring.jpa.properties.hibernate.envers.revision_type_field_name=revision_type
```

## 🏗️ Architecture

The framework is built around several key architectural components:

### Entity Hierarchy

```
AbstractEntityLifecycleHooks
├── AbstractAuditableCreated
├── AbstractAuditableUpdated
├── AbstractAuditableActorTracking
└── AbstractDetail
    ├── AbstractAuditableDetail
    │   ├── AbstractAuditableImmutableDetail
    │   └── AbstractAuditableMutableDetail
    ├── AbstractImmutableDetail
    └── AbstractMutableDetail
```

### ID Generation Strategy Pattern

The framework implements a flexible ID generation system:

- **UuidIdGenerationStrategy**: Generates UUID-based identifiers
- **DbSequenceIdGenerationStrategy**: Uses database stored procedures for ID generation
- **Custom Strategies**: Easily extensible for custom ID generation logic

## 📚 Usage Examples

### Basic Entity with Auditing

```java
@Entity
@Table(name = "products")
public class Product extends AbstractAuditableMutableDetail implements Detail {
    
    @Column(nullable = false)
    private String name;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(length = 1000)
    private String description;
    
    // Constructors
    public Product() {}
    
    public Product(String name, BigDecimal price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    @Override
    public String getComponentName() {
        return "PRODUCT";
    }
}
```

### Immutable Entity

```java
@Entity
@Table(name = "transactions")
public class Transaction extends AbstractAuditableImmutableDetail implements Detail {
    
    @Column(nullable = false)
    private String transactionId;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(nullable = false)
    private String currency;
    
    // Constructor for immutable entity
    @Builder
    public Transaction(String transactionId, BigDecimal amount, String currency) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.currency = currency;
    }
    
    // Only getters for immutable entity
    public String getTransactionId() { return transactionId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    
    @Override
    public String getComponentName() {
        return "TRANSACTION";
    }
}
```

### Custom ID Generation with UUID

```java
@Entity
@Table(name = "sessions")
public class UserSession extends AbstractAuditableDetail implements UuidAsPrimaryKey {
    
    @Column(nullable = false)
    private String userId;
    
    @Column(nullable = false)
    private LocalDateTime loginTime;
    
    @Column
    private LocalDateTime logoutTime;
    
    // Constructors, getters, and setters
    
    // No need to implement getComponentName() for UUID strategy
}
```

### Repository Layer

```java
@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    
    List<Product> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice, 
                                   @Param("maxPrice") BigDecimal maxPrice);
    
    List<Product> findByCreatedOnBetween(LocalDateTime startDate, LocalDateTime endDate);
}
```

### Service Layer

```java
@Service
@Transactional
public class ProductService {
    
    private final ProductRepository productRepository;
    
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public Product createProduct(ProductCreateRequest request) {
        Product product = new Product(
            request.getName(),
            request.getPrice(),
            request.getDescription()
        );
        return productRepository.save(product);
    }
    
    public Product updateProduct(String id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        
        return productRepository.save(product);
    }
    
    @Transactional(readOnly = true)
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
}
```

## ⚙️ Configuration

### Database Sequence for ID Generation

Create a stored procedure in your MySQL database:

```sql
DELIMITER //

CREATE PROCEDURE generateId(
    IN component_name VARCHAR(50),
    OUT generated_id VARCHAR(255)
)
BEGIN
    DECLARE seq_value INT;
    
    -- Get or create sequence for component
    INSERT INTO id_sequences (component, current_value) 
    VALUES (component_name, 1) 
    ON DUPLICATE KEY UPDATE current_value = current_value + 1;
    
    -- Get current value
    SELECT current_value INTO seq_value 
    FROM id_sequences 
    WHERE component = component_name;
    
    -- Generate formatted ID
    SET generated_id = CONCAT(component_name, '_', LPAD(seq_value, 8, '0'));
END //

DELIMITER ;
```

Create the sequence table:

```sql
CREATE TABLE id_sequences (
    component VARCHAR(50) PRIMARY KEY,
    current_value INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Audit Configuration

Enable auditing in your configuration:

```java
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class AuditConfig {
    
    @Bean
    public AuditorAware<String> auditAwareImpl() {
        return new AuditorAwareImpl();
    }
}

@Component
public class AuditorAwareImpl implements AuditorAware<String> {
    
    @Override
    public Optional<String> getCurrentAuditor() {
        // Return current user from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of("system");
        }
        
        return Optional.of(authentication.getName());
    }
}
```

## 🔧 Advanced Features

### Custom Lifecycle Hooks

```java
@Entity
@Table(name = "orders")
public class Order extends AbstractAuditableMutableDetail implements Detail {
    
    @Column(nullable = false)
    private String orderNumber;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    @Override
    protected void onPrePersist() {
        super.onPrePersist();
        if (this.status == null) {
            this.status = OrderStatus.PENDING;
        }
        // Custom logic before persistence
    }
    
    @Override
    protected void onPreUpdate() {
        super.onPreUpdate();
        // Custom logic before update
        validateStatusTransition();
    }
    
    private void validateStatusTransition() {
        // Implement business logic for status transitions
    }
    
    @Override
    public String getComponentName() {
        return "ORDER";
    }
}
```

### Querying Audit History

```java
@Service
public class AuditService {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public List<Object[]> getEntityAuditHistory(String entityId, Class<?> entityClass) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        
        return auditReader.createQuery()
            .forRevisionsOfEntity(entityClass, false, true)
            .add(AuditEntity.id().eq(entityId))
            .getResultList();
    }
    
    public Object getEntityAtRevision(String entityId, Class<?> entityClass, Number revision) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        return auditReader.find(entityClass, entityId, revision);
    }
}
```

## 📋 Best Practices

### 1. Entity Design

- Always extend the appropriate abstract class based on your needs
- Use `AbstractAuditableImmutableDetail` for entities that shouldn't be modified after creation
- Use `AbstractAuditableMutableDetail` for entities that can be updated
- Implement the `Detail` interface for database sequence ID generation
- Implement `UuidAsPrimaryKey` for UUID-based ID generation

### 2. Performance Considerations

```java
// Use appropriate fetch strategies
@Entity
public class Order extends AbstractAuditableMutableDetail implements Detail {
    
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}

// Use projections for read-only queries
public interface ProductSummary {
    String getId();
    String getName();
    BigDecimal getPrice();
}

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<ProductSummary> findAllProjectedBy();
}
```

### 3. Transaction Management

```java
@Service
@Transactional
public class OrderService {
    
    @Transactional(readOnly = true)
    public List<Order> findOrdersByCustomer(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processOrderAsync(String orderId) {
        // Process order in separate transaction
    }
}
```

### 4. Exception Handling

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("ENTITY_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        ErrorResponse error = new ErrorResponse("DATA_INTEGRITY_VIOLATION", "Data constraint violation");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
```

## 🧪 Testing

### Unit Testing

```java
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    
    @Mock
    private ProductRepository productRepository;
    
    @InjectMocks
    private ProductService productService;
    
    @Test
    void createProduct_ShouldReturnSavedProduct() {
        // Given
        ProductCreateRequest request = new ProductCreateRequest("Test Product", 
            BigDecimal.valueOf(99.99), "Test Description");
        Product savedProduct = new Product("Test Product", BigDecimal.valueOf(99.99), "Test Description");
        
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        
        // When
        Product result = productService.createProduct(request);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Product");
        verify(productRepository).save(any(Product.class));
    }
}
```

### Integration Testing

```java
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryIntegrationTest {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Test
    void findByNameContainingIgnoreCase_ShouldReturnMatchingProducts() {
        // Given
        Product product1 = new Product("Test Product 1", BigDecimal.valueOf(10.00), "Description 1");
        Product product2 = new Product("Another Product", BigDecimal.valueOf(20.00), "Description 2");
        productRepository.saveAll(Arrays.asList(product1, product2));
        
        // When
        List<Product> results = productRepository.findByNameContainingIgnoreCase("test");
        
        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Test Product 1");
    }
}
```

## 🤝 Contributing

We welcome contributions! Please follow these guidelines:

1. **Fork the repository** and create your feature branch
2. **Write tests** for new functionality
3. **Follow coding standards** (see `.editorconfig` and checkstyle rules)
4. **Document your changes** in the README and JavaDoc
5. **Submit a pull request** with a clear description

### Development Setup

```bash
# Clone the repository
git clone https://github.com/sekraft/sp-entity-connect.git
cd sp-entity-connect

# Build the project
./mvnw clean install

# Run tests
./mvnw test

# Run with MySQL (using Docker)
docker-compose up -d
./mvnw spring-boot:run
```

### Code Style

This project follows Google Java Style Guide. Please ensure your code:

- Uses 4 spaces for indentation
- Has proper JavaDoc comments
- Follows naming conventions
- Includes appropriate unit tests

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Spring Boot Team** for the excellent framework
- **Hibernate Team** for JPA implementation and Envers
- **MySQL Team** for the robust database system
- **Lombok Team** for reducing boilerplate code

## 📞 Support

- **GitHub Issues**: [Report bugs and request features](https://github.com/sekraft/sp-entity-connect/issues)
- **Documentation**: [Full documentation](https://github.com/sekraft/sp-entity-connect/wiki)
- **Email**: ashwani.singh.dev@gmail.com

## 🔄 Changelog

### Version 1.0.0
- Initial release
- Core entity management framework
- Auditing capabilities
- Custom ID generation strategies
- Lifecycle hooks
- MySQL integration

---

## 📁 File Structure

```
sp-entity-connect/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── sekraft/
│   │   │           └── entityconnect/
│   │   │               ├── EntityConnectAutoConfig.java
│   │   │               └── core/
│   │   │                   ├── annotation/
│   │   │                   │   └── IdentifierGenerator.java
│   │   │                   ├── audit/
│   │   │                   │   ├── AbstractAuditableActorTracking.java
│   │   │                   │   ├── AbstractAuditableCreated.java
│   │   │                   │   ├── AbstractAuditableDetail.java
│   │   │                   │   ├── AbstractAuditableImmutableDetail.java
│   │   │                   │   ├── AbstractAuditableMutableDetail.java
│   │   │                   │   ├── AbstractAuditableUpdated.java
│   │   │                   │   ├── AbstractEntityLifecycleHooks.java
│   │   │                   │   ├── AbstractImmutableDetail.java
│   │   │                   │   └── AbstractMutableDetail.java
│   │   │                   ├── entity/
│   │   │                   │   └── AbstractDetail.java
│   │   │                   ├── generator/
│   │   │                   │   ├── DbSequenceIdGenerationStrategy.java
│   │   │                   │   ├── IdGenerator.java
│   │   │                   │   └── UuidIdGenerationStrategy.java
│   │   │                   ├── interfaces/
│   │   │                   │   ├── Detail.java
│   │   │                   │   ├── IdGenerationStrategy.java
│   │   │                   │   └── UuidAsPrimaryKey.java
│   │   │                   └── service/
│   │   │                       └── IdGenerationService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
├── pom.xml
└── README.md
```

---

**Made with ❤️ by [Ashwani Singh](https://github.com/ashwani-singh-dev) at SeKraft**

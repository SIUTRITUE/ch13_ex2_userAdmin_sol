package murach.data;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class DatabaseConfig {
    private static EntityManagerFactory emf;
    
    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            try {
                // Get database configuration from environment variables
                String dbHost = System.getenv("DB_HOST");
                String dbPort = System.getenv("DB_PORT");
                String dbName = System.getenv("DB_NAME");
                String dbUser = System.getenv("DB_USER");
                String dbPassword = System.getenv("DB_PASSWORD");
                
                Map<String, String> properties = new HashMap<>();
                
                // If environment variables are set (production), use them
                if (dbHost != null && dbPort != null && dbName != null) {
                    String jdbcUrl = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", 
                                                   dbHost, dbPort, dbName);
                    properties.put("jakarta.persistence.jdbc.url", jdbcUrl);
                    properties.put("jakarta.persistence.jdbc.user", dbUser != null ? dbUser : "root");
                    properties.put("jakarta.persistence.jdbc.password", dbPassword != null ? dbPassword : "");
                    properties.put("jakarta.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
                    System.out.println("Using production MySQL database: " + jdbcUrl);
                } else {
                    // For demo/development, use H2 in-memory database
                    properties.put("jakarta.persistence.jdbc.url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
                    properties.put("jakarta.persistence.jdbc.user", "sa");
                    properties.put("jakarta.persistence.jdbc.password", "");
                    properties.put("jakarta.persistence.jdbc.driver", "org.h2.Driver");
                    System.out.println("Using H2 in-memory database for demo");
                }
                
                // Common properties (driver is set above)
                properties.put("jakarta.persistence.schema-generation.database.action", "create");
                properties.put("eclipselink.logging.level", "FINE");
                properties.put("eclipselink.ddl-generation", "create-or-extend-tables");
                
                emf = Persistence.createEntityManagerFactory("userAdminPU", properties);
                System.out.println("EntityManagerFactory created successfully");
            } catch (Exception e) {
                System.err.println("Failed to create EntityManagerFactory: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }
        return emf;
    }
    
    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            emf = null;
        }
    }
}
package murach.data;

import jakarta.persistence.EntityManagerFactory;

public class DBUtil {
    public static EntityManagerFactory getEmFactory() {
        return DatabaseConfig.getEntityManagerFactory();
    }
    
    public static void closeEmFactory() {
        DatabaseConfig.closeEntityManagerFactory();
    }
}
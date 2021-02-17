
package util;

import javax.persistence.*;

public class DBUtil {
    private static final EntityManagerFactory emf =
        Persistence.createEntityManagerFactory("level-ground-recruitmentPU");

    public static EntityManagerFactory getEmFactory() {
        return emf;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import javax.persistence.*;

/**
 *
 * @author 839645
 */
public class DBUtil {
    private static final EntityManagerFactory emf =
        Persistence.createEntityManagerFactory("Sem3-ProjectPU");

    public static EntityManagerFactory getEmFactory() {
        return emf;
    }
}

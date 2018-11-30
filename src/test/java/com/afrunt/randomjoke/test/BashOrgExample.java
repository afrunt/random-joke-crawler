package com.afrunt.randomjoke.test;

import com.afrunt.randomjoke.suppliers.BashOrg;

/**
 * @author Andrii Frunt
 */
public class BashOrgExample {
    public static void main(String[] args) {
        BashOrg bashOrg = new BashOrg();
        for (int i = 0; i < 100; i++) {
            System.out.println(bashOrg.get().getText());
        }
    }
}

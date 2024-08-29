package com.reisparadijs.reisparadijs.business.domain;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 07 August Wednesday 2024 - 18:36
 */


public class Role {
     private  Integer id;
     private  String name;

    public enum RoleEnum  {
        ROLE_HOST("HOST"),
        ROLE_ADMIN("ADMIN"),
        ROLE_GUEST("GUEST");

        private String value;

        RoleEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public Role() {
     }
     public Role(Integer id, String name) {
         this.id = id;
         this.name = name;
     }

     public Integer getId() {
         return id;
     }

     public String getName() {
         return name;
     }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

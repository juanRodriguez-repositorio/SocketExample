/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package socketexample;
import java.io.*;

/**
 *
 * @author kamus
 */
class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public Person(){}

    @Override
    public String toString() {
        return "Persona{nombre='" + name + "', edad=" + age + "}";
    }
    
}


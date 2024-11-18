package com.example.comwanyoikesufeeds;

public class Student {
    private String adm;
    private String name;
    private String email;

    public Student(String name, String email, String adm) {
        this.adm = adm;
        this.name = name;
        this.email = email;
    }
    public Student(String adm){
        this.adm = adm;
    }
    public Student(String adm,String email){
        this.adm = adm;
        this.email = email;
    }

    public String getAdm() {return this.adm;}
    public String getName() {return this.name;}
    public String getEmail() {return this.email;}
}

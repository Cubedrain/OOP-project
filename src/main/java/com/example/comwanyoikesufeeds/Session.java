package com.example.comwanyoikesufeeds;

public class Session {
    private static Student loggedStudent;

    public static void setCurrentStudent(Student currentStudent) {loggedStudent = currentStudent;}
    public static Student getCurrentStudent() {return loggedStudent;}
    public static void clear(){loggedStudent = null;}
}

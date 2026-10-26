
package com.studentmaster.model;

public class Student {
    private String nim;
    private String name;
    private String major;
    private String email;
    private String photo;

    public Student(String nim, String name, String major,
                   String email, String photo) {
        this.nim = nim;
        this.name = name;
        this.major = major;
        this.email = email;
        this.photo = photo;
    }

    public String getNim() {
        return nim;
    }

    public String getName() {
        return name;
    }

    public String getMajor() {
        return major;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

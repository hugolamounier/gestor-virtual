package br.edu.ufvjm.gestorvirtual;


public class User {
    private String email, password, name, birth, profilePictureUri;
    private int gender;

    public User(String email, String password, String name, String birth, String profilePictureUri, int gender) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.birth = birth;
        this.profilePictureUri = profilePictureUri;
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getProfilePictureUri() {
        return profilePictureUri;
    }

    public void setProfilePictureUri(String profilePictureUri) {
        this.profilePictureUri = profilePictureUri;
    }
    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}

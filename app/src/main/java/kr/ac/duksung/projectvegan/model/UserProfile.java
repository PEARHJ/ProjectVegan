package kr.ac.duksung.projectvegan.model;

public class UserProfile {

    private String userId, userEmail, userPassword, userVeganReason, userVeganType, userAllergy;

    public UserProfile(String userId, String userEmail, String userPassword, String userVeganReason, String userVeganType, String userAllergy) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userVeganReason = userVeganReason;
        this.userVeganType = userVeganType;
        this.userAllergy = userAllergy;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserVeganReason() {
        return userVeganReason;
    }

    public void setUserVeganReason(String userVeganReason) {
        this.userVeganReason = userVeganReason;
    }

    public String getUserVeganType() {
        return userVeganType;
    }

    public void setUserVeganType(String userVeganType) {
        this.userVeganType = userVeganType;
    }

    public String getUserAllergy() {
        return userAllergy;
    }

    public void setUserAllergy(String userAllergy) {
        this.userAllergy = userAllergy;
    }
}

package com.md.splashloginsignup;


public class users {

    public String fullName;
    public String status;
    public String profile_images;


    public String date_from;

    public String getRequest_details() {
        return request_details;
    }

    public void setRequest_details(String request_details) {
        this.request_details = request_details;
    }

    public String request_details;

    public String date_to;


    public String number_of_people;

    public users()
    {

    }

    public users(String fullName, String status, String profile_images,String date_from,String date_to,String number_of_people) {
        this.fullName = fullName;
        this.status = status;
        this.profile_images = profile_images;
        this.date_from=date_from;
        this.date_to=date_to;
        this.number_of_people=number_of_people;
    }

    public String getName() {
        return fullName;
    }

    public void setName(String fullName) {
        this.fullName = fullName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return profile_images;
    }

    public void setImage(String profile_images){
        this.profile_images=profile_images;
    }

    public String getDate_from() {
        return date_from;
    }

    public void setDate_from(String date_from) {
        this.date_from = date_from;
    }
    public String getDate_to() {
        return date_to;
    }

    public void setDate_to(String date_to) {
        this.date_to = date_to;
    }

    public String getNumber_of_people() {
        return number_of_people;
    }

    public void setNumber_of_people(String number_of_people) {
        this.number_of_people = number_of_people;
    }

}

package com.md.splashloginsignup;

import java.util.List;

public class search_model {
    String fullName;
    String Languages;
    String Sign_up_as;
    String profile_images;
    String City;



    String payment;

 public search_model(String fullName,String Languages ,String Sign_up_as,String profile_images, String payment,String City) {
        this.fullName = fullName;
        this.Languages=Languages;
        this.Sign_up_as=Sign_up_as;
        this.profile_images = profile_images;
          this.payment=payment;
          this.City=City;

    }
    public search_model(){

    }
    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getProfile_images() {
        return profile_images;
    }

    public void setProfile_images(String profile_images) {
        this.profile_images = profile_images;
    }

    public String getSign_up_as() {
        return Sign_up_as;
    }

    public void setSign_up_as(String Sign_up_as) {
        Sign_up_as = Sign_up_as;
    }

    public String getLanguages() {
        return Languages;
    }

    public void setLanguages(String languages) {
        Languages = languages;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}

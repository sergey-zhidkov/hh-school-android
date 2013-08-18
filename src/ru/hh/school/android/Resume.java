package ru.hh.school.android;

import java.util.Date;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;

public class Resume implements Parcelable{

    private int id;
    private String lastFirstName;
    private Date birthday;
    private String gender;
    private String desiredJobTitle;
    private String salary;
    private String phone;
    private String email;

    public Resume() {

    }

    public boolean isFilledCorrectly() {
        boolean filledCorrectly = true;
        if (lastFirstName == null || lastFirstName.length() == 0) {
            filledCorrectly = false;
        }

        if (desiredJobTitle == null || desiredJobTitle.length() == 0) {
            filledCorrectly = false;
        }

        return filledCorrectly;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastFirstName() {
        return lastFirstName;
    }

    public void setLastFirstName(String lastFirstName) {
        this.lastFirstName = lastFirstName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getFormattedBirthday(Context context) {
        java.text.DateFormat dateFormat = DateFormat.getDateFormat(context);
        return dateFormat.format(birthday);
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDesiredJobTitle() {
        return desiredJobTitle;
    }

    public void setDesiredJobTitle(String desiredJobTitle) {
        this.desiredJobTitle = desiredJobTitle;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Resume [lastFirstName=" + lastFirstName + ", birthday=" + birthday + ", gender=" + gender
                + ", desiredJobTitle=" + desiredJobTitle + ", salary=" + salary + ", phone=" + phone + ", email="
                + email + "]";
    }

    //******* Parcelable part ******
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lastFirstName);
        dest.writeLong(birthday.getTime());
        dest.writeString(gender);
        dest.writeString(desiredJobTitle);
        dest.writeString(salary);
        dest.writeString(phone);
        dest.writeString(email);
    }

    public static final Parcelable.Creator<Resume> CREATOR = new Parcelable.Creator<Resume>() {
        @Override
        public Resume createFromParcel(Parcel in) {
            return new Resume(in);
        }

        @Override
        public Resume[] newArray(int size) {
            return new Resume[size];
        }
    };

    private Resume(Parcel parcel) {
        lastFirstName = parcel.readString();
        birthday = new Date(parcel.readLong());
        gender = parcel.readString();
        desiredJobTitle = parcel.readString();
        salary = parcel.readString();
        phone = parcel.readString();
        email = parcel.readString();
    }
}

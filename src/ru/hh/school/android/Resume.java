package ru.hh.school.android;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;

public class Resume implements Parcelable{

    private String lastFirstName;
    private Date birthday;
    private String gender;
    private String desiredJobTitle;
    private String salary;
    private String phone;
    private String email;

    public Resume() {

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

    public String getFormattedBirthday() {
        SimpleDateFormat formattedBirthday = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return formattedBirthday.format(birthday);
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

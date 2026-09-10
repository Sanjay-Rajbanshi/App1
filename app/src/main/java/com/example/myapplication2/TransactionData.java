package com.example.myapplication2;


import android.os.Parcelable;

import android.os.Parcel;

public class TransactionData implements Parcelable {
    private final int tid;
    private final String amount;
    private final String cardNumber;
    private final String cardHolderName;
    private final String cvv;

    private final String expiryDate;
    private final String datetime;
    private final String remarks;
    private final String application;


    protected TransactionData(Parcel in){
        tid = in.readInt();
        amount = in.readString();
        cardNumber = in.readString();
        cardHolderName = in.readString();
        cvv = in.readString();
        expiryDate = in.readString();
        datetime = in.readString();
        remarks = in.readString();
        application = in.readString();
    }
    public static final Creator<TransactionData> CREATOR = new Creator<>() {
        @Override
        public TransactionData createFromParcel(Parcel source) {
            return new TransactionData(source);
        }

        @Override
        public TransactionData[] newArray(int size) {
            return new TransactionData[size];
        }
    };

    public int getTid() {
        return tid;
    }


    public String getAmount() {
        return amount;
    }



    public String getCardNumber() {
        return cardNumber;
    }



    public String getCardHolderName() {
        return cardHolderName;
    }



    public String getCvv() {
        return cvv;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
    public String getDatetime(){
        return datetime;
    }
    public String getRemarks() {
        return remarks;
    }
    public String getApplication(){return application;}




    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(tid);
        dest.writeString(amount);
        dest.writeString(cardNumber);
        dest.writeString(cardHolderName);
        dest.writeString(cvv);
        dest.writeString(expiryDate);
        dest.writeString(datetime);
        dest.writeString(remarks);
        dest.writeString(application);
    }
}

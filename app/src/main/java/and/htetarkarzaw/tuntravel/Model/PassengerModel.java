package and.htetarkarzaw.tuntravel.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Htet Arkar Zaw on 7/6/2017.
 */

public class PassengerModel implements Parcelable {
    private String passengerName,passengerNRC,passengerPhno,seatNo,remark;

    public PassengerModel(String passengerName, String passengerNRC, String passengerPhno, String seatNo,String remark) {
        this.passengerName = passengerName;
        this.passengerNRC = passengerNRC;
        this.passengerPhno = passengerPhno;
        this.seatNo = seatNo;
        this.remark=remark;

    }


    protected PassengerModel(Parcel in) {
        passengerName=in.readString();
        passengerNRC=in.readString();
        passengerPhno=in.readString();
        seatNo=in.readString();
        remark=in.readString();
    }

    public PassengerModel() {
    }

    public static final Creator<PassengerModel> CREATOR = new Creator<PassengerModel>() {
        @Override
        public PassengerModel createFromParcel(Parcel in) {
            return new PassengerModel(in);
        }

        @Override
        public PassengerModel[] newArray(int size) {
            return new PassengerModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(passengerName);
        dest.writeString(passengerNRC);
        dest.writeString(passengerPhno);
        dest.writeString(seatNo);
        dest.writeString(remark);
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerNRC() {
        return passengerNRC;
    }

    public void setPassengerNRC(String passengerNRC) {
        this.passengerNRC = passengerNRC;
    }

    public String getPassengerPhno() {
        return passengerPhno;
    }

    public void setPassengerPhno(String passengerPhno) {
        this.passengerPhno = passengerPhno;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

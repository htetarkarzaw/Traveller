package and.htetarkarzaw.tuntravel.Model;

/**
 * Created by Eiron on 6/20/17.
 */

public class DriverModel {
    String driverName,driverNRC,driverLicenceNo,driverAddress,driverPhNo,driverRemark,startTripDate,endTripDate,currentTrip;

    public DriverModel(String driverName, String driverNRC, String driverLicenceNo, String driverAddress, String driverPhNo, String driverRemark, String startTripDate, String endTripDate,String currentTrip) {
        this.driverName = driverName;
        this.driverNRC = driverNRC;
        this.driverLicenceNo = driverLicenceNo;
        this.driverAddress = driverAddress;
        this.driverPhNo = driverPhNo;
        this.driverRemark = driverRemark;
        this.startTripDate = startTripDate;
        this.endTripDate = endTripDate;
        this.currentTrip = currentTrip;
    }

    public DriverModel() {
    }

    public void setCurrentTrip(String currentTrip) {
        this.currentTrip = currentTrip;
    }

    public String getCurrentTrip() {
        return currentTrip;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverNRC() {
        return driverNRC;
    }

    public String getDriverLicenceNo() {
        return driverLicenceNo;
    }

    public String getDriverAddress() {
        return driverAddress;
    }

    public String getDriverPhNo() {
        return driverPhNo;
    }

    public String getDriverRemark() {
        return driverRemark;
    }

    public String getStartTripDate() {
        return startTripDate;
    }

    public String getEndTripDate() {
        return endTripDate;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setDriverNRC(String driverNRC) {
        this.driverNRC = driverNRC;
    }

    public void setDriverLicenceNo(String driverLicenceNo) {
        this.driverLicenceNo = driverLicenceNo;
    }

    public void setDriverAddress(String driverAddress) {
        this.driverAddress = driverAddress;
    }

    public void setDriverPhNo(String driverPhNo) {
        this.driverPhNo = driverPhNo;
    }

    public void setDriverRemark(String driverRemark) {
        this.driverRemark = driverRemark;
    }

    public void setStartTripDate(String startTripDate) {
        this.startTripDate = startTripDate;
    }

    public void setEndTripDate(String endTripDate) {
        this.endTripDate = endTripDate;
    }
}

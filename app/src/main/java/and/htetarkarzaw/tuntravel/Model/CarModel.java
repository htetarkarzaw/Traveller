package and.htetarkarzaw.tuntravel.Model;

/**
 * Created by Eiron on 6/20/17.
 */

public class CarModel {
    String carNo,carType,countSeats,carOwnerName,carOwnerPhNo,carOwnerAddress,carOwnerNRC,currentLine,carRemark;
    String startTripDate,endTripDate,currentTrip;

    public CarModel() {
    }

    public CarModel(String carNo, String carType, String countSeats, String carOwnerName, String carOwnerPhNo, String carOwnerAddress, String carOwnerNRC, String currentLine, String carRemark,String startTripDate,String endTripDate,String currentTrip) {
        this.carNo = carNo;
        this.carType = carType;
        this.countSeats = countSeats;
        this.carOwnerName = carOwnerName;
        this.carOwnerPhNo = carOwnerPhNo;
        this.carOwnerAddress = carOwnerAddress;
        this.carOwnerNRC = carOwnerNRC;
        this.currentLine = currentLine;
        this.carRemark = carRemark;
        this.startTripDate = startTripDate;
        this.endTripDate = endTripDate;
        this.currentTrip = currentTrip;
    }

    public void setCurrentTrip(String currentTrip) {
        this.currentTrip = currentTrip;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public void setCountSeats(String countSeats) {
        this.countSeats = countSeats;
    }

    public void setCarOwnerName(String carOwnerName) {
        this.carOwnerName = carOwnerName;
    }

    public void setCarOwnerPhNo(String carOwnerPhNo) {
        this.carOwnerPhNo = carOwnerPhNo;
    }

    public void setCarOwnerAddress(String carOwnerAddress) {
        this.carOwnerAddress = carOwnerAddress;
    }

    public void setCarOwnerNRC(String carOwnerNRC) {
        this.carOwnerNRC = carOwnerNRC;
    }

    public void setCurrentLine(String currentLine) {
        this.currentLine = currentLine;
    }

    public void setCarRemark(String carRemark) {
        this.carRemark = carRemark;
    }

    public void setStartTripDate(String startTripDate) {
        this.startTripDate = startTripDate;
    }

    public void setEndTripDate(String endTripDate) {
        this.endTripDate = endTripDate;
    }

    public String getCarNo() {
        return carNo;
    }

    public String getCarType() {
        return carType;
    }

    public String getCountSeats() {
        return countSeats;
    }

    public String getCarOwnerName() {
        return carOwnerName;
    }

    public String getCarOwnerPhNo() {
        return carOwnerPhNo;
    }

    public String getCarOwnerAddress() {
        return carOwnerAddress;
    }

    public String getCarOwnerNRC() {
        return carOwnerNRC;
    }

    public String getCurrentLine() {
        return currentLine;
    }

    public String getCarRemark() {
        return carRemark;
    }

    public String getStartTripDate() {
        return startTripDate;
    }

    public String getEndTripDate() {
        return endTripDate;
    }

    public String getCurrentTrip() {
        return currentTrip;
    }
}

package and.htetarkarzaw.tuntravel.Model;

/**
 * Created by Eiron on 6/20/17.
 */

public class ConductorModel {
    String conductorName,conductorNRC,conductorLicenceNo,conductorAddress,conductorPhNo,conductorRemark,startTripDate,endTripDate,currentTrip;

    public ConductorModel(String conductorName, String conductorNRC, String conductorLicenceNo, String conductorAddress, String conductorPhNo, String conductorRemark,String startTripDate,String endTripDate,String currentTrip) {
        this.conductorName = conductorName;
        this.conductorNRC = conductorNRC;
        this.conductorLicenceNo = conductorLicenceNo;
        this.conductorAddress = conductorAddress;
        this.conductorPhNo = conductorPhNo;
        this.conductorRemark = conductorRemark;
        this.startTripDate = startTripDate;
        this.endTripDate = endTripDate;
        this.currentTrip = currentTrip;
    }

    public ConductorModel() {
    }

    public void setCurrentTrip(String currentTrip) {
        this.currentTrip = currentTrip;
    }

    public void setConductorName(String conductorName) {
        this.conductorName = conductorName;
    }

    public void setConductorNRC(String conductorNRC) {
        this.conductorNRC = conductorNRC;
    }

    public void setConductorLicenceNo(String conductorLicenceNo) {
        this.conductorLicenceNo = conductorLicenceNo;
    }

    public void setConductorAddress(String conductorAddress) {
        this.conductorAddress = conductorAddress;
    }

    public void setConductorPhNo(String conductorPhNo) {
        this.conductorPhNo = conductorPhNo;
    }

    public void setConductorRemark(String conductorRemark) {
        this.conductorRemark = conductorRemark;
    }

    public void setStartTripDate(String startTripDate) {
        this.startTripDate = startTripDate;
    }

    public void setEndTripDate(String endTripDate) {
        this.endTripDate = endTripDate;
    }

    public String getConductorName() {
        return conductorName;
    }

    public String getConductorNRC() {
        return conductorNRC;
    }

    public String getConductorLicenceNo() {
        return conductorLicenceNo;
    }

    public String getConductorAddress() {
        return conductorAddress;
    }

    public String getConductorPhNo() {
        return conductorPhNo;
    }

    public String getConductorRemark() {
        return conductorRemark;
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

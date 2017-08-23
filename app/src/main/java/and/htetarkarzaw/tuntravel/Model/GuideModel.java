package and.htetarkarzaw.tuntravel.Model;

/**
 * Created by Eiron on 6/20/17.
 */

public class GuideModel {
    String guideName,guideNRC,guideLicenceNo,guideAddress,guidePhNo,guideRemark,startTripDate,endTripDate,currentTrip;

    public GuideModel() {
    }

    public GuideModel(String guideName, String guideNRC, String guideLicenceNo, String guideAddress, String guidePhNo, String guideRemark, String startTripDate, String endTripDate,String currentTrip) {
        this.guideName = guideName;
        this.guideNRC = guideNRC;
        this.guideLicenceNo = guideLicenceNo;
        this.guideAddress = guideAddress;
        this.guidePhNo = guidePhNo;
        this.guideRemark = guideRemark;
        this.startTripDate = startTripDate;
        this.endTripDate = endTripDate;
        this.currentTrip = currentTrip;
    }

    public String getCurrentTrip() {
        return currentTrip;
    }

    public String getGuideName() {
        return guideName;
    }

    public String getGuideNRC() {
        return guideNRC;
    }

    public String getGuideLicenceNo() {
        return guideLicenceNo;
    }

    public String getGuideAddress() {
        return guideAddress;
    }

    public String getGuidePhNo() {
        return guidePhNo;
    }

    public String getGuideRemark() {
        return guideRemark;
    }

    public String getStartTripDate() {
        return startTripDate;
    }

    public String getEndTripDate() {
        return endTripDate;
    }

    public void setGuideName(String guideName) {
        this.guideName = guideName;
    }

    public void setGuideNRC(String guideNRC) {
        this.guideNRC = guideNRC;
    }

    public void setGuideLicenceNo(String guideLicenceNo) {
        this.guideLicenceNo = guideLicenceNo;
    }

    public void setGuideAddress(String guideAddress) {
        this.guideAddress = guideAddress;
    }

    public void setGuidePhNo(String guidePhNo) {
        this.guidePhNo = guidePhNo;
    }

    public void setGuideRemark(String guideRemark) {
        this.guideRemark = guideRemark;
    }

    public void setStartTripDate(String startTripDate) {
        this.startTripDate = startTripDate;
    }

    public void setEndTripDate(String endTripDate) {
        this.endTripDate = endTripDate;
    }

    public void setCurrentTrip(String currentTrip) {
        this.currentTrip = currentTrip;
    }
}

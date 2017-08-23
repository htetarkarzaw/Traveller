package and.htetarkarzaw.tuntravel.Model;

/**
 * Created by Eiron on 7/5/17.
 */

public class TripModel {
    String tripType,tripName,startDate,strStartDate,strEndDate,endDate,driverName,driverKey,conductorName1,conductor1Key,conductorName2,conductor2Key,guideName,guideKey, carNo,carKey,
    renderCharge,roadFee,costForFood,generalExpense,debit,profit,clientPhNum,clientName,clientCompany,clientPhNo;

    public TripModel() {
    }

    public TripModel(String tripType, String tripName, String startDate, String endDate, String driverName, String driverKey, String conductorName1, String conductor1Key, String conductorName2, String conductor2Key, String guideName, String guideKey, String carNo, String carKey, String renderCharge, String roadFee, String costForFood, String generalExpense, String debit, String profit, String clientName, String clientPhNum, String clientCompany) {
        this.tripType = tripType;
        this.tripName = tripName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.driverName = driverName;
        this.driverKey = driverKey;
        this.conductorName1 = conductorName1;
        this.conductor1Key = conductor1Key;
        this.conductorName2 = conductorName2;
        this.conductor2Key = conductor2Key;
        this.guideName = guideName;
        this.guideKey = guideKey;
        this.carNo = carNo;
        this.carKey = carKey;
        this.renderCharge = renderCharge;
        this.roadFee = roadFee;
        this.costForFood = costForFood;
        this.generalExpense = generalExpense;
        this.debit = debit;
        this.profit = profit;
        this.clientName = clientName;
        this.clientPhNum = clientPhNum;
        this.clientCompany = clientCompany;
    }

    public String getStrStartDate() {
        return strStartDate;
    }

    public String getStrEndDate() {
        return strEndDate;
    }

    public String getClientCompany() {
        return clientCompany;
    }

    public String getClientPhNo() {
        return clientPhNo;
    }

    public String getTripType() {
        return tripType;
    }

    public String getDriverKey() {
        return driverKey;
    }

    public String getConductor1Key() {
        return conductor1Key;
    }

    public String getConductor2Key() {
        return conductor2Key;
    }

    public String getGuideKey() {
        return guideKey;
    }

    public String getCarKey() {
        return carKey;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientPhNum() {
        return clientPhNum;
    }

    public String getTripName() {
        return tripName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getConductorName1() {
        return conductorName1;
    }

    public String getConductorName2() {
        return conductorName2;
    }

    public String getGuideName() {
        return guideName;
    }

    public String getCarNo() {
        return carNo;
    }

    public String getRenderCharge() {
        return renderCharge;
    }

    public String getRoadFee() {
        return roadFee;
    }

    public String getCostForFood() {
        return costForFood;
    }

    public String getGeneralExpense() {
        return generalExpense;
    }

    public String getDebit() {
        return debit;
    }

    public String getProfit() {
        return profit;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setConductorName1(String conductorName1) {
        this.conductorName1 = conductorName1;
    }

    public void setConductorName2(String conductorName2) {
        this.conductorName2 = conductorName2;
    }

    public void setGuideName(String guideName) {
        this.guideName = guideName;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public void setRenderCharge(String renderCharge) {
        this.renderCharge = renderCharge;
    }

    public void setRoadFee(String roadFee) {
        this.roadFee = roadFee;
    }

    public void setCostForFood(String costForFood) {
        this.costForFood = costForFood;
    }

    public void setGeneralExpense(String generalExpense) {
        this.generalExpense = generalExpense;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientPhNum(String clientPhNum) {
        this.clientPhNum = clientPhNum;
    }

    public void setDriverKey(String driverKey) {
        this.driverKey = driverKey;
    }

    public void setConductor1Key(String conductor1Key) {
        this.conductor1Key = conductor1Key;
    }

    public void setConductor2Key(String conductor2Key) {
        this.conductor2Key = conductor2Key;
    }

    public void setGuideKey(String guideKey) {
        this.guideKey = guideKey;
    }

    public void setCarKey(String carKey) {
        this.carKey = carKey;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public void setClientCompany(String clientCompany) {
        this.clientCompany = clientCompany;
    }

    public void setClientPhNo(String clientPhNo) {

        this.clientPhNo = clientPhNo;
    }

    public void setStrStartDate(String strStartDate) {
        this.strStartDate = strStartDate;
    }

    public void setStrEndDate(String strEndDate) {
        this.strEndDate = strEndDate;
    }
}

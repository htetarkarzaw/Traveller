package and.htetarkarzaw.tuntravel.Model;

/**
 * Created by Eiron on 7/6/17.
 */

public class ExistingKeyModelForTrip {
    String carKey,driverKey, conductor1Key,conductor2Key,guideKey;

    public ExistingKeyModelForTrip() {
    }

    public ExistingKeyModelForTrip(String carKey, String driverKey, String conductor1Key,String conductor2Key, String guideKey) {
        this.carKey = carKey;
        this.driverKey = driverKey;
        this.conductor1Key = conductor1Key;
        this.conductor2Key = conductor2Key;
        this.guideKey = guideKey;
    }

    public String getConductor2Key() {
        return conductor2Key;
    }

    public String getCarKey() {
        return carKey;
    }

    public String getDriverKey() {
        return driverKey;
    }

    public String getConductor1Key() {
        return conductor1Key;
    }

    public String getGuideKey() {
        return guideKey;
    }

    public void setCarKey(String carKey) {
        this.carKey = carKey;
    }

    public void setDriverKey(String driverKey) {
        this.driverKey = driverKey;
    }

    public void setConductor1Key(String conductor1Key) {
        this.conductor1Key = conductor1Key;
    }

    public void setGuideKey(String guideKey) {
        this.guideKey = guideKey;
    }

    public void setConductor2Key(String conductor2Key) {
        this.conductor2Key = conductor2Key;
    }
}

package and.htetarkarzaw.tuntravel.Model;

/**
 * Created by Htet Arkar Zaw on 7/4/2017.
 */

public class ModelForSpinner {
    String first,second,key;
    public ModelForSpinner(){

    }

    public ModelForSpinner(String first, String second,String key){
        this.first=first;
        this.second=second;
        this.key = key;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getKey() {
        return key;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

package fr.nicolashoareau_toulousewcs.appliwcsprojet;

/**
 * Created by wilder on 22/04/18.
 */

public class RequestModel {

    private String description;
    private String idRequest;
    private long date;

    public RequestModel(){

    }

    public RequestModel(String description, String idRequest, long date){
        this.description = description;
        this.idRequest = idRequest;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(String idRequest) {
        this.idRequest = idRequest;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}

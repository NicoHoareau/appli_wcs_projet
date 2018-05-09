package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import java.util.Date;

/**
 * Created by wilder on 22/04/18.
 */

public class RequestModel {

    private String description;
    private String idRequest;
    private Date date;

    public RequestModel(){

    }

    public RequestModel(String description, Date date, String idRequest){
        this.description = description;
        this.date = date;
        this.idRequest = idRequest;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

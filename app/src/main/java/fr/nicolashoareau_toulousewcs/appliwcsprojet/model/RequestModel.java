package fr.nicolashoareau_toulousewcs.appliwcsprojet.model;

/**
 * Created by wilder on 22/04/18.
 */

public class RequestModel {

    private String description;
    private String idRequest;
    private long date;
    private boolean validated = false;
    private String idUser;

    public RequestModel(){

    }

    public RequestModel(String description, String idRequest, long date, boolean validated, String idUser){
        this.description = description;
        this.idRequest = idRequest;
        this.date = date;
        this.validated = validated;
        this.idUser = idUser;
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

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdUser() {
        return idUser;
    }
}

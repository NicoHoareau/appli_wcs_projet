package fr.nicolashoareau_toulousewcs.appliwcsprojet;

/**
 * Created by wilder on 22/04/18.
 */

public class RequestModel {

    private String description;
    private String request;

    public RequestModel(String description, String request){
        this.description = description;
        this.request = request;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getDescription() {
        return description;
    }

    public String getRequest() {
        return request;
    }
}

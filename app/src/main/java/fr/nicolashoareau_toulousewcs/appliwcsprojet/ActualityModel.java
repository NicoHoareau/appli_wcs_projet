package fr.nicolashoareau_toulousewcs.appliwcsprojet;

public class ActualityModel {
    private String pseudoUser;
    private String description;
    private String urlPhoto;
    private String urlPhotoUser;
    private long datePost;
    private String idUser;

    public ActualityModel(){

    }

    public ActualityModel(String pseudoUser, String description, String urlPhoto, String urlPhotoUser, long datePost, String idUser) {
        this.description = description;
        this.urlPhoto = urlPhoto;
        this.urlPhotoUser = urlPhotoUser;
        this.datePost = datePost;
        this.pseudoUser = pseudoUser;
        this.idUser = idUser;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getUrlPhotoUser() {
        return urlPhotoUser;
    }

    public void setUrlPhotoUser(String urlPhotoUser) {
        this.urlPhotoUser = urlPhotoUser;
    }

    public long getDatePost() {
        return datePost;
    }

    public void setDatePost(long datePost) {
        this.datePost = datePost;
    }

    public void setPseudoUser(String pseudoUser) {
        this.pseudoUser = pseudoUser;
    }

    public String getPseudoUser() {
        return pseudoUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdUser() {
        return idUser;
    }
}

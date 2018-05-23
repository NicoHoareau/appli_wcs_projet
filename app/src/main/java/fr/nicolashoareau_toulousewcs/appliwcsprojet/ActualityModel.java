package fr.nicolashoareau_toulousewcs.appliwcsprojet;

public class ActualityModel {
    private String userId;
    private String description;
    private String urlPhoto;
    private String urlPhotoUser;
    private long datePost;

    public ActualityModel(){

    }

    public ActualityModel(String userId, String description, String urlPhoto, String urlPhotoUser, long datePost) {
        this.userId = userId;
        this.description = description;
        this.urlPhoto = urlPhoto;
        this.urlPhotoUser = urlPhotoUser;
        this.datePost = datePost;
    }


    public ActualityModel(String userId, String description, String urlPhoto, long datePost) {
        this.userId = userId;
        this.description = description;
        this.urlPhoto = urlPhoto;
        this.datePost = datePost;
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}

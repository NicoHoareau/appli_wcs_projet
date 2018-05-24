package fr.nicolashoareau_toulousewcs.appliwcsprojet;

public class ActualityModel {
    private String username;
    private String description;
    private String urlPhoto;
    private String urlPhotoUser;
    private long datePost;

    public ActualityModel(){

    }

    public ActualityModel(String username, String description, String urlPhoto, String urlPhotoUser, long datePost) {
        this.username = username;
        this.description = description;
        this.urlPhoto = urlPhoto;
        this.urlPhotoUser = urlPhotoUser;
        this.datePost = datePost;
    }


    public ActualityModel(String username, String description, String urlPhoto, long datePost) {
        this.username = username;
        this.description = description;
        this.urlPhoto = urlPhoto;
        this.datePost = datePost;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

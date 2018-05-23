package fr.nicolashoareau_toulousewcs.appliwcsprojet;

public class UserModel {
    private String pseudo;
    private String profilPic;
    private String language;
    private String promo;



    public UserModel() {}


    public UserModel(String pseudo, String profilPic) {
        this.pseudo = pseudo;
        this.profilPic = profilPic;
    }

    public UserModel (String profilPic){
        this.profilPic = profilPic;
    }

    public UserModel(String pseudo, String profilPic, String language, String promo) {
        this.pseudo = pseudo;
        this.profilPic = profilPic;
        this.language = language;
        this.promo = promo;
    }

    public String getPseudo() {
        return pseudo;
    }

    public UserModel setPseudo(String pseudo) {
        this.pseudo = pseudo;
        return this;
    }

    public String getProfilPic() {
        return profilPic;
    }

    public UserModel setProfilPic(String profilPic) {
        this.profilPic = profilPic;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public UserModel setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getPromo() {
        return promo;
    }

    public UserModel setPromo(String promo) {
        this.promo = promo;
        return this;
    }
}
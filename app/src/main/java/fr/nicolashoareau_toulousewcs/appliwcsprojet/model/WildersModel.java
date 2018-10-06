package fr.nicolashoareau_toulousewcs.appliwcsprojet.model;

public class WildersModel {

    private String wilderUrlPhoto;
    private String wilderPseudo;
    private String wilderPromo;


    public WildersModel(String wilderUrlPhoto, String wilderPseudo, String wilderPromo) {
        this.wilderUrlPhoto = wilderUrlPhoto;
        this.wilderPseudo = wilderPseudo;
        this.wilderPromo = wilderPromo;
    }

    public WildersModel(){

    }

    public String getWilderUrlPhoto() {
        return wilderUrlPhoto;
    }

    public void setWilderUrlPhoto(String wilderUrlPhoto) {
        this.wilderUrlPhoto = wilderUrlPhoto;
    }

    public String getWilderPseudo() {
        return wilderPseudo;
    }

    public void setWilderPseudo(String wilderPseudo) {
        this.wilderPseudo = wilderPseudo;
    }

    public String getWilderPromo() {
        return wilderPromo;
    }

    public void setWilderPromo(String wilderPromo) {
        this.wilderPromo = wilderPromo;
    }

}

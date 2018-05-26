package fr.nicolashoareau_toulousewcs.appliwcsprojet;

public class WildersModel {

    private String wilderUrlPhoto;
    private String wilderName;
    private String wilderPromo;

    public WildersModel(String wilderUrlPhoto, String wilderName, String wilderPromo) {
        this.wilderUrlPhoto = wilderUrlPhoto;
        this.wilderName = wilderName;
        this.wilderPromo = wilderPromo;
    }

    public String getWilderUrlPhoto() {
        return wilderUrlPhoto;
    }

    public void setWilderUrlPhoto(String wilderUrlPhoto) {
        this.wilderUrlPhoto = wilderUrlPhoto;
    }

    public String getWilderName() {
        return wilderName;
    }

    public void setWilderName(String wilderName) {
        this.wilderName = wilderName;
    }

    public String getWilderPromo() {
        return wilderPromo;
    }

    public void setWilderPromo(String wilderPromo) {
        this.wilderPromo = wilderPromo;
    }
}

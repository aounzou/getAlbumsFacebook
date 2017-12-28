package com.amine.hiddenfoundrs.model;

import java.io.Serializable;

/**
 * Created by AMINE on 27/11/2017.
 */

public class IdAlbum implements Serializable {
    public String idAlb, nameAlb, dateCreation;

    public IdAlbum(String idAlb, String nameAlb, String dateCreation) {
        this.idAlb = idAlb;
        this.nameAlb = nameAlb;
        this.dateCreation = dateCreation;
    }

    public IdAlbum() {
    }

    public String getIdAlb() {
        return idAlb;
    }

    public void setIdAlb(String idAlb) {
        this.idAlb = idAlb;
    }

    public String getNameAlb() {
        return nameAlb;
    }

    public void setNameAlb(String nameAlb) {
        this.nameAlb = nameAlb;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }
}
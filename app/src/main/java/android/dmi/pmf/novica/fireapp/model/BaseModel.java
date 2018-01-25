package android.dmi.pmf.novica.fireapp.model;

import java.io.Serializable;

/**
 * Created by Alex on 5/7/17.
 */

public abstract class BaseModel implements Serializable{

    private String id;

    public BaseModel() {
    }



    public BaseModel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

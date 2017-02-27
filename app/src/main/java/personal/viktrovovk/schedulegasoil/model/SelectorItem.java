package personal.viktrovovk.schedulegasoil.model;

import java.io.Serializable;

/**
 * Created by Viktor on 23/02/2017.
 */

public class SelectorItem implements Serializable{
    private Integer id;
    private String institution;

    public SelectorItem(Integer id, String institution) {
        this.id = id;
        this.institution = institution;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }
}

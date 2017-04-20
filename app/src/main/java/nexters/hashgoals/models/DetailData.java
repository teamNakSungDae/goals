package nexters.hashgoals.models;

import java.io.Serializable;
import lombok.Data;


/**
 * Created by kwongiho on 2017. 2. 23..
 */
@Data
public class DetailData implements Detail , Serializable {

    private int id;
    private int foreignKey;
    private String taskName;
    private int remainNo;
    private int viewType;
    private int recycleNo;




}

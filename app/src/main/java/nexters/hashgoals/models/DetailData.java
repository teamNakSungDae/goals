package nexters.hashgoals.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


/**
 * Created by kwongiho on 2017. 2. 23..
 */
@Data
public class DetailData implements Detail , Serializable {
    private int id;
    private int foreignKey;
    private String taskName;
    private int remain;
    private int viewType;
    private int repeat;
}

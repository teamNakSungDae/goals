package nexters.hashgoals.models;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by kwongiho on 2017. 2. 24..
 */

@Data

public class DetailPercent implements Serializable,Percent{

    private int viewType;

    private double percent;

}

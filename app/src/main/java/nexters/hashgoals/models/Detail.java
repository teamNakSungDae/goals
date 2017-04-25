package nexters.hashgoals.models;

/**
 * Created by kwongiho on 2017. 2. 24..
 */

public interface Detail extends ViewType , DBKey , Repeat , Remain , Percent{
    String getTaskName();
    void setTaskName(String taskName);
}
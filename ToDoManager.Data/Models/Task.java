package Models;

import com.sun.istack.internal.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class Task
{
    public  Task()
    {

    }
    public Task(ResultSet st) throws SQLException
    {
        Id = st.getInt("ID");
        Title = st.getString("TITLE");
        IsImportant = st.getBoolean("ISIMPORTANT");
        TaskListId = st.getInt("TASKLISTID");

        Timestamp startDate = st.getTimestamp("STARTDATE");
        Timestamp endDate = st.getTimestamp("ENDDATE");
        Timestamp plannedStartDate = st.getTimestamp("PLANNEDSTARTDATE");
        Timestamp plannedEndDate = st.getTimestamp("PLANNEDENDDATE");
        StartDate = startDate != null ? new Date(startDate.getTime()) : null;
        EndDate = endDate != null ? new Date(endDate.getTime()) : null;
        PlannedStartDate = plannedStartDate != null ? new Date(plannedStartDate.getTime()) : null;
        PlannedEndDate = plannedEndDate != null ? new Date(plannedEndDate.getTime()) : null;
    }

    public int Id;
    public String Title;
    public Date StartDate;
    public Date EndDate;
    public Date PlannedStartDate;
    public Date PlannedEndDate;
    public boolean IsImportant;
    public int TaskListId;

    public int GetId() { return Id; }
    public int GetTaskListId() { return  TaskListId; }
    public boolean GetIsImportant() { return IsImportant; }
    public boolean GetIsPlannedToday()
    {
        Date now = new Date();
        Date toady = new Date(now.getYear(), now.getMonth(), now.getDay(), 0, 0, 0);
        return EndDate == null && StartDate != null || EndDate == null && PlannedStartDate != null && PlannedStartDate.after(toady);
    }
}

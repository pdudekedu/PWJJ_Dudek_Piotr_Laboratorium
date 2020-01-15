package Models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskList
{
    public  TaskList()
    {

    }
    public TaskList(ResultSet st) throws SQLException
    {
        Id = st.getInt("ID");
        Name = st.getString("NAME");
    }

    public int Id;
    public String Name;
}

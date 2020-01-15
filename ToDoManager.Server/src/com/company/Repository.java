package com.company;

import Models.*;
import com.sun.deploy.util.StringUtils;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Repository
{
    public Repository() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://DELL-02\\SQLSRV2017:1433;databaseName=TDM;user=sa;password=sa;";
        Connection connection = DriverManager.getConnection(connectionUrl);
        statement = connection.createStatement();
    }
    Statement statement;

    private String DateToSQL(Date date)
    {
        return date != null ? "'" + _DateTimeFormat.format(date) + "'" : "NULL";
    }
    private final SimpleDateFormat _DateTimeFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    public ArrayList<TaskList> GetTaskLists() throws SQLException{
        ArrayList<TaskList> result = new ArrayList<>();
        ResultSet rs = statement.executeQuery("SELECT * FROM TASKLISTS ORDER BY NAME");
        while (rs.next()) {
            result.add(new TaskList(rs));
        }
        return result;
    }
    public ArrayList<TaskList> GetTaskLists(int[] ids) throws SQLException{
        ArrayList<TaskList> result = new ArrayList<>();
        if(ids.length == 0)
            return result;
        String query = "SELECT * FROM TASKLISTS WHERE ID IN(";
        for (int i = 0; i < ids.length; ++i)
        {
            if(i != 0)
                query += ",";
            query += Integer.toString(ids[i]);
        }
        query += ") ORDER BY NAME";
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            result.add(new TaskList(rs));
        }
        return result;
    }
    public ArrayList<Task> GetTasks(int taskListId) throws SQLException{
        ArrayList<Task> result = new ArrayList<>();
        ResultSet rs = statement.executeQuery(String.format("SELECT * FROM TASKS WHERE TASKLISTID = %s", taskListId));
        while (rs.next()) {
            result.add(new Task(rs));
        }
        return result;
    }
    public ArrayList<Task> GetTasks(int[] ids) throws SQLException{
        ArrayList<Task> result = new ArrayList<>();
        if(ids.length == 0)
            return result;
        String query = "SELECT * FROM TASKS WHERE ID IN(";
        for (int i = 0; i < ids.length; ++i)
        {
            if(i != 0)
                query += ",";
            query += Integer.toString(ids[i]);
        }
        query += ")";
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            result.add(new Task(rs));
        }
        return result;
    }
    public ArrayList<Task> GetAllTasks() throws SQLException{
        ArrayList<Task> result = new ArrayList<>();
        ResultSet rs = statement.executeQuery(String.format("SELECT * FROM TASKS"));
        while (rs.next()) {
            result.add(new Task(rs));
        }
        return result;
    }
    public ArrayList<Task> GetTodaysTasks() throws SQLException{
        ArrayList<Task> result = new ArrayList<>();
        ResultSet rs = statement.executeQuery(String.format("SELECT * FROM TASKS WHERE ENDDATE IS NULL AND STARTDATE IS NOT NULL OR ENDDATE IS NULL AND PLANNEDSTARTDATE IS NOT NULL AND PLANNEDSTARTDATE >= CONVERT(DATE, GETDATE())"));
        while (rs.next()) {
            result.add(new Task(rs));
        }
        return result;
    }
    public ArrayList<Task> GetImportantTasks() throws SQLException{
        ArrayList<Task> result = new ArrayList<>();
        ResultSet rs = statement.executeQuery(String.format("SELECT * FROM TASKS WHERE ISIMPORTANT = 1"));
        while (rs.next()) {
            result.add(new Task(rs));
        }
        return result;
    }
    public int InsertTaskList(TaskList taskList) throws SQLException
    {
        statement.execute(String.format("INSERT INTO TASKLISTS (NAME) VALUES (N'%s')", taskList.Name));
        ResultSet rs = statement.executeQuery("SELECT IDENT_CURRENT('TASKLISTS')");
        if(rs.next())
            return rs.getInt(1);
        return 0;
    }
    public int InsertTask(Task task) throws SQLException
    {
        String query = String.format("INSERT INTO TASKS (TASKLISTID, TITLE, PLANNEDSTARTDATE, PLANNEDENDDATE, ISIMPORTANT) VALUES (%d, N'%s', %s, %s, %d)",
                task.TaskListId,
                task.Title,
                DateToSQL(task.PlannedStartDate),
                DateToSQL(task.PlannedEndDate),
                task.IsImportant ? 1 : 0);
        statement.execute(query);
        ResultSet rs = statement.executeQuery("SELECT IDENT_CURRENT('TASKS')");
        if(rs.next())
            return rs.getInt(1);
        return 0;
    }
    public int UpdateTask(Task task) throws SQLException
    {
        String query = String.format("UPDATE TASKS SET " +
                        "PLANNEDSTARTDATE = %s, " +
                        "PLANNEDENDDATE = %s, " +
                        "STARTDATE = %s, " +
                        "ENDDATE = %s, " +
                        "ISIMPORTANT = %d " +
                        "WHERE ID = %d",
                DateToSQL(task.PlannedStartDate),
                DateToSQL(task.PlannedEndDate),
                DateToSQL(task.StartDate),
                DateToSQL(task.EndDate),
                task.IsImportant ? 1 : 0,
                task.Id);
        return statement.executeUpdate(query);
    }
    public int DeleteTaskList(int taskListId) throws SQLException
    {
        return statement.executeUpdate(String.format("DELETE FROM TASKLISTS WHERE ID = %s", taskListId));
    }
    public int DeleteTask(int taskId) throws SQLException
    {
        return statement.executeUpdate(String.format("DELETE FROM TASKS WHERE ID = %s", taskId));
    }
}

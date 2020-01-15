package ToDoManager.Clients;

import Models.SyncData;
import Models.Task;
import Models.TaskList;
import Settings.Config;
import Settings.Operations;
import ToDoManager.Main;
import ToDoManager.UIModels.CustomTaskList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.util.ArrayList;

public class Client
{
    public Client() throws IOException
    {
        InetAddress host = InetAddress.getLocalHost();
        socket = new Socket(host.getHostName(), Config.PORT);
    }

    Socket socket = null;

    public ArrayList<TaskList> GetTaskLists() throws IOException
    {
        return ListRequest(TaskList.class, Operations.GetTaskLists);
    }
    public ArrayList<Task> GetTasks(int taskListId) throws IOException
    {
        return ListRequest(Task.class, Operations.GetTasks, taskListId);
    }
    public ArrayList<Task> GetAllTasks() throws IOException
    {
        return ListRequest(Task.class, Operations.GetAllTasks);
    }
    public ArrayList<Task> GetTodaysTasks() throws IOException
    {
        return ListRequest(Task.class, Operations.GetTodaysTasks);
    }
    public ArrayList<Task> GetImportantTasks() throws IOException
    {
        return ListRequest(Task.class, Operations.GetImportantTasks);
    }
    public boolean InsertTaskList(CustomTaskList taskList) throws IOException
    {
        int id = Request(int.class, Operations.InsertTaskList, taskList.GetTaskList());
        if(id != 0)
            taskList.Id = id;
        return id != 0;
    }
    public boolean InsertTask(ToDoManager.UIModels.Task task) throws IOException
    {
        int id = Request(int.class, Operations.InsertTask, task.GetTask());
        if(id != 0)
            task.Id = id;
        return id != 0;
    }
    public boolean UpdateTask(ToDoManager.UIModels.Task task) throws IOException
    {
        return Request(int.class, Operations.UpdateTask, task.GetTask()) != 0;
    }
    public boolean DeleteTask(ToDoManager.UIModels.Task task) throws IOException
    {
        return Request(int.class, Operations.DeleteTask, task.Id) != 0;
    }
    public boolean DeleteTaskList(CustomTaskList taskList) throws IOException
    {
        return Request(int.class, Operations.DeleteTaskList, taskList.Id) != 0;
    }
    public SyncData Sync() throws IOException
    {
        return Request(SyncData.class, Operations.Sync);
    }

    protected <T> ArrayList<T> ListRequest(final Class<T> dataClass, String operation, Object... parameters) throws IOException {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.println(gson.toJson(Main.Current.GetApplicationId()));
        writer.println(operation);
        writer.println(gson.toJson(parameters));
        writer.flush();
        BufferedReader brinp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line = brinp.readLine();
        return gson.fromJson(line, getType(ArrayList.class, dataClass));
    }
    protected <T> T Request(final Class<T> dataClass, String operation, Object... parameters) throws IOException {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.println(gson.toJson(Main.Current.GetApplicationId()));
        writer.println(operation);
        writer.println(gson.toJson(parameters));
        writer.flush();
        BufferedReader brinp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line = brinp.readLine();
        return gson.fromJson(line, dataClass);
    }
    public void Close(){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.println(gson.toJson(Main.Current.GetApplicationId()));
            writer.println(Operations.Close);
            writer.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    Type getType(Class<?> rawClass, Class<?> parameter) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] {parameter};
            }
            @Override
            public Type getRawType() {
                return rawClass;
            }
            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }
}

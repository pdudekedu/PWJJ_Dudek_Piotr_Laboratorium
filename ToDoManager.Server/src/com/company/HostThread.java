package com.company;

import Models.SyncData;
import Models.Task;
import Models.TaskList;
import Settings.Operations;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class HostThread extends Thread {
    protected Socket socket;
    protected List<Modification> Modifications;
    protected List<UUID> Clients;

    public HostThread(Socket clientSocket, List<UUID> clients, List<Modification> modifications)
    {
        this.socket = clientSocket;
        Clients = clients;
        Modifications = modifications;
    }

    public void run()
    {
        InputStream inp = null;
        BufferedReader brinp = null;
        PrintStream out = null;
        try {
            inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
            out = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("I/O error: " + e);
            return;
        }

        String operation, parameters;
        UUID uuid;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
        while (true) {
            try {
                String rawUUID = brinp.readLine();
                if(rawUUID == null)
                    continue;
                uuid = gson.fromJson(rawUUID, UUID.class);
                if (!Clients.contains(uuid))
                {
                    UUID localUUID = uuid;
                    Clients.add(localUUID);
                    Modifications.forEach(x -> x.GetServedClients().add(localUUID));
                }
                operation = brinp.readLine();
                parameters = brinp.readLine();
                if (operation.equalsIgnoreCase(Operations.Close)) {
                    socket.close();
                    return;
                } else {
                    JsonArray array;
                    Repository repository = new Repository();
                    switch (operation)
                    {
                        case Operations.GetTaskLists:
                            out.println(gson.toJson(repository.GetTaskLists()));
                            break;
                        case Operations.GetTasks:
                            array = gson.fromJson(parameters, JsonArray.class);
                            out.println(gson.toJson(repository.GetTasks(
                                    gson.fromJson(array.get(0), int.class))));
                            break;
                        case Operations.GetAllTasks:
                            out.println(gson.toJson(repository.GetAllTasks()));
                            break;
                        case Operations.GetTodaysTasks:
                            out.println(gson.toJson(repository.GetTodaysTasks()));
                            break;
                        case Operations.GetImportantTasks:
                            out.println(gson.toJson(repository.GetImportantTasks()));
                            break;
                        case Operations.InsertTaskList:
                            array = gson.fromJson(parameters, JsonArray.class);
                            int newTaskListId = repository.InsertTaskList(gson.fromJson(array.get(0), TaskList.class));
                            out.println(gson.toJson(newTaskListId));
                            synchronized (Modifications)
                            {
                                Modifications.add(new Modification(ModificationType.TASKLIST_INSERT, newTaskListId, uuid));
                            }
                            break;
                        case Operations.InsertTask:
                            array = gson.fromJson(parameters, JsonArray.class);
                            int newTaskId = repository.InsertTask(gson.fromJson(array.get(0), Task.class));
                            out.println(gson.toJson(newTaskId));
                            synchronized (Modifications)
                            {
                                Modifications.add(new Modification(ModificationType.TASK_INSERT, newTaskId, uuid));
                            }
                            break;
                        case Operations.UpdateTask:
                            array = gson.fromJson(parameters, JsonArray.class);
                            Task updatedTask = gson.fromJson(array.get(0), Task.class);
                            out.println(gson.toJson(repository.UpdateTask(updatedTask)));
                            synchronized (Modifications)
                            {
                                Modifications.removeIf(x -> x.GetIsTaskModification() && x.GetModificationElement() == updatedTask.Id);
                                Modifications.add(new Modification(ModificationType.TASK_UPDATE, updatedTask.Id, uuid));
                            }
                            break;
                        case Operations.DeleteTask:
                            array = gson.fromJson(parameters, JsonArray.class);
                            int deletedTaskId = gson.fromJson(array.get(0), int.class);
                            out.println(gson.toJson(repository.DeleteTask(deletedTaskId)));
                            synchronized (Modifications)
                            {
                                Modifications.removeIf(x -> x.GetIsTaskModification() && x.GetModificationElement() == deletedTaskId);
                                Modifications.add(new Modification(ModificationType.TASK_DELETE, deletedTaskId, uuid));
                            }
                            break;
                        case Operations.DeleteTaskList:
                            array = gson.fromJson(parameters, JsonArray.class);
                            int deletedTaskListId = gson.fromJson(array.get(0), int.class);
                            out.println(gson.toJson(repository.DeleteTaskList(deletedTaskListId)));
                            synchronized (Modifications)
                            {
                                Modifications.removeIf(x -> x.GetIsTaskListModification() && x.GetModificationElement() == deletedTaskListId);
                                Modifications.add(new Modification(ModificationType.TASKLIST_DELETE, deletedTaskListId, uuid));
                            }
                            break;
                        case Operations.Sync:
                            SyncData syncData = new SyncData();
                            synchronized (Modifications)
                            {
                                final UUID localUUID = uuid;
                                List<Modification> modifications = Modifications.stream().filter(x -> !x.GetServedClients().contains(localUUID)).collect(Collectors.toList());
                                if(!modifications.isEmpty())
                                {
                                    List<Modification> insertedTaskLists = modifications.stream().filter(x -> x.GetModificationType() == ModificationType.TASKLIST_INSERT).collect(Collectors.toList());
                                    if(!insertedTaskLists.isEmpty())
                                        syncData.InsertedTasksLists.addAll(repository.GetTaskLists(GetIds(insertedTaskLists)));

                                    List<Modification> deletedTaskLists = modifications.stream().filter(x -> x.GetModificationType() == ModificationType.TASKLIST_DELETE).collect(Collectors.toList());
                                    if(!deletedTaskLists.isEmpty())
                                        syncData.DeletedTasksLists.addAll(deletedTaskLists.stream().map(x -> x.GetModificationElement()).collect(Collectors.toList()));

                                    List<Modification> insertedTasks = modifications.stream().filter(x -> x.GetModificationType() == ModificationType.TASK_INSERT).collect(Collectors.toList());
                                    if(!insertedTasks.isEmpty())
                                        syncData.InsertedTasks.addAll(repository.GetTasks(GetIds(insertedTasks)));

                                    List<Modification> updatedTasks = modifications.stream().filter(x -> x.GetModificationType() == ModificationType.TASK_UPDATE).collect(Collectors.toList());
                                    if(!updatedTasks.isEmpty())
                                        syncData.UpdatedTasks.addAll(repository.GetTasks(GetIds(updatedTasks)));

                                    List<Modification> deletedTasks = modifications.stream().filter(x -> x.GetModificationType() == ModificationType.TASK_DELETE).collect(Collectors.toList());
                                    if(!deletedTasks.isEmpty())
                                        syncData.DeletedTasks.addAll(deletedTasks.stream().map(x -> x.GetModificationElement()).collect(Collectors.toList()));

                                    modifications.forEach(x -> x.GetServedClients().add(localUUID));
                                }
                            }
                            out.println(gson.toJson(syncData));
                            break;
                    }
                    out.flush();
                }
            }catch (SocketTimeoutException e){
                try {

                    socket.close();
                    System.out.println("Socket timeout close: " + e);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return;
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
                e.printStackTrace();
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int[] GetIds(List<Modification> modifications)
    {
        int[] result = new int[modifications.size()];
        for (int i = 0; i < modifications.size(); ++i)
            result[i] = modifications.get(i).GetModificationElement();
        return result;
    }
}
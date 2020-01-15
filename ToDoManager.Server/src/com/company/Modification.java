package com.company;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Modification
{
    public Modification(ModificationType type, int element, UUID client)
    {
        _ModificationType = type;
        _ModificationElement = element;
        _ModificationDate = new Date();
        _ModificationClient = client;
        _ServedClients = new ArrayList<>();
        _ServedClients.add(client);
    }
    private ModificationType _ModificationType;
    private Date _ModificationDate;
    private int _ModificationElement;
    private UUID _ModificationClient;
    private List<UUID> _ServedClients;

    public  ModificationType GetModificationType() { return  _ModificationType; }
    public Date GetModificationDate() { return _ModificationDate; }
    public int GetModificationElement() { return _ModificationElement; }
    public UUID GetModificationClient() { return _ModificationClient; }
    public List<UUID> GetServedClients() { return _ServedClients; }
    public boolean GetIsTaskModification()
    {
        return _ModificationType == ModificationType.TASK_INSERT
                || _ModificationType == ModificationType.TASK_DELETE
                || _ModificationType == ModificationType.TASK_UPDATE;
    }
    public boolean GetIsTaskListModification()
    {
        return _ModificationType == ModificationType.TASKLIST_DELETE
                || _ModificationType == ModificationType.TASKLIST_INSERT;
    }
}

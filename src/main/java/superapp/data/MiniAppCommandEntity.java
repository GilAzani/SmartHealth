package superapp.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import superapp.Boundary.CommandId;
import superapp.Boundary.InvokedBy;
import superapp.Boundary.TargetObject;


import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Document(collection = "COMMAND")
public class MiniAppCommandEntity {
    @Id
    private CommandId commandId;
    private String command;
    private TargetObject targetObject;
    private Date invocationTimestamp;
    private InvokedBy invokedBy;
    private Map<String, Object> commandAttributes; // key-value


    public MiniAppCommandEntity() {
    }

    public MiniAppCommandEntity(CommandId commandId, String command, TargetObject targetObject, Date invocationTimestamp,
                                  InvokedBy invokedBy, Map<String, Object> commandAttributes) {
        this.commandId = commandId;
        this.command = command;
        this.targetObject = targetObject;
        this.invocationTimestamp = invocationTimestamp;
        this.invokedBy = invokedBy;
        this.commandAttributes = commandAttributes;
    }

    public MiniAppCommandEntity(String command, TargetObject targetObject, Date invocationTimestamp, InvokedBy invokedBy,
                                  Map<String, Object> commandAttributes) {
        this(null, command, targetObject, invocationTimestamp, invokedBy, commandAttributes);
    }

    // GETS
    public CommandId getCommandId() {
        return commandId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MiniAppCommandEntity other = (MiniAppCommandEntity) obj;
        return this.commandId.equals(other.getCommandId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandId);
    }

    public String getCommand() {
        return command;
    }

    public TargetObject getTargetObject() {
        return targetObject;
    }

    public Date getInvocationTimestamp() {
        return invocationTimestamp;
    }

    public InvokedBy getInvokedBy() {
        return invokedBy;
    }

    public Map<String, Object> getCommandAttributes() {
        return commandAttributes;
    }

    // sets
    public void setCommandId(CommandId commandId) {
        this.commandId = commandId;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setTargetObject(TargetObject targetObject) {
        this.targetObject = targetObject;
    }

    public void setInvocationTimestamp(Date invocationTimestamp) {
        this.invocationTimestamp = invocationTimestamp;
    }

    public void setInvokedBy(InvokedBy invokedBy) {
        this.invokedBy = invokedBy;
    }

    public void setCommandAttributes(Map<String, Object> commandAttributes) {
        this.commandAttributes = commandAttributes;
    }
}

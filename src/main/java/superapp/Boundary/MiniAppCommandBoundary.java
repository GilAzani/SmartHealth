package superapp.Boundary;

import org.springframework.data.mongodb.core.mapping.Document;
import superapp.Boundary.User.UserId;

import java.util.Date;
import java.util.Map;
public class MiniAppCommandBoundary
{
    private CommandId commandId;
    private String command;
    private ObjectId targetObject;
    private Date invocationTimestamp;
    private UserId invokedBy;
    private Map<String, Object> commandAttributes; // key-value

    public MiniAppCommandBoundary() {
    }



    public MiniAppCommandBoundary(CommandId commandId, String command, ObjectId targetObject, Date invocationTimestamp,
                                  UserId invokedBy, Map<String, Object> commandAttributes) {
        this.commandId = commandId;
        this.command = command;
        this.targetObject = targetObject;
        this.invocationTimestamp = invocationTimestamp;
        this.invokedBy = invokedBy;
        this.commandAttributes = commandAttributes;
    }

    public MiniAppCommandBoundary(String command, ObjectId targetObject, Date invocationTimestamp, UserId invokedBy,
                                  Map<String, Object> commandAttributes) {
        this(null, command, targetObject, invocationTimestamp, invokedBy, commandAttributes);
    }

    // GETS
    public CommandId getCommandId() {
        return commandId;
    }
    
    public String getCommand() {
        return command;
    }

    public ObjectId getTargetObject() {
        return targetObject;
    }

    public Date getInvocationTimestamp() {
        return invocationTimestamp;
    }

    public UserId getInvokedBy() {
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

    public void setTargetObject(ObjectId targetObject) {
        this.targetObject = targetObject;
    }

    public void setInvocationTimestamp(Date invocationTimestamp) {
        this.invocationTimestamp = invocationTimestamp;
    }

    public void setInvokedBy(UserId invokedBy) {
        this.invokedBy = invokedBy;
    }

    public void setCommandAttributes(Map<String, Object> commandAttributes) {
        this.commandAttributes = commandAttributes;
    }

}
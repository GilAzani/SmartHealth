package SuperApp.Controller;

import java.util.HashMap;
import java.util.Map;

import SuperApp.Mockup.MiniAppCommandServiceMockUp;
import SuperApp.Model.MiniAppCommandBoundary;
import SuperApp.Model.MiniAppCommandService;
import SuperApp.Model.ObjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController

public class MiniAppCommandApiController
{


    private MiniAppCommandService miniAppCommandService;

    @Autowired
    public MiniAppCommandApiController(MiniAppCommandService miniAppCommandService) {
        this.miniAppCommandService = miniAppCommandService;

    }


    // POST: Invoke MiniApp command
    @RequestMapping(
            path = {"/superapp/miniapp/{miniappname}"},
            method = {RequestMethod.POST},
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> invokeMiniApp(@PathVariable("miniappname") String miniAppName, @RequestBody MiniAppCommandBoundary miniAppCommand) {
        if (miniAppName.equals("sample-miniapp")) {
            Map<String, Object> output = new HashMap<>();
            output.put("result", "success");
            output.put("message", "Sample MiniApp command invoked successfully.");
            output.put("command", miniAppCommand);
            return output;
        }
        Map<String, Object> errorOutput = new HashMap<>();
        errorOutput.put("result", "error");
        errorOutput.put("message", "MiniApp not found.");
        return errorOutput;
    }

}

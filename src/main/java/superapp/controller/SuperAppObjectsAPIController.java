package superapp.controller;

import superapp.Boundary.*;
import superapp.logic.service.ObjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SuperAppObjectsAPIController {


	private ObjectsService objectsService;

	@Autowired
	public SuperAppObjectsAPIController(ObjectsService objectsService) {
		this.objectsService = objectsService;

	}

	
	//POST: Create Object
	@RequestMapping(
			path = {"/superapp/objects"},
			method = {RequestMethod.POST},
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	
	public ObjectBoundary createObject(@RequestBody ObjectBoundary objectBoundary) {

		return objectsService.createObject(objectBoundary);

	}
	
	//PUT: Update Object
	@RequestMapping(
			path = {"/superapp/objects/{superapp}/{internalObjectId}"},
			method = {RequestMethod.PUT},
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	
	public void updateObject(@PathVariable("superapp") String superapp,
										  @PathVariable("internalObjectId") String internalObjectid,
										  @RequestBody ObjectBoundary objectBoundary) {

			objectsService.updateObject(superapp, internalObjectid, objectBoundary);

		}

	//GET: Get Object
	@RequestMapping(
			path = {"/superapp/objects/{superapp}/{internalObjectId}"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	
	public ObjectBoundary retrieveObject(@PathVariable("superapp") String superapp,
										   @PathVariable("internalObjectId") String internalObjectId)
	{
		return objectsService.getSpecificObject(superapp, internalObjectId)
				.orElseThrow(()->new RuntimeException("could not find object with id: " + superapp + "_" + internalObjectId));
		
	}

	//GET: Get All Objects
	@RequestMapping(
			path = {"/superapp/objects"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})

	public  List<ObjectBoundary> getAllObjects()
	{
		return  objectsService.getAllObjects();
	}

}





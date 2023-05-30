package superapp.logic.Mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import superapp.Boundary.SuperAppObjectBoundary;
import superapp.Boundary.User.UserId;
import superapp.Boundary.ObjectId;
import superapp.dal.SuperAppObjectRepository;
import superapp.dal.UserRepository;
import superapp.data.UserRole;
import superapp.data.SuperAppObjectEntity;
import superapp.data.UserEntity;
import superapp.logic.Exceptions.DepreacatedOpterationException;
import superapp.logic.Exceptions.ObjectNotFoundException;
import superapp.logic.Exceptions.PermissionDeniedException;
import superapp.logic.Exceptions.UserNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import superapp.logic.service.SuperAppObjService.ObjectServicePaginationSupported;
import superapp.logic.service.SuperAppObjService.ObjectsService;
import superapp.logic.service.SuperAppObjService.ObjectsServiceWithAdminPermission;
import superapp.logic.service.SuperAppObjService.SuperAppObjectRelationshipService;
import superapp.logic.utilitys.GeneralUtility;
import java.util.*;
import java.util.stream.Collectors;

@Service
public  class ObjectServiceRepo implements ObjectsServiceWithAdminPermission, SuperAppObjectRelationshipService, ObjectServicePaginationSupported , ObjectsService {

	private final SuperAppObjectRepository objectRepository;
	private final UserRepository userRepository;//for permission checks
	private String springAppName;



	@Autowired
	public ObjectServiceRepo(SuperAppObjectRepository objectRepository,UserRepository userRepository) {

		this.objectRepository = objectRepository;
		this.userRepository = userRepository;
	}

	@Value("${spring.application.name:iAmTheDefaultNameOfTheApplication}")
	public void setSpringApplicationName(String springApplicationName) {
		this.springAppName = springApplicationName;
	}

	@Override
	public SuperAppObjectBoundary createObject(SuperAppObjectBoundary obj) throws RuntimeException {
		UserEntity userEntity = this.userRepository.findById(new UserId(obj.getCreatedBy().getUserId().getSuperapp(),obj.getCreatedBy().getUserId().getEmail()))
				.orElseThrow(()->new UserNotFoundException("inserted user not exist"));
		try {
			validateObject(obj);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e.getMessage());
		}

		if (!userEntity.getRole().equals(UserRole.SUPERAPP_USER) )
			throw new PermissionDeniedException("User do not have permission to createObject");

		obj.setObjectId(new ObjectId(springAppName, UUID.randomUUID().toString()));
		obj.setCreationTimestamp(new Date());
		SuperAppObjectEntity entity = boundaryToEntity(obj);
		entity = objectRepository.save(entity);
		return entityToBoundary(entity);
	}

	private void validateObject(SuperAppObjectBoundary obj) {
		GeneralUtility generalUtility = new GeneralUtility();
		// Check if alias is valid
		if (generalUtility.isStringEmptyOrNull(obj.getAlias())){
			throw new RuntimeException("alias is empty");
		}
		// Check if type is valid
		if (generalUtility.isStringEmptyOrNull(obj.getType())){
			throw new RuntimeException("alias is empty");
			// Check if created by is valid
		}if (generalUtility.isStringEmptyOrNull(obj.getCreatedBy().getUserId().getEmail()) ||
				generalUtility.isStringEmptyOrNull(obj.getCreatedBy().getUserId().getSuperapp()) ){
			throw new RuntimeException("created by is empty");
		}
	}
	@Override
	@Deprecated
	public SuperAppObjectBoundary updateObject(String superAppId, String internal_obj_id, SuperAppObjectBoundary update) {
		throw new DepreacatedOpterationException("do not use this operation any more, as it is deprecated");
	}

	@Override
	public SuperAppObjectBoundary updateObject(String superapp, String internal_obj_id, SuperAppObjectBoundary update, String userSuperApp, String userEmail) throws RuntimeException {
		Optional<SuperAppObjectEntity> optionalEntity = objectRepository
				.findById(new ObjectId(superapp, internal_obj_id));
		UserEntity userEntity = this.userRepository
				.findById(new UserId(userSuperApp,userEmail))
				.orElseThrow(()->new UserNotFoundException("inserted user not exist"));

		if (optionalEntity.isEmpty())
			throw new NullPointerException("internal id not exist");
		if (!userEntity.getRole().equals(UserRole.SUPERAPP_USER))
			throw new PermissionDeniedException("User do not have permission to updateObject");

		SuperAppObjectEntity entity = optionalEntity.get();
		if (update.getType() != null) {
			entity.setType(update.getType());
		}
		if (update.getAlias() != null) {
			entity.setAlias(update.getAlias());
		}
		if (update.getActive() != null) {
			entity.setActive(update.getActive());
		}
		if (update.getLocation() != null) {
			entity.setLocation(update.getLocation());
		}
		if (update.getObjectDetails() != null) {
			entity.setObjectDetails(update.getObjectDetails());
		}

		entity = objectRepository.save(entity);

		return entityToBoundary(entity);
	}

	@Override
	@Deprecated
	public Optional<SuperAppObjectBoundary> getSpecificObject(String superAppId, String internal_obj_id) {
		throw new DepreacatedOpterationException("do not use this operation any more, as it is deprecated");
	}

	@Override
	public SuperAppObjectBoundary getSpecificObject(String superAppId, String internal_obj_id, String userSuperApp, String userEmail) throws RuntimeException {
		UserEntity userEntity = this.userRepository.findByUserId(new UserId(userSuperApp,userEmail))
				.orElseThrow(()->new UserNotFoundException("inserted id: "
						+userEmail + userSuperApp + " does not exist"));
		
		SuperAppObjectEntity objectEntity = objectRepository.
				findByObjectId(new ObjectId(superAppId, internal_obj_id))
				.orElseThrow(() -> new ObjectNotFoundException("Could not find object with id: " + superAppId + "_" + internal_obj_id) );
		
		SuperAppObjectBoundary objectBoundary = entityToBoundary(objectEntity);
		if (userEntity.getRole().equals(UserRole.SUPERAPP_USER))
		{
			return objectBoundary;

		}
		else if (userEntity.getRole().equals(UserRole.MINIAPP_USER)) {
			if (objectEntity.getActive()) {
				
				return objectBoundary;
			}
			throw new ObjectNotFoundException("User does not have permission to getSpecificObject");
		}
		throw new PermissionDeniedException("User do not have permission to getSpecificObject");
	}
	
	

	@Override
	public List<SuperAppObjectBoundary> getAllObjects(int size, int page) {
		return null;
	}


	@Override
	@Deprecated
	public List<SuperAppObjectBoundary> getAllObjects() {
		throw new DepreacatedOpterationException("do not use this operation any more, as it is deprecated");
	}

	//pagination Support

	@Override
	public List<SuperAppObjectBoundary> getAllObjects(String userSuperapp, String userEmail, int size, int page) {
		UserEntity userEntity = this.userRepository.findById(new UserId(userSuperapp, userEmail))
				.orElseThrow(() -> new UserNotFoundException("inserted id: "
						+ userSuperapp + "_" + userEmail + " does not exist"));

		if (userEntity.getRole().equals(UserRole.SUPERAPP_USER)) {
			return this.objectRepository
					.findAll(PageRequest.of(page, size, Sort
							.Direction.DESC, "creationTimestamp", "_id"))
					.stream()
					.map(this::entityToBoundary)
					.toList();
		} else if (userEntity.getRole().equals(UserRole.MINIAPP_USER)) {
			return getActiveObjects(page, size);
		} else {
			throw new PermissionDeniedException("User do not have permission to get all objects");
		}
	}


	@Override
	@Deprecated
	public void deleteAllObjects() {
		objectRepository.deleteAll();
	}

	@Override
	public void deleteAllObjects(UserId userId) {
		UserEntity userEntity = this.userRepository.findById(userId)
				.orElseThrow(()->new UserNotFoundException("inserted id: "
						+ userId + " does not exist"));

		if (!userEntity.getRole().equals(UserRole.ADMIN)) {
			throw new PermissionDeniedException("You do not have permission to delete all objects");
		}
		this.objectRepository.deleteAll();
	}


	@Override
	public void bindParentAndChild(String parentId, String childId, String userSuperApp, String userEmail) throws RuntimeException {
		UserEntity userEntity = this.userRepository.findById(new UserId(userSuperApp,userEmail))
				.orElseThrow(()->new UserNotFoundException("inserted id: "
						+ userEmail + userSuperApp + " does not exist"));
		if (childId.equals(parentId)){
			throw new RuntimeException("can't bind the same object");
		}
		if(!userEntity.getRole().equals(UserRole.SUPERAPP_USER))
			throw new PermissionDeniedException("User do not have permission to bindParentAndChild Objects");

		SuperAppObjectEntity parent  = objectRepository.findById(new ObjectId(springAppName, parentId)).orElseThrow(()
				-> new ObjectNotFoundException("could not find object with id: "+parentId ));
		SuperAppObjectEntity child = objectRepository.findById(new ObjectId(springAppName, childId)).orElseThrow(()
				-> new ObjectNotFoundException("could not find object with id: " +childId));

		boolean isChildAlreadyAssociated = parent.getChildObjects().stream()
				.anyMatch(existingChild -> existingChild.equals(child));

		if (!isChildAlreadyAssociated) {
			parent.getChildObjects().add(child);
			child.getParentObjects().add(parent);
			objectRepository.save(child);
			objectRepository.save(parent);
		}else {
			throw new RuntimeException("child and parent already bind");
		}
	}


	public List<SuperAppObjectBoundary> getActiveObjects(int page, int size) {
		List<SuperAppObjectEntity> activeObjects = this.objectRepository.findByActiveIsTrue();
		PageRequest pageRequest = PageRequest.of(page, size, Sort
				.Direction.DESC, "creationTimestamp", "type");

		List<SuperAppObjectEntity> objectsOnPage = getPage(activeObjects, pageRequest);
		List<SuperAppObjectBoundary> activeObjectBoundaries = objectsOnPage.stream()
				.map(this::entityToBoundary)
				.toList();
		return activeObjectBoundaries;
	}

	private List<SuperAppObjectEntity> getPage(List<SuperAppObjectEntity> objects, PageRequest pageRequest) {
		int startIndex = pageRequest.getPageNumber() * pageRequest.getPageSize();
		int endIndex = Math.min(startIndex + pageRequest.getPageSize(), objects.size());
		return objects.subList(startIndex, endIndex);
	}




	@Deprecated
	public Set<SuperAppObjectBoundary> getAllChildren(String objectId) {
		throw  new DepreacatedOpterationException("dont use this func ");
	}

	@Override
	public List<SuperAppObjectBoundary> getAllChildren(String internalObjectId, String userSuperApp, String userEmail, int size, int page) {

		UserEntity userEntity = this.userRepository.findByUserId(new UserId(userSuperApp, userEmail))
				.orElseThrow(() -> new UserNotFoundException("Inserted ID: " + userEmail + userSuperApp + " does not exist"));

		SuperAppObjectEntity parent = objectRepository.findById(new ObjectId(springAppName, internalObjectId))
				.orElseThrow(() -> new ObjectNotFoundException("Object not found"));


		if (userEntity.getRole().equals(UserRole.SUPERAPP_USER)) {

			return this.objectRepository
					.findByParentObjects_ObjectId(parent.getObjectId(), PageRequest.of(page, size, Sort
							.Direction.ASC , "creationTimestamp", "_id"))
					.stream()
					.map(this::entityToBoundary)
					.toList();
			
			
		}
		else if (userEntity.getRole().equals(UserRole.MINIAPP_USER))
		{
			return this.objectRepository
					.findByParentObjects_ObjectIdAndActiveIsTrue(parent.getObjectId(),PageRequest.of(page, size, Sort
							.Direction.ASC , "creationTimestamp", "_id"))
					.stream()
					.map(this::entityToBoundary)
					.toList();

		}
		throw new PermissionDeniedException("User do not have permission to get all children");
	}

	@Override
	public List<SuperAppObjectBoundary> getAllParents(String internalObjectId, String userSuperapp, String userEmail, int size, int page) {
		SuperAppObjectEntity child = objectRepository.findById(new ObjectId(springAppName, internalObjectId))
				.orElseThrow(() -> new ObjectNotFoundException("Object not found"));
		
		UserEntity userEntity = this.userRepository.findById(new UserId(userSuperapp, userEmail))
				.orElseThrow(() -> new UserNotFoundException("inserted id: "
						+ userEmail + userSuperapp + " does not exist"));
	

		if (userEntity.getRole().equals(UserRole.SUPERAPP_USER)) {
			return this.objectRepository
					.findByChildObjects_ObjectId(child.getObjectId(), PageRequest.of(page, size, Sort
							.Direction.ASC , "creationTimestamp", "_id"))
					.stream()
					.map(this::entityToBoundary)
					.toList();
			
		} else if (userEntity.getRole().equals(UserRole.MINIAPP_USER)) {
			return this.objectRepository
					.findByChildObjects_ObjectIdAndActiveIsTrue(child.getObjectId(), PageRequest.of(page, size, Sort
							.Direction.ASC , "creationTimestamp", "_id"))
					.stream()
					.map(this::entityToBoundary)
					.toList();
		}
		throw new PermissionDeniedException("User do not have permission to get all parents");
	}


	public List<SuperAppObjectBoundary> searchByAlias(String alias, String userSuperapp, String userEmail, int size, int page) {
		UserEntity userEntity = this.userRepository.findByUserId(new UserId(userSuperapp, userEmail))
				.orElseThrow(() -> new UserNotFoundException("inserted id: "
						+ userEmail + userSuperapp + " does not exist"));

		PageRequest pageRequest = PageRequest.of(page, size , Sort.Direction.ASC,"_id");
		Page<SuperAppObjectEntity> objectPage = objectRepository.findByAlias(alias, pageRequest);

		if (userEntity.getRole().equals(UserRole.SUPERAPP_USER)) {
			return objectPage.getContent()
					.stream()
					.map(this::entityToBoundary) // Convert SuperAppObjectEntity to superAppObjectBoundary
					.collect(Collectors.toList());
		} else if (userEntity.getRole().equals(UserRole.MINIAPP_USER)){
			return getActiveObjects(page, size);
		} else {
			throw new PermissionDeniedException("User do not have permission to search by alias");
		}
	}

	public List<SuperAppObjectBoundary> searchByType(String alias, String userSuperapp, String userEmail, int size, int page) {
		
		PageRequest pageRequest = PageRequest.of(page, size,  Sort.Direction.ASC,"_id");
		Page<SuperAppObjectEntity> objectPage = objectRepository.searchByType(alias, pageRequest);

		return objectPage.getContent()
				.stream()
				.map(this::entityToBoundary) // Convert SuperAppObjectEntity to superAppObjectBoundary
				.collect(Collectors.toList());
	}

	public List<SuperAppObjectBoundary> searchByLocation(double latitude, double longitude, double distance, String distanceUnits, String superapp, String email, int size, int page) {
		// Convert distance units as needed
		double radius;

		if (distanceUnits.equalsIgnoreCase("KILOMETERS")) {
			radius = distance; // Distance is already in kilometers
		} else if (distanceUnits.equalsIgnoreCase("MILES")) {
			radius = distance * 1.60934; // Convert distance to kilometers
		} else {
			radius = distance; // Assume neutral units or invalid value (treated as kilometers)
		}

		// Perform the search by location implementation
		List<SuperAppObjectBoundary> results = new ArrayList<>();
		for (SuperAppObjectEntity entity : objectRepository.findAll()) {
			double objectLat = entity.getLocation().getLat();
			double objectLng = entity.getLocation().getLng();

			double objectDistance = calculateDistance(latitude, longitude, objectLat, objectLng);

			if (objectDistance <= radius) {
				results.add(entityToBoundary(entity));
			}
		}
		return results;
	}

	private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
		// Calculate the distance between two points using the Haversine formula
		double earthRadius = 6371; // Earth's radius in kilometers

		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
				Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
				Math.sin(dLng / 2) * Math.sin(dLng / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return earthRadius * c;
	}

	public SuperAppObjectBoundary entityToBoundary(SuperAppObjectEntity entity) {
		SuperAppObjectBoundary obj = new SuperAppObjectBoundary();

		// convert entity to boundary
		obj.setObjectId(entity.getObjectId());
		obj.setActive(entity.getActive());
		obj.setCreatedBy(entity.getCreatedBy());
		obj.setAlias(entity.getAlias());
		obj.setObjectDetails(entity.getObjectDetails());
		obj.setCreationTimestamp(entity.getCreationTimestamp());
		obj.setType(entity.getType());
		obj.setLocation(entity.getLocation());
		return obj;
	}


	public SuperAppObjectEntity boundaryToEntity (SuperAppObjectBoundary obj) {
		SuperAppObjectEntity rv = new SuperAppObjectEntity();

		rv.setObjectId(obj.getObjectId());
		rv.setActive(obj.getActive());
		rv.setLocation(obj.getLocation());
		rv.setType(obj.getType());
		rv.setObjectDetails(obj.getObjectDetails());
		rv.setAlias(obj.getAlias());
		rv.setCreatedBy(obj.getCreatedBy());
		rv.setCreationTimestamp(obj.getCreationTimestamp());

		return rv;
	}
}

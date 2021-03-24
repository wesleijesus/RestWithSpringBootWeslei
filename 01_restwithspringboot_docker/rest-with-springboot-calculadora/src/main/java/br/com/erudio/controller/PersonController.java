package br.com.erudio.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.data.vo.PersonVO;
import br.com.erudio.services.PersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Person Endpoint", description = "description for person", tags = {"PersonEndPoint"})
@RestController
@RequestMapping("api/person/v1")
public class PersonController {
	
	@Autowired
	private PersonService service;
	
	@ApiOperation (value = "Find all people recorded")
	@GetMapping (produces = {"application/json", "application/xml", "application/x-yaml"})
	//@RequestMapping(method=RequestMethod.GET) mesma coisa que getMapping
		//	produces=MediaType.APPLICATION_JSON_VALUE) ja esta no pacote do Spring Boot
//	public List<PersonVO> findAll(
//			@RequestParam(value="page", defaultValue = "0") int page,
//			@RequestParam(value="limit", defaultValue = "12") int limit,
//			@RequestParam(value="direction", defaultValue = "asc") String direction) {
//		
//		Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
//		
//		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));
//		
//		List<PersonVO> persons = service.findAll(pageable);
//		persons
//			.stream()
//			.forEach(p -> p.add(
//					linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel())
//					);
//	//	return service.findAll();
//		return persons;
//		
//	}
	
	public ResponseEntity<PagedResources<PersonVO>> findAll(
			@RequestParam(value="page", defaultValue = "0") int page,
			@RequestParam(value="limit", defaultValue = "12") int limit,
			@RequestParam(value="direction", defaultValue = "asc") String direction,
			PagedResourcesAssembler assembler) {
		
		Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));
		
		Page<PersonVO> persons = service.findAll(pageable);
		persons
			.stream()
			.forEach(p -> p.add(
					linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel())
					);
	//	return service.findAll();
		return new ResponseEntity<>(assembler.toResource(persons), HttpStatus.OK);
		
	}
	
	@ApiOperation (value = "Find all people recorded by token name")
	@GetMapping (value = "/findPersonByName/{firstName}",
				produces = {"application/json", "application/xml", "application/x-yaml"})

	public ResponseEntity<PagedResources<PersonVO>> findPersonByName(
			@PathVariable ("firstName") String firstName,
			@RequestParam(value="page", defaultValue = "0") int page,
			@RequestParam(value="limit", defaultValue = "12") int limit,
			@RequestParam(value="direction", defaultValue = "asc") String direction,
			PagedResourcesAssembler assembler) {
		
		Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));
		
		Page<PersonVO> persons = service.findPersonByName(firstName,pageable);
		persons
			.stream()
			.forEach(p -> p.add(
					linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel())
					);
	//	return service.findAll();
		return new ResponseEntity<>(assembler.toResource(persons), HttpStatus.OK);
		
	}
	
	@ApiOperation (value = "Find a person ")
	@GetMapping(value = "/{id}", produces = {"application/json","application/xml", "application/x-yaml"})
	//@RequestMapping(value="/{Id}", 
		//	method=RequestMethod.GET)
		//	produces=MediaType.APPLICATION_JSON_VALUE)
	public PersonVO findById(@PathVariable("id") Long id) {
		PersonVO personVO = service.findById(id);
		personVO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return personVO;
	//	return service.findById(Id);
		
	}
	
	@ApiOperation (value = "Disable a person ")
	@PatchMapping(value = "/{id}", produces = {"application/json","application/xml", "application/x-yaml"})
	//@RequestMapping(value="/{Id}", 
		//	method=RequestMethod.GET)
		//	produces=MediaType.APPLICATION_JSON_VALUE)
	public PersonVO disablePerson(@PathVariable("id") Long id) {
		PersonVO personVO = service.disablePerson(id);
		personVO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return personVO;
	//	return service.findById(Id);
		
	}
	
	@ApiOperation (value = "Create a person ")
	@PostMapping (produces = {"application/json","application/xml", "application/x-yaml"},
				  consumes = {"application/json", "application/xml", "application/x-yaml"})
	//@RequestMapping(method=RequestMethod.POST)
		//	consumes=MediaType.APPLICATION_JSON_VALUE,
		//	produces=MediaType.APPLICATION_JSON_VALUE)
	public PersonVO create(@RequestBody PersonVO person) {
		

		return service.create(person);
		
	}
	
	@ApiOperation (value = "Update a person ")
	@PutMapping
//	@RequestMapping(method=RequestMethod.PUT)
			//consumes=MediaType.APPLICATION_JSON_VALUE,
			//produces=MediaType.APPLICATION_JSON_VALUE)
	public PersonVO update(@RequestBody PersonVO person) {
		

		return service.update(person);
		
	}
	@ApiOperation (value = "Delete a person ")
	@DeleteMapping("/{Id}")
//	@RequestMapping(value="/{Id}", 
//			method=RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("Id") Long Id) {
		

		service.delete(Id);
		
		return ResponseEntity.ok().build();
		
	}
	


}

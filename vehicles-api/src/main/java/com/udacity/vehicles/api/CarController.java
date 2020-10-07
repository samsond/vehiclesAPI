package com.udacity.vehicles.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.service.CarService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implements a REST-based controller for the Vehicles API.
 */
@RestController
@RequestMapping("/cars")
@ApiResponses(value = {
        @ApiResponse(responseCode="400", description = "This is a bad request, please follow the API documentation for the proper request format."),
        @ApiResponse(responseCode="401", description = "Due to security constraints, your access request cannot be authorized. "),
        @ApiResponse(responseCode="500", description = "The server is down. Please make sure that the Location microservice is running.")
})
@Tag(name = "Car", description = "cars api")
class CarController {

    private final CarService carService;
    private final CarResourceAssembler assembler;

    CarController(CarService carService, CarResourceAssembler assembler) {
        this.carService = carService;
        this.assembler = assembler;
    }

    /**
     * Creates a list to store any vehicles.
     * @return list of vehicles
     */
    @GetMapping
    CollectionModel<EntityModel<Car>> list() {
        List<EntityModel<Car>> resources = carService.list().stream().map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(resources,
                linkTo(methodOn(CarController.class).list()).withSelfRel());
    }

    /**
     * Gets information of a specific car by ID.
     * @param id the id number of the given vehicle
     * @return all information for the requested vehicle
     */
    @GetMapping("/{id}")
    EntityModel<Car> get(@PathVariable Long id) {
        /**
         * COMPLETED: Use the `findById` method from the Car Service to get car information.
         * COMPLETED: Use the `assembler` on that car and return the resulting output.
         *   Update the first line as part of the above implementing.
         */
        return assembler.toModel(carService.findById(id));
    }

    /**
     * Posts information to create a new vehicle in the system.
     * @param car A new vehicle to add to the system.
     * @return response that the new vehicle was added to the system
     * @throws URISyntaxException if the request contains invalid fields or syntax
     */
    @PostMapping
    ResponseEntity<?> post(@Valid @RequestBody Car car) throws URISyntaxException {
        /**
         * COMPLETED: Use the `save` method from the Car Service to save the input car.
         * COMPLETED: Use the `assembler` on that saved car and return as part of the response.
         *   Update the first line as part of the above implementing.
         */
        EntityModel<Car> resource = assembler.toModel(carService.save(car));
        // return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
        return ResponseEntity.created(new URI(resource.getContent().getId().toString())).body(resource);

    }

    /**
     * Updates the information of a vehicle in the system.
     * @param id The ID number for which to update vehicle information.
     * @param car The updated information about the related vehicle.
     * @return response that the vehicle was updated in the system
     */
    @PutMapping("/{id}")
    ResponseEntity<?> put(@PathVariable Long id, @Valid @RequestBody Car car) {
        /**
         * COMPLETED: Set the id of the input car object to the `id` input.
         * COMPLETED: Save the car using the `save` method from the Car service
         * COMPLETED: Use the `assembler` on that updated car and return as part of the response.
         *   Update the first line as part of the above implementing.
         */
        car.setId(id);
        EntityModel<Car> resource = assembler.toModel(carService.save(car));
        return ResponseEntity.ok(resource);
    }

    /**
     * Removes a vehicle from the system.
     * @param id The ID number of the vehicle to remove.
     * @return response that the related vehicle is no longer in the system
     */
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        /**
         * COMPLETED: Use the Car Service to delete the requested vehicle.
         */
        carService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

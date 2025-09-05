package com.openclassrooms.rentals.controllers;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

/**
 * Abstract base class providing utility methods for controller response handling.
 *
 * This class simplifies the creation of {@link ResponseEntity} objects from {@link Optional} values,
 * allowing for cleaner and more expressive controller logic.
 *
 * Key features:
 * <ul>
 *   <li>Standardized handling of optional responses</li>
 *   <li>Support for custom HTTP status codes</li>
 *   <li>Flexible response building using functional interfaces</li>
 * </ul>
 *
 * Intended to be extended by REST controllers to reduce boilerplate code
 * when dealing with optional service results.
 *
 * @author Pagès
 */
public class AbstractController {

    /**
     * Builds a {@link ResponseEntity} from an {@link Optional} value.
     * <p>
     * Returns HTTP 200 OK with the value if present, or HTTP 404 Not Found if empty.
     *
     * @param object the optional value to wrap
     * @param <T> the type of the response body
     * @return a {@code ResponseEntity} containing the value or a 404 response
     */
    public <T> ResponseEntity<T> responseFromOptional(Optional<T> object) {
        return object.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Builds a {@link ResponseEntity} from an {@link Optional} value with a custom status code.
     * <p>
     * Returns HTTP 200 OK with the value if present, or the provided status code if empty.
     *
     * @param object the optional value to wrap
     * @param responseCode the HTTP status code to use if the value is absent
     * @param <T> the type of the response body
     * @return a {@code ResponseEntity} containing the value or a custom status response
     */
    public <T> ResponseEntity<T> responseFromOptional(Optional<T> object, HttpStatusCode responseCode) {
        return object.map(ResponseEntity::ok).orElse(ResponseEntity.status(responseCode).build());
    }

    /**
     * Builds a {@link ResponseEntity} from an {@link Optional} value using a custom builder function.
     * <p>
     * Returns the result of the builder function if the value is present, or HTTP 404 Not Found if empty.
     *
     * @param object the optional value to wrap
     * @param builder a function that transforms the value into a {@code ResponseEntity}
     * @param <T> the type of the input value
     * @param <U> the type of the response body
     * @return a {@code ResponseEntity} built from the value or a 404 response
     */
    public <T, U> ResponseEntity<U> responseFromOptional(Optional<T> object, Function<T, ResponseEntity<U>> builder) {
        return object.map(builder).orElse(ResponseEntity.notFound().build());
    }
}
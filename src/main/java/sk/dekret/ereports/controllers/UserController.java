package sk.dekret.ereports.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.dekret.ereports.exceptions.EReportsException;
import sk.dekret.ereports.models.UserAccount;
import sk.dekret.ereports.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Retrieves all users.
     *
     * @return list of {@link UserAccount}
     */
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Retrieves all users.")
    public ResponseEntity<List<UserAccount>> findAll() {
        return ResponseEntity.ok(this.userService.findAll());
    }

    /**
     * Retrieves a user based on id.
     *
     * @param id of user
     * @return {@link UserAccount}
     */
    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Retrieves a user based on id.")
    public ResponseEntity<UserAccount> findUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.userService.findById(id));
    }

    /**
     * Creates a new user account.
     *
     * @param userAccount representing information about user account
     * @return {@link UserAccount} if user account created successfully
     * @throws EReportsException thrown if username already exists
     */
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Creates a new user account.")
    public ResponseEntity<UserAccount> createUser(@Valid @RequestBody UserAccount userAccount) throws EReportsException {
        return new ResponseEntity<>(this.userService.createUser(userAccount), HttpStatus.CREATED);
    }

    /**
     * Updates existing user account by id.
     *
     * @param id    of {@link UserAccount}
     * @param model representing data to be changed
     * @return updated model
     * @throws EReportsException if user account does not exist for the id
     */
    @PutMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Updates existing user account by id.")
    public ResponseEntity<UserAccount> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserAccount model) throws EReportsException {
        return ResponseEntity.ok(this.userService.updateUser(id, model));
    }

    /**
     * Deletes user account by id.
     *
     * @param id of {@link UserAccount}
     * @return true if user account successfully deleted
     * @throws EReportsException if user account does not exist for the id
     */
    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Deletes user account by id.")
    public ResponseEntity<Boolean> deleteUser(@PathVariable("id") Long id) throws EReportsException {
        return ResponseEntity.ok(this.userService.deleteUser(id));
    }
}

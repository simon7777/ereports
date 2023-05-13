package sk.dekret.ereports.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.dekret.ereports.db.entities.UserAccount;
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
    @GetMapping
    public ResponseEntity<List<UserAccount>> findAll() {
        return ResponseEntity.ok(this.userService.findAll());
    }
}

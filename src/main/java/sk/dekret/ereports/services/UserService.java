package sk.dekret.ereports.services;

import org.springframework.stereotype.Service;
import sk.dekret.ereports.db.entities.UserAccount;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    public List<UserAccount> findAll() {
        return Collections.emptyList();
    }
}

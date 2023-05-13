package sk.dekret.ereports.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sk.dekret.ereports.db.entities.UserAccount;
import sk.dekret.ereports.exceptions.EReportsException;
import sk.dekret.ereports.mappers.UserAccountMapper;
import sk.dekret.ereports.repositories.UserAccountRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder encoder;

    public List<sk.dekret.ereports.models.UserAccount> findAll() {
        return userAccountRepository.findAll().stream()
                .map(UserAccountMapper::toModel).toList();
    }

    public sk.dekret.ereports.models.UserAccount findById(Long id) {
        return UserAccountMapper.toModel(userAccountRepository.findById(id).orElse(null));
    }

    public sk.dekret.ereports.models.UserAccount createUser(sk.dekret.ereports.models.UserAccount userAccount) throws EReportsException {
        if (existsUserByUsername(userAccount.getUsername())) {
            throw new EReportsException(EReportsException.EReportsErrors.USER_WITH_NAME_ALREADY_EXISTS);
        }

        UserAccount entity = UserAccountMapper.toEntity(userAccount, new UserAccount());
        entity.setPassword(encodePassword(userAccount.getPassword()));

        return UserAccountMapper.toModel(entity);
    }

    public sk.dekret.ereports.models.UserAccount updateUser(Long id, sk.dekret.ereports.models.UserAccount model) throws EReportsException {
        UserAccount entity = this.loadEntityById(id);

        entity = UserAccountMapper.toEntity(model, entity);

        return UserAccountMapper.toModel(this.userAccountRepository.save(entity));
    }

    public Boolean deleteUser(Long id) throws EReportsException {
        UserAccount entity = this.loadEntityById(id);

        this.userAccountRepository.delete(entity);

        return Boolean.TRUE;
    }

    private boolean existsUserByUsername(String username) {
        return userAccountRepository.findByUsername(username).isPresent();
    }

    private String encodePassword(String password) {
        return this.encoder.encode(password);
    }

    private UserAccount loadEntityById(Long id) throws EReportsException {
        return this.userAccountRepository.findById(id)
                .orElseThrow(() -> new EReportsException(EReportsException.EReportsErrors.USER_WITH_ID_DOES_NOT_EXIST));
    }
}

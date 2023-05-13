package sk.dekret.ereports.mappers;

import lombok.experimental.UtilityClass;
import sk.dekret.ereports.models.UserAccount;

@UtilityClass
public class UserAccountMapper {

    public static UserAccount toModel(sk.dekret.ereports.db.entities.UserAccount entity) {
        if (entity != null) {
            UserAccount model = new UserAccount();

            model.setId(entity.getId());
            model.setFirstName(entity.getFirstName());
            model.setLastName(entity.getLastName());
            model.setUsername(entity.getUsername());
            model.setRole(entity.getRole());

            return model;
        }

        return null;
    }

    public static sk.dekret.ereports.db.entities.UserAccount toEntity(UserAccount model, sk.dekret.ereports.db.entities.UserAccount entity) {
        entity.setUsername(model.getUsername());
        entity.setFirstName(model.getFirstName());
        entity.setLastName(model.getLastName());
        entity.setRole(model.getRole());

        return entity;
    }
}

package sk.dekret.ereports.mappers;

import lombok.experimental.UtilityClass;
import sk.dekret.ereports.models.Report;

import java.time.LocalDate;

@UtilityClass
public class ReportMapper {

    public static Report toModel(sk.dekret.ereports.db.entities.Report entity) {
        if (entity != null) {
            Report model = new Report();

            model.setId(entity.getId());
            model.setDate(entity.getDate().toString());
            model.setFrom(entity.getFrom());
            model.setTo(entity.getTo());
            model.setActivity(entity.getActivity());
            model.setProject(entity.getProject());
            model.setUserAccountId(entity.getUserAccount().getId());

            return model;
        }

        return null;
    }

    public static sk.dekret.ereports.db.entities.Report toEntity(Report model, sk.dekret.ereports.db.entities.Report entity) {
        entity.setProject(model.getProject());
        entity.setActivity(model.getActivity());
        entity.setDate(LocalDate.parse(model.getDate()));
        entity.setFrom(model.getFrom());
        entity.setTo(model.getTo());

        return entity;
    }
}

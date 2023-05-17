package sk.dekret.ereports.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseResultList<T> {
    List<T> items;
    Long totalItems;

    public ResponseResultList(List<T> items) {
        this.items = items;
    }

    public ResponseResultList(List<T> items, Long totalItems) {
        this.items = items;
        this.totalItems = totalItems;
    }
}

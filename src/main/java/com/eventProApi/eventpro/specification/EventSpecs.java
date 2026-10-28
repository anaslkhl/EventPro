package com.eventProApi.eventpro.specification;

import com.eventProApi.eventpro.entity.Event;
import com.eventProApi.eventpro.entity.TicketCategoriesPerEvent;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventSpecs {


    public static Specification<Event> search(String title , String categoryName){

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if(title != null && !title.isBlank()){

                predicates.add(
                        criteriaBuilder.like(criteriaBuilder
                                .lower(root.get("title")), "%" + title.trim().toLowerCase() + "%")
                );
            }

            if(categoryName != null && !categoryName.isBlank()) {

                var subquery = query.subquery(java.util.UUID.class);
                var categoryPerEvent = subquery.from(TicketCategoriesPerEvent.class);

                subquery.select(categoryPerEvent.get("event").get("id"))
                        .where(criteriaBuilder.like(
                                criteriaBuilder.lower(categoryPerEvent.get("category").get("name")),
                                "%" + categoryName.trim().toLowerCase(Locale.ROOT) + "%"
                        ));
                predicates.add(root.get("id").in(subquery));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

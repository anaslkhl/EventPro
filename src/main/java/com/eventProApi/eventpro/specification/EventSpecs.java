package com.eventProApi.eventpro.specification;

import com.eventProApi.eventpro.entity.Event;
import com.eventProApi.eventpro.entity.TicketCategoriesPerEvent;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class EventSpecs {


    public static Specification<Event> search(String title , String categoryName){


        return (root, query, criteriaBuilder) -> {


            List<Predicate> predicates = new ArrayList<>();

            if(title != null && !title.isBlank()){



                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(
                                root.get("title")
                        ),"%" + title.trim().toLowerCase() + "%"
                ));

            }

            if(categoryName != null && !categoryName.isBlank()){


                var rootcategory = root.join("categories");

                var category = rootcategory.join("category");


                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        category.get("name")
                                ), "%" + categoryName.trim() + "%"
                        )
                );


            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };


    }
}






package org.couchbase.quickstart.models.beersample;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BeerRequest {

    public String name;
    public double abv;
    public int ibu;
    public int srm;
    public int upc;
    public String type;
    public String brewery_id;
    public String updated;
    public String description;
    public String style;
    public String category;

    public Beer getBeer() {
        return Beer.builder()
                .id(UUID.randomUUID().toString())
                .name(name)
                .abv(abv)
                .ibu(ibu)
                .srm(srm)
                .upc(upc)
                .type(type)
                .brewery_id(brewery_id)
                .updated(updated)
                .description(description)
                .style(style)
                .category(category)
                .build();
    }
}

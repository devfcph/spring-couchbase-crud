package org.couchbase.quickstart.models.beersample;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Beer {
    public String id;

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
}

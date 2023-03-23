package org.couchbase.quickstart.models.gamesimsample;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GamesinRequest {
    public int experience;
    public int hitpoints;
    public String jsonType;
    public int level;
    public boolean loggedIn;
    public String name;
    public String uuid;
}

package es.iesclaradelrey.dm2e.ut02.actividad.entities;

import lombok.*;
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class Genre {
    private Integer genreId;
    private String name;

    public Genre(String name) {
        this.genreId = null;
        this.name = name;
    }


}

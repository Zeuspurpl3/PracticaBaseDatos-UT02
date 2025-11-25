package es.iesclaradelrey.dm2e.ut02.actividad.entities;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una lista de reproducción en la base de datos.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlayList {

    private Integer playListId;
    private String name;

    @Builder.Default
    private List<PlayListTrack> playListTracks = new ArrayList<>();

    public PlayList(String name) {
        this.playListId = null;
        this.name = name;
    }
}

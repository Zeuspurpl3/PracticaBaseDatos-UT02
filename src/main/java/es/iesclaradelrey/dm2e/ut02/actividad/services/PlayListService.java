package es.iesclaradelrey.dm2e.ut02.actividad.services;

import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayList;

import java.util.Optional;

public interface PlayListService {
    Optional<PlayList> findById(int id);
    PlayList save(PlayList playList);
    boolean delete(int id);
}

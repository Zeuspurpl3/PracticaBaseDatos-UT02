package es.iesclaradelrey.dm2e.ut02.actividad.services;

import es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.PlayListDataAccess;
import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayList;

import java.util.Optional;

public class PlayListServiceImpl implements PlayListService {
    private final PlayListDataAccess dataAccess;

    public PlayListServiceImpl(PlayListDataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @Override
    public Optional<PlayList> findById(int id) {
        try{
           return dataAccess.findById(id);
        }catch (Exception e){
            throw new RuntimeException("Error al buscar la playlist con id " + id, e);
        }
    }

    @Override
    public PlayList save(PlayList playList) {
        try{
            return  dataAccess.save(playList);
        }catch (Exception e){
            throw new RuntimeException("Error al guardar la playlist" + playList.getName(), e);
        }
    }

    @Override
    public boolean delete(int id) {
        try{
            return dataAccess.delete(id);
        }catch (Exception e){
            throw new RuntimeException("Error al eliminar la playlist con id" + id, e);
        }
    }
}

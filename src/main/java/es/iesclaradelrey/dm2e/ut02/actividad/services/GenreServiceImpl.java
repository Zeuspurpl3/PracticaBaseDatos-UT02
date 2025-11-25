package es.iesclaradelrey.dm2e.ut02.actividad.services;

import es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.GenreDataAccess;
import es.iesclaradelrey.dm2e.ut02.actividad.entities.Genre;

import java.util.List;
import java.util.Optional;

public class GenreServiceImpl implements GenreService {
    private final GenreDataAccess genreDataAccess;

    public GenreServiceImpl(GenreDataAccess genreDataAccess) {
        this.genreDataAccess = genreDataAccess;
    }

    @Override
    public List<Genre> findAll(){
        return genreDataAccess.findAll();
    }

    @Override
    public List<Genre> findByName(String name){
        return genreDataAccess.findByName(name);
    }

    @Override
    public Optional<Genre> findById(int id) {
        return genreDataAccess.findById(id);
    }

    @Override
    public boolean existsById(int id) {
        return genreDataAccess.existsById(id);
    }

    @Override
    public Genre save(Genre genre) {

        return genreDataAccess.save(genre);
    }

    @Override
    public boolean delete(int id) {
        return genreDataAccess.delete(id);
    }

}

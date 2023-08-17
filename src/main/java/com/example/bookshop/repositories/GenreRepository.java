package com.example.bookshop.repositories;

import com.example.bookshop.model.entity.genre.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {


    @Query(value = "with recursive r (id, parent_id, name, slug) as " +
            "(select id, parent_id, name, slug from genre " +
            " where id = :id " +
            "union all " +
            "select g.id, g.parent_id, g.name, g.slug from r " +
            "inner join genre g " +
            "on r.id = g.parent_id) select * from r", nativeQuery = true)
    List<Genre> getGenreEntitiesById(@Param("id") int id);

    Genre findGenreEntityBySlug(String slug);

    @Query(value = "select * from genre where parent_id is null ", nativeQuery = true)
    List<Genre> getParentGenreEntities();
}

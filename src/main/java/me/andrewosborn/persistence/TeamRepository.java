package me.andrewosborn.persistence;

import me.andrewosborn.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long>
{
    Team findOneByUrlName(String urlName);

    Team findOneByName(String name);

    List<Team> findAllByOrderByRpiDesc();
}

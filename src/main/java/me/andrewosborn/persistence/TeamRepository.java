package me.andrewosborn.persistence;

import me.andrewosborn.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long>
{
    Team findOneByUrlName(String urlName);

    Team findOneByName(String name);

    List<Team> findAllByOrderByRpiDesc();

    List<Team> findAllByOrderByNameAsc();

    List<Team> findAllByOrderByStrengthOfScheduleDesc();

    List<Team> findTop10ByOrderByQuadOneWinsDesc();

    @Query(nativeQuery = true, value = "select * from team order by (quad_one_wins + quad_two_wins) DESC LIMIT  10")
    List<Team> findTop10ByQuadOneWinsPlusQuadTwoWinsDesc();
}

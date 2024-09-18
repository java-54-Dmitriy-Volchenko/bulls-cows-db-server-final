package telran.net.games;
import java.time.*;
import java.util.List;

import jakarta.persistence.*;
public class JpqlQueriesRepository {
	private EntityManager em;

	public JpqlQueriesRepository(EntityManager em) {
		this.em = em;
	}
	public List<Game> getGamesFinished(boolean isFinished) {
		TypedQuery<Game> query = em.createQuery(
				"select game from Game game where is_finished=?1",
				Game.class);
		List<Game> res = query.setParameter(1, isFinished).getResultList();
		return res;
	}
	public List<DateTimeSequence> getDateTimeSequence(LocalTime time) {
		TypedQuery<DateTimeSequence> query =
				em.createQuery("select date, sequence "
						+ "from Game where cast(date as time) < :time",
						DateTimeSequence.class);
		List<DateTimeSequence> res = query.setParameter("time", time).getResultList();
		return res;
	}
	public List<Integer> getBullsInMovesGamersBornAfter(LocalDate afterDate) {
		TypedQuery<Integer> query = em.createQuery(
				"select bulls from Move where gameGamer.gamer.birthdate > "
				+ "?1", Integer.class);
		List<Integer> res = query.setParameter(1, afterDate).getResultList();
		return res;
		
	}
	public List<MinMaxAmount> getDistributionGamesMoves(int interval) {
		TypedQuery<MinMaxAmount> query = em.createQuery(
				"select floor(game_moves / :interval) * :interval as min_moves, "
				+ "floor(game_moves / :interval) * :interval + (:interval - 1) as max_moves, "
				+ "count(*) as amount "
				+ "from "
				+ "(select count(*) as game_moves from Move "
				+ "group by gameGamer.game.id) "
				+ "group by min_moves, max_moves order by min_moves", MinMaxAmount.class);
		List<MinMaxAmount> res = query.setParameter("interval", interval).getResultList();
		return res;
	}
	public List<Game> getGamesGamersAvgAgeGreaterThan(double avgAge) {
		TypedQuery<Game> query = em.createQuery(
		        "select game from Game game where id in (" +
		        "select gameGamer.game.id from GameGamer gameGamer  " +
		        "group by gameGamer.game.id " +
		        "having avg(extract(year from current_date) - extract(year from gamer.birthdate)) > ?1)",
		        Game.class
		    );
		    return query.setParameter(1, avgAge).getResultList();
	}
	public List<GameMoves> getGamesWinnerMovesLessThan(int nMoves) {
		TypedQuery<GameMoves> query = em.createQuery(
				"select gameGamer.game.id as gameId, count(*) as moves from "
				+ "Move  where gameGamer.is_winner=true "
				+ "group by gameId having count(*) < ?1", 
				GameMoves.class);
		return query.setParameter(1, nMoves).getResultList();
	}
	public List<String> getGamersGameMovesLessThan(int nMoves) {
		TypedQuery<String> query = em.createQuery(
				"select distinct gameGamer.gamer.username as username from "
				+ "Move group by gameGamer.game.id, username"
				+ " having count(*) < ?1 ",
				String.class);
		return query.setParameter(1,  nMoves).getResultList();
	}
	public List<GameAvgMoves> getGamesAvgMoves() {
		TypedQuery<GameAvgMoves> query = em.createQuery(
				"select gameId, round(avg(moves),1) from "
				+ "(select gameGamer.game.id as gameId, count(*) as moves"
				+ " from Move  group by gameId, gameGamer.gamer.username) "
				+ "group by gameId",
				GameAvgMoves.class);
		return query.getResultList();
	}
	
}

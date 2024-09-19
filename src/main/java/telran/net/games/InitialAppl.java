package telran.net.games;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

import org.hibernate.jpa.HibernatePersistenceProvider;

import jakarta.persistence.*;
public class InitialAppl {

	private static final int HYPHENS_BEFORE_TITLE = 20;
	private static final int HYPHENS_AFTER_TITLE = 20;
	public static void main(String[] args) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("hibernate.hbm2ddl.auto", "update");//using existing table
		
		EntityManagerFactory emFactory = new HibernatePersistenceProvider()
				.createContainerEntityManagerFactory(new BullsCowsPersistenceUnitInfo(), map);
		EntityManager em = emFactory.createEntityManager();
		JpqlQueriesRepository repository = new JpqlQueriesRepository(em);
		displayTitle("1. display all data about games, average age\r\n"
				+ "of gamers in which greater than 60 (consider constraction\r\n"
				+ "where id in (select with group by)");
		List<Game> result1 = repository.getGamesGamersAvgAgeGreaterThan(60);
		displayResult(result1);
		displayTitle("2. display game_id and number of moves made by winner of games with number of moves made by\r\n"
				+ "winner less than 5");
		List<GameMoves> result2 = repository.getGamesWinnerMovesLessThan(5);
		displayResult(result2);
		displayTitle("3. display gamer names who made in one game number of moves less than 4");
		List<String> result3 = repository.getGamersGameMovesLessThan(4);
		displayResult(result3);
		displayTitle(" display game_id and average number of moves made by each gamer. Example in game 100000\r\n"
				+ "there are three gamers (gamer1, gamer2, gamer3)");
		List<GameAvgMoves> result4 = repository.getGamesAvgMoves();
		displayResult(result4);
	}

	private static <T >void displayResult(List<T> list) {
		list.forEach(System.out::println);
		System.out.println(list.size());
		
	}
	private static void displayTitle(String title) {
		System.out.printf("%s%s%s\n", "-".repeat(HYPHENS_BEFORE_TITLE),title,
				"-".repeat(HYPHENS_AFTER_TITLE));
	}

}

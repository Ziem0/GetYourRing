import com.nba.baller.getyourring.repositories.*;
import com.nba.baller.getyourring.services.GameService;
import com.nba.baller.getyourring.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;

class GameServiceTest {

	private GameService gameService;

	@Mock
	UserService userService = mock(UserService.class);

	@Mock
	TeamRepo teamRepo = mock(TeamRepo.class);

	@Mock
	PlayerRepo playerRepo= mock(PlayerRepo.class);

	@Mock
	CoachRepo coachRepo = mock(CoachRepo.class);

	@Mock
	MatchRepo matchRepo = mock(MatchRepo.class);

	@Mock
	RingRepo ringRepo = mock(RingRepo.class);

	@Mock
	CityRepo cityRepo = mock(CityRepo.class);

	@Mock
	HallRepo hallRepo = mock(HallRepo.class);


	@BeforeEach
	void setUp() {
		gameService = new GameService(teamRepo, playerRepo, coachRepo, matchRepo, ringRepo, cityRepo, hallRepo, userService);
	}
}
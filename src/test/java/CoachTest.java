import com.nba.baller.getyourring.helpers.Position;
import com.nba.baller.getyourring.models.game.Coach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

class CoachTest {

	@RepeatedTest(20)
	void testIfRangeIncludeAllPosition() {
		Position[] positions = Position.values();
		Coach coach = new Coach("coachName");
		coach.setRandomPosition();
		assertTrue(Arrays.asList(positions).contains(coach.getBoostedPosition()));
	}
}
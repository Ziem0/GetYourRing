import com.nba.baller.getyourring.models.game.Hall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

class HallTest {

	private Hall hall;

	@BeforeEach
	void setUp() {
		hall = new Hall("Garden");
	}

	@RepeatedTest(50)
	void testIfNumberBetween5and12() {
		List<Integer> range = IntStream.rangeClosed(5, 12).boxed().collect(Collectors.toList());
		assertTrue(range.contains(hall.getCrowdFactor()));
	}
}
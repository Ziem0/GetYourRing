import com.nba.baller.getyourring.models.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;


class OwnerTest {

	private Owner owner;

	@BeforeEach
	void setUp() {
		owner = new Owner("ziemo", "password", "ziemo@mail.com");
	}

	@Test
	void shouldThrowExceptionWhenEmailIsNull() {
		assertAll(
				() -> assertThrows(NullPointerException.class, () -> new Owner(null, "pass", "email@com")),
				() -> assertThrows(NullPointerException.class, () -> new Owner("ziemo", null, "email@com")),
				() -> assertThrows(NullPointerException.class, () -> new Owner("ziemo", "pass", null))
		);
	}




}
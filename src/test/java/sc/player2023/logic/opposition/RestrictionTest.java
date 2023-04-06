package sc.player2023.logic.opposition;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.player2023.Direction;
import sc.player2023.logic.board.BoardParser;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.rating.Rating;
import sc.player2023.logic.score.GameScore;

import static org.junit.jupiter.api.Assertions.*;

public class RestrictionTest {

    @Test
    void isInRestrictionTopRight_returnsTrue() {
        String boardString = """
              4 3 3 =   = = 3\s
               - = - = = - - =\s
              - = - = - - - -\s
               = =     = - = -\s
              - G - =     = =\s
               - - - - = - = -\s
              = - P = = G = -\s
               3 = =   = 3 3 4
              """;
        Restriction restriction = new Restriction(new Coordinates(10, 6), Direction.LEFT);
        Coordinates outOfBoundsCoords = new Coordinates(11, 5);
        assertTrue(restriction.isInRestriction(outOfBoundsCoords));
    }

    @Test
    void returnsTrue_whenSomewhereFarInRestriction() {
        String boardString = """
              4 3 3 =   = = 3\s
               - = - = = - - =\s
              - = - = - - - -\s
               = =     = - = -\s
              - G - =     = =\s
               - - - - = - = -\s
              = - P = = G = -\s
               3 = =   = 3 3 4
              """;
        Restriction restriction = new Restriction(new Coordinates(10, 6), Direction.LEFT);
        Coordinates outOfBoundsCoords = new Coordinates(15, 7);
        assertTrue(restriction.isInRestriction(outOfBoundsCoords));
    }

    @Test
    void returnsFalse_whenOutsideOfRestriction_nearOpponentPenguin() {
        String boardString = """
              4 3 3 =   = = 3\s
               - = - = = - - =\s
              - = - = - - - -\s
               = =     = - = -\s
              - G - =     = =\s
               - - - - = - = -\s
              = - P = = G = -\s
               3 = =   = 3 3 4
              """;
        Restriction restriction = new Restriction(new Coordinates(10, 6), Direction.LEFT);
        Coordinates outOfBoundsCoords = new Coordinates(9, 7);
        assertFalse(restriction.isInRestriction(outOfBoundsCoords));
    }

    @Test
    void returnsFalse_whenOutsideOfRestriction_nearOwnPenguin() {
        String boardString = """
              4 3 3 =   = = 3\s
               - = - = = - - =\s
              - = - = - - - -\s
               = =     = - = -\s
              - G - =     = =\s
               - - - - = - = -\s
              = - P = = G = -\s
               3 = =   = 3 3 4
              """;
        Restriction restriction = new Restriction(new Coordinates(10, 6), Direction.LEFT);
        Coordinates outOfBoundsCoords = new Coordinates(3, 6);
        assertFalse(restriction.isInRestriction(outOfBoundsCoords));
    }

}

package projet_graphe_L2;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {

    @Test
    public void exempleTestClassiqueJunit() {
        assertTrue(true);
    }

    //Exemple pour utiliser asserThat
    @Test
    public void exempleTestAsserThat() {
        int a = 2;
        int b = 3;
        assertThat(a + b).isEqualTo(5);
        assertThat(a + b).isNotEqualTo(6);
        assertThat(b).isGreaterThan(a);
    }

    //Voici une panoplie de test que l'on peut faire avec assertThat :
    /*
    
    ** basic assertions :
    
    assertThat(frodo.getName()).isEqualTo("Frodo");
    assertThat(frodo).isNotEqualTo(sauron);

    ** chaining string specific assertions :
    
    assertThat(frodo.getName()).startsWith("Fro")
                           .endsWith("do")
                           .isEqualToIgnoringCase("frodo");

    ** collection specific assertions (there are plenty more) :
    in the examples below fellowshipOfTheRing is a List<TolkienCharacter>
    
    assertThat(fellowshipOfTheRing).hasSize(9)
                               .contains(frodo, sam)
                               .doesNotContain(sauron);

    ** as() is used to describe the test and will be shown before the error message :
    
    assertThat(frodo.getAge()).as("check %s's age", frodo.getName()).isEqualTo(33);

    ** exception assertion, standard style ... :
    
    assertThatThrownBy(() -> { throw new Exception("boom!"); }).hasMessage("boom!");

    ** ... or BDD style :
    
    Throwable thrown = catchThrowable(() -> { throw new Exception("boom!"); });
    assertThat(thrown).hasMessageContaining("boom");

    ** using the 'extracting' feature to check fellowshipOfTheRing character's names :

    assertThat(fellowshipOfTheRing).extracting(TolkienCharacter::getName)
                                   .doesNotContain("Sauron", "Elrond");

    ** extracting multiple values at once grouped in tuples :

    assertThat(fellowshipOfTheRing).extracting("name", "age", "race.name")
                                   .contains(tuple("Boromir", 37, "Man"),
                                             tuple("Sam", 38, "Hobbit"),
                                             tuple("Legolas", 1000, "Elf"));

    ** filtering a collection before asserting :
    
    assertThat(fellowshipOfTheRing).filteredOn(character -> character.getName().contains("o"))
                                   .containsOnly(aragorn, frodo, legolas, boromir);

    ** combining filtering and extraction (yes we can) :
    
    assertThat(fellowshipOfTheRing).filteredOn(character -> character.getName().contains("o"))
                                   .containsOnly(aragorn, frodo, legolas, boromir)
                                   .extracting(character -> character.getRace().getName())
                                   .contains("Hobbit", "Elf", "Man");

    ** and many more assertions: iterable, stream, array, map, dates, path, file, numbers, predicate, optional ...
    */

}

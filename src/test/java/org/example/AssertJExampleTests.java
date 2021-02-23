package org.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Examples in the spirit of https://assertj.github.io/doc/#assertj-core.
 */
class AssertJExampleTests {

    @Test
    void assertionExamples() {

        String frodo = "Frodo";
        String sam = "Sam";
        String aragorn = "Aragorn";
        String sauron = "Sauron";

        var fellowship = Arrays.asList(frodo, sam, aragorn);

        assertThat(frodo)
                .startsWith("Fro")
                .endsWith("do");

        assertThat(fellowship)
                .contains(aragorn)
                .doesNotContain(sauron);

        assertThat(fellowship)
                .filteredOn(name -> name.startsWith("F"))
                .containsOnly(frodo);

        // note that there is also special exception assert support in JUnit5
        assertThatThrownBy(() -> { throw new Exception("boom!"); })
                .hasMessage("boom!");
    }

}

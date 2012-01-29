package pl.edu.amu.wmi.daut.base;

import junit.framework.TestCase;

/**
 * Test klasy NondeterministicAutomatonByThompsonApproach.
 */
public class TestNondeterministicAutomatonByThompsonApproach extends TestCase {

    /**
     * Pierwszy test (przykładowy prosty automat).
     */
    public final void testSimpleAutomaton() {
        final AutomatonSpecification spec = new NaiveAutomatonSpecification();

        State q0a = spec.addState();
        State q1a = spec.addState();
        State q2a = spec.addState();
        State q3a = spec.addState();
        State q4a = spec.addState();
        State q5a = spec.addState();

        spec.addTransition(q0a, q1a, new CharTransitionLabel('a'));
        spec.addTransition(q0a, q2a, new CharTransitionLabel('b'));
        spec.addTransition(q0a, q5a, new CharTransitionLabel('b'));
        spec.addTransition(q1a, q2a, new CharTransitionLabel('c'));
        spec.addTransition(q1a, q3a, new CharTransitionLabel('c'));
        spec.addTransition(q1a, q4a, new CharTransitionLabel('b'));
        spec.addTransition(q2a, q4a, new CharTransitionLabel('a'));
        spec.addTransition(q2a, q5a, new CharTransitionLabel('a'));
        spec.addTransition(q3a, q5a, new CharTransitionLabel('b'));
        spec.addTransition(q4a, q3a, new CharTransitionLabel('c'));
        spec.addTransition(q4a, q5a, new CharTransitionLabel('a'));
        spec.addTransition(q5a, q0a, new CharTransitionLabel('a'));
        spec.addLoop(q5a, new CharTransitionLabel('a'));

        spec.markAsInitial(q0a);
        spec.markAsFinal(q3a);
        spec.markAsFinal(q4a);
        spec.markAsFinal(q5a);

        final NondeterministicAutomatonByThompsonApproach automaton =
                new NondeterministicAutomatonByThompsonApproach(spec);

        assertTrue(automaton.accepts("abc"));
        assertTrue(automaton.accepts("ba"));
        assertTrue(automaton.accepts("ac"));
        assertTrue(automaton.accepts("baaaaaaaaa"));
        assertTrue(automaton.accepts("b"));
        assertFalse(automaton.accepts("cccccccccabbbbbbc"));
        assertFalse(automaton.accepts("aaaaaaaaaaa"));
        assertFalse(automaton.accepts("bcccccc"));
        assertFalse(automaton.accepts("z"));
    }

    /**
     * Drugi test (tylko epsilon-przejścia).
     */
    public final void testOnlyEpsilonTransitionLabel() {
        final AutomatonSpecification spec = new NaiveAutomatonSpecification();

        State q0a = spec.addState();
        State q1a = spec.addState();
        State q2a = spec.addState();
        State q3a = spec.addState();

        spec.addTransition(q0a, q1a, new EpsilonTransitionLabel());
        spec.addTransition(q0a, q2a, new EpsilonTransitionLabel());
        spec.addTransition(q1a, q3a, new EpsilonTransitionLabel());
        spec.addTransition(q2a, q3a, new EpsilonTransitionLabel());

        spec.markAsInitial(q0a);
        spec.markAsFinal(q3a);

        final NondeterministicAutomatonByThompsonApproach automaton =
                new NondeterministicAutomatonByThompsonApproach(spec);

        assertTrue(automaton.accepts(""));
        assertFalse(automaton.accepts("uam"));
    }

    /**
     * Trzeci test (pusty automat).
     */
    public final void testEmptyAutomaton() {
        final AutomatonSpecification spec = new NaiveAutomatonSpecification();

        final NondeterministicAutomatonByThompsonApproach automaton =
                new NondeterministicAutomatonByThompsonApproach(spec);

        assertFalse(automaton.accepts("cccccccccabbbbbbc"));
        assertFalse(automaton.accepts("aaaaaaaaaaa"));
        assertFalse(automaton.accepts("bcccccc"));
        assertFalse(automaton.accepts("z"));
    }

    /**
     * Czwarty test (Od stanu początkowego odchodzą 3 epsilon-przejścia do
     * trzech różnych stanów. Dopiero od owych trzech stanów występują
     * "normalne" przejścia - tj. po znaku).
     */
    public final void testOnlyEpsilonTransitionLabelFromInitialState() {
        final AutomatonSpecification spec = new NaiveAutomatonSpecification();

        State q0a = spec.addState();
        State q1a = spec.addState();
        State q2a = spec.addState();
        State q3a = spec.addState();
        State q4a = spec.addState();
        State q5a = spec.addState();
        State q6a = spec.addState();
        State q7a = spec.addState();

        spec.addTransition(q0a, q1a, new EpsilonTransitionLabel());
        spec.addTransition(q0a, q2a, new EpsilonTransitionLabel());
        spec.addTransition(q0a, q3a, new EpsilonTransitionLabel());
        spec.addTransition(q1a, q4a, new CharTransitionLabel('a'));
        spec.addTransition(q1a, q5a, new CharTransitionLabel('a'));
        spec.addTransition(q2a, q5a, new CharTransitionLabel('b'));
        spec.addTransition(q3a, q5a, new CharTransitionLabel('c'));
        spec.addTransition(q3a, q6a, new CharTransitionLabel('a'));
        spec.addTransition(q3a, q6a, new CharTransitionLabel('b'));
        spec.addTransition(q4a, q5a, new CharTransitionLabel('b'));
        spec.addTransition(q4a, q7a, new CharTransitionLabel('b'));
        spec.addTransition(q5a, q7a, new CharTransitionLabel('a'));
        spec.addTransition(q5a, q6a, new CharTransitionLabel('a'));
        spec.addTransition(q6a, q7a, new CharTransitionLabel('c'));
        spec.addTransition(q6a, q5a, new CharTransitionLabel('b'));
        spec.addLoop(q3a, new CharTransitionLabel('c'));
        spec.addLoop(q6a, new CharTransitionLabel('c'));
        spec.addLoop(q7a, new CharTransitionLabel('a'));
        spec.addLoop(q7a, new CharTransitionLabel('b'));
        spec.addLoop(q7a, new CharTransitionLabel('c'));

        spec.markAsInitial(q0a);
        spec.markAsFinal(q7a);

        final NondeterministicAutomatonByThompsonApproach automaton =
                new NondeterministicAutomatonByThompsonApproach(spec);

        assertTrue(automaton.accepts("abc"));
        assertTrue(automaton.accepts("ac"));
        assertTrue(automaton.accepts("baaaaaaa"));
        assertTrue(automaton.accepts("ccccccca"));
        assertFalse(automaton.accepts("ccc"));
        assertFalse(automaton.accepts(""));
        assertFalse(automaton.accepts("uam"));
    }

    /**
     * Piąty test (Do stanu końcowego prowadzą tylko epsilon przejścia.
     * Występują pętle epsilon).
     */
    public final void testOnlyEpsilonTransitionLabelToFinalState() {
        final AutomatonSpecification spec = new NaiveAutomatonSpecification();

        State q0a = spec.addState();
        State q1a = spec.addState();
        State q2a = spec.addState();
        State q3a = spec.addState();
        State q4a = spec.addState();

        spec.addTransition(q1a, q4a, new EpsilonTransitionLabel());
        spec.addTransition(q2a, q4a, new EpsilonTransitionLabel());
        spec.addTransition(q3a, q4a, new EpsilonTransitionLabel());
        spec.addTransition(q0a, q1a, new CharTransitionLabel('a'));
        spec.addTransition(q0a, q2a, new CharTransitionLabel('b'));
        spec.addTransition(q0a, q3a, new CharTransitionLabel('b'));
        spec.addTransition(q1a, q2a, new CharTransitionLabel('a'));
        spec.addTransition(q2a, q0a, new CharTransitionLabel('b'));
        spec.addTransition(q3a, q2a, new CharTransitionLabel('c'));
        spec.addLoop(q1a, new CharTransitionLabel('c'));
        spec.addLoop(q3a, new CharTransitionLabel('a'));
        spec.addLoop(q2a, new CharTransitionLabel('b'));
        spec.addLoop(q1a, new EpsilonTransitionLabel());
        spec.addLoop(q3a, new EpsilonTransitionLabel());

        spec.markAsInitial(q0a);
        spec.markAsFinal(q4a);

        final NondeterministicAutomatonByThompsonApproach automaton =
                new NondeterministicAutomatonByThompsonApproach(spec);

        assertTrue(automaton.accepts("b"));
        assertFalse(automaton.accepts("uam"));
    }
    /**
     * Szósty test (tylko jeden stan, brak przejść).
     */
    public final void testOneState() {
        final AutomatonSpecification spec = new NaiveAutomatonSpecification();

        State q0a = spec.addState();

        final NondeterministicAutomatonByThompsonApproach automaton =
                new NondeterministicAutomatonByThompsonApproach(spec);

        spec.markAsInitial(q0a);
        spec.markAsFinal(q0a);

        assertTrue(automaton.accepts(""));
        assertFalse(automaton.accepts("cccccccccabbbbbbc"));
        assertFalse(automaton.accepts("aaaaaaaaaaa"));
        assertFalse(automaton.accepts("bcccccc"));
        assertFalse(automaton.accepts("z"));
    }

    /**
     * Siódmy test (dwa stany, epsilon-przejście pomiędzy nimi).
     */

    public final void testTwoStatesOneEpsilonTrasitionLabel() {
        final AutomatonSpecification spec = new NaiveAutomatonSpecification();

        State q0a = spec.addState();
        State q1a = spec.addState();

        spec.addTransition(q0a, q1a, new EpsilonTransitionLabel());

        final NondeterministicAutomatonByThompsonApproach automaton =
                new NondeterministicAutomatonByThompsonApproach(spec);

        spec.markAsInitial(q0a);
        spec.markAsFinal(q1a);

        assertTrue(automaton.accepts(""));
        assertFalse(automaton.accepts("cccccccccabbbbbbc"));
        assertFalse(automaton.accepts("aaaaaaaaaaa"));
        assertFalse(automaton.accepts("bcccccc"));
        assertFalse(automaton.accepts("z"));
    }

	/**Ósmy test
		*Automat: automat (deterministyczny bądź niedeterministyczny, 
		*z epsilon-przejściami lub bez nich) akceptujący liczby szesnastkowe 
		*(cyfry zapisane małymi bądź wielkimi literami, 
		*na początku mogą być zera nieznaczące, np. 00FA powinno być akceptowane).
		*Co przetestować: metodę accepts klasy NondeterministicAutomatonByThompsonApproach.
		*Podpowiedź: można użyć klasy CharClassTransitionLabel.
	*/
 
 
	public final void testNondeterministicAutomatonWithEpsilonTransition()
	{
		final AutomatonSpecification spec = new NaiveAutomatonSpecification();
		
		State q0 = spec.addState();
		State q1 = spec.addState();
		State q2 = spec.addState();
		State q3 = spec.addState();
		State q4 = spec.addState();
		State q5 = spec.addState();
		
		TransitionLabel hexLabel = new CharClassTransitionLabel('0-9,A-F,a-f');
		
		spec.addTransition(q0, q1, hexLabel);
		spec.addTransition(q1, q2, hexLabel);
		spec.addTransition(q2, q3, hexLabel);
		spec.addTransition(q3, q4, hexLabel);
		spec.addTransition(q3, q5, hexLabel);
		spec.addTransition(q4, q0, new EpsilonTransitionLabel());
		spec.addTransition(q4, q1, hexLabel);
		
		spec.markAsInitial(q0);
		spec.markAsFinal(q5);
		
		final NondeterministicAutomatonByThompsonApproach automat0F = 
			new NondeterministicAutomatonByThompsonApproach(spec);

		assertTrue(automat0F.accepts("05AF16B027C138D249E3"));
		assertTrue(automat0F.accepts("05AF16B027C138D2"));
		assertTrue(automat0F.accepts("05AF16B027C1"));
		assertTrue(automat0F.accepts("05AF16B0"));
		assertTrue(automat0F.accepts("05AF"));

		assertFalse(automat0F.accepts(""));
		assertFalse(automat0F.accepts("T"));
		assertFalse(automat0F.accepts("\n"));
		assertFalse(automat0F.accepts("00F"));
		assertFalse(automat0F.accepts("2,10"));
		assertFalse(automat0F.accepts("true"));
		assertFalse(automat0F.accepts("LN61R"));
	}
}

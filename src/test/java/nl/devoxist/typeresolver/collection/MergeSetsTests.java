/*
 * Copyright (c) 2023 Devoxist, Dev-Bjorn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package nl.devoxist.typeresolver.collection;

import nl.devoxist.typeresolver.register.Register;
import nl.devoxist.typeresolver.register.RegisterPriority;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class MergeSetsTests {

    @Test
    public void checkIfMergedSetsMatch() {
        Set<Integer> integerSet = new HashSet<>();
        integerSet.add(4);
        integerSet.add(2);
        Set<Integer> integerSet1 = new HashSet<>();
        integerSet1.add(3);
        integerSet1.add(1);
        Collection<Set<Integer>> sets = new HashSet<>();
        sets.add(integerSet);
        sets.add(integerSet1);

        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);

        Set<Integer> mergedSets = MergeSets.mergeSets(sets);
        Assertions.assertTrue(mergedSets.contains(1));
        Assertions.assertTrue(mergedSets.contains(2));
        Assertions.assertTrue(mergedSets.contains(3));
        Assertions.assertTrue(mergedSets.contains(4));
        Assertions.assertEquals(set, mergedSets);
    }

    @Test
    public void checkIfMergedSetsMatch1() {
        Set<Integer> integerSet = new HashSet<>();
        integerSet.add(1);
        integerSet.add(2);
        Set<Integer> integerSet1 = new HashSet<>();
        integerSet1.add(3);
        integerSet1.add(4);
        Collection<Set<Integer>> sets = new HashSet<>();
        sets.add(integerSet);
        sets.add(integerSet1);

        Set<Integer> set = new TreeSet<>(Comparator.reverseOrder());
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);

        Set<Integer> mergedSets = MergeSets.mergeSets(sets, () -> new TreeSet<>(Comparator.reverseOrder()));
        Assertions.assertTrue(mergedSets.contains(1));
        Assertions.assertTrue(mergedSets.contains(2));
        Assertions.assertTrue(mergedSets.contains(3));
        Assertions.assertTrue(mergedSets.contains(4));
        Assertions.assertEquals(set, mergedSets);
    }

    @Test
    public void checkIfMergedSetsMatch2() {
        Set<Integer> integerSet = new HashSet<>();
        integerSet.add(1);
        integerSet.add(2);
        Set<Integer> integerSet1 = new HashSet<>();
        integerSet1.add(3);
        integerSet1.add(4);
        Collection<Set<Integer>> sets = new HashSet<>();
        sets.add(integerSet);
        sets.add(integerSet1);

        Set<Integer> set = new TreeSet<>(Comparator.reverseOrder());
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);

        Set<Integer> mergedSets = MergeSets.mergeSets(sets, Comparator.reverseOrder());
        Assertions.assertTrue(mergedSets.contains(1));
        Assertions.assertTrue(mergedSets.contains(2));
        Assertions.assertTrue(mergedSets.contains(3));
        Assertions.assertTrue(mergedSets.contains(4));
        Assertions.assertEquals(set, mergedSets);
    }

    @Test
    public void checkIfMergedSetsMatch3() {
        Set<Register> registers = new HashSet<>();
        Register register1 = new Register(RegisterPriority.HIGHEST);
        Register register2 = new Register(RegisterPriority.HIGH);
        Register register3 = new Register(RegisterPriority.LOW);
        Register register4 = new Register(RegisterPriority.LOWEST, register3, register2, register1);
        registers.add(register4);

        Set<Register> checkableSet = new TreeSet<>(Comparator.comparing(Register::getPriority).reversed());
        checkableSet.add(register1);
        checkableSet.add(register2);
        checkableSet.add(register3);
        checkableSet.add(register4);

        Set<Register> mergedSets = MergeSets.mergeSets(
                registers,
                Register::getRegistries,
                Comparator.comparing(Register::getPriority).reversed()
        );
        Assertions.assertTrue(mergedSets.contains(register1));
        Assertions.assertTrue(mergedSets.contains(register2));
        Assertions.assertTrue(mergedSets.contains(register3));
        Assertions.assertTrue(mergedSets.contains(register4));
        Assertions.assertEquals(checkableSet, mergedSets);
    }

    @Test
    public void checkIfMergedSetsMatch4() {
        Set<Register> registers = new HashSet<>();
        Register register1 = new Register(RegisterPriority.HIGHEST);
        Register register2 = new Register(RegisterPriority.HIGH);
        Register register3 = new Register(RegisterPriority.LOW);
        Register register4 = new Register(RegisterPriority.LOWEST, register3, register2, register1);
        registers.add(register4);

        Set<Register> checkableSet = new TreeSet<>(Comparator.comparing(Register::getPriority).reversed());
        checkableSet.add(register1);
        checkableSet.add(register2);
        checkableSet.add(register3);
        checkableSet.add(register4);

        Set<Register> mergedSets = MergeSets.mergeSets(
                registers,
                Register::getRegistries,
                () -> new TreeSet<>(Comparator.comparing(Register::getPriority)
                                            .reversed())
        );
        Assertions.assertTrue(mergedSets.contains(register1));
        Assertions.assertTrue(mergedSets.contains(register2));
        Assertions.assertTrue(mergedSets.contains(register3));
        Assertions.assertTrue(mergedSets.contains(register4));
        Assertions.assertEquals(checkableSet, mergedSets);
    }

    @Test
    public void checkIfMergedSetsMatch5() {
        Set<Register> registers = new HashSet<>();
        Register register1 = new Register(RegisterPriority.HIGHEST);
        Register register2 = new Register(RegisterPriority.HIGH);
        Register register3 = new Register(RegisterPriority.LOW);
        Register register4 = new Register(RegisterPriority.LOWEST, register3, register2, register1);
        registers.add(register4);

        Set<Register> checkableSet = new HashSet<>();
        checkableSet.add(register1);
        checkableSet.add(register2);
        checkableSet.add(register3);
        checkableSet.add(register4);

        Set<Register> mergedSets = MergeSets.mergeSets(registers, Register::getRegistries);
        Assertions.assertTrue(mergedSets.contains(register1));
        Assertions.assertTrue(mergedSets.contains(register2));
        Assertions.assertTrue(mergedSets.contains(register3));
        Assertions.assertTrue(mergedSets.contains(register4));
        Assertions.assertEquals(checkableSet, mergedSets);
    }
}

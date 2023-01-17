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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Merge multiple {@link Set} into each other.
 *
 * @param <T> The type of the {@link Set}.
 *
 * @author Dev-Bjorn
 * @version 1.3.0
 * @since 1.3.0
 */
public final class MergeSets<T> extends Thread {
    /**
     * The set that need to be merged. If this field is applied {@link #set} won't be applied.
     *
     * @since 1.3.0
     */
    private Collection<Set<T>> sets;
    /**
     * The set to get the function providers ({@link #setGetter}) from. This is only applied when the function is also
     * applied. If this field is applied {@link #sets} won't be applied.
     *
     * @since 1.3.0
     */
    private Set<T> set;
    /**
     * Function provides the getter for the {@link Set}s, this only works when the {@link #set} is applied too.
     *
     * @since 1.3.0
     */
    private Function<T, Set<T>> setGetter;
    /**
     * The constructed or given set where the {@link Set}s are too.
     *
     * @since 1.3.0
     */
    private final Set<T> mergedSet;
    /**
     * The future callback of the merged {@link Set}s.
     *
     * @since 1.3.0
     */
    private final CompletableFuture<Set<T>> providerCompletableFuture = new CompletableFuture<>();

    /**
     * Constructs the {@link MergeSets} with the callback as a {@link HashSet}.
     *
     * @param setsToMerge The setsToMerge that need to be merged.
     *
     * @since 1.3.0
     */
    private MergeSets(Collection<Set<T>> setsToMerge) {
        this.sets = setsToMerge;
        this.mergedSet = new HashSet<>();
    }


    /**
     * Constructs the {@link MergeSets} with the callback as a {@link TreeSet} and sorted on given the
     * {@link Comparator}.
     *
     * @param setsToMerge The setsToMerge that need to be merged.
     * @param comparator  The {@link Set} {@link Comparator} on which the {@link Set} gets sorted.
     *
     * @since 1.3.0
     */
    private MergeSets(Collection<Set<T>> setsToMerge, Comparator<T> comparator) {
        this.sets = setsToMerge;
        this.mergedSet = new TreeSet<>(comparator);
    }

    /**
     * Constructs the {@link MergeSets} with the callback as a given provider {@link Set}.
     *
     * @param setsToMerge The setsToMerge that need to be merged.
     * @param setProvider The provider for the merged {@link Set}.
     *
     * @since 1.3.0
     */
    private MergeSets(Collection<Set<T>> setsToMerge, @NotNull Supplier<Set<T>> setProvider) {
        this.sets = setsToMerge;
        this.mergedSet = setProvider.get();
    }

    /**
     * Constructs the {@link MergeSets} with the callback as a {@link HashSet}. The merged {@link Set} will be formed on
     * the callback of the {@link Function}.
     *
     * @param setToMerge The setToMerge that need to be merged.
     * @param setGetter  The method in the {@link Set}toMerge to retrieve the {@link Set}s to merge.
     *
     * @since 1.3.0
     */
    private MergeSets(Set<T> setToMerge, Function<T, Set<T>> setGetter) {
        this.set = setToMerge;
        this.setGetter = setGetter;
        this.mergedSet = new HashSet<>();
    }

    /**
     * Constructs the {@link MergeSets} with the callback as a {@link TreeSet} and sorted on given the
     * {@link Comparator}. The merged {@link Set} will be formed on the callback of the {@link Function}.
     *
     * @param setToMerge The sets that contains objects that have the given {@link Function}, and the return of that
     *                   function will be merged.
     * @param setGetter  The method in the {@link Set}toMerge to retrieve the {@link Set}s to merge.
     * @param comparator The {@link Set} {@link Comparator} on which the {@link Set} gets sorted.
     *
     * @since 1.3.0
     */
    private MergeSets(Set<T> setToMerge, Function<T, Set<T>> setGetter, Comparator<T> comparator) {
        this.set = setToMerge;
        this.setGetter = setGetter;
        this.mergedSet = new TreeSet<>(comparator);
    }

    /**
     * Constructs the {@link MergeSets} with the callback as a given provider {@link Set}. The merged {@link Set} will
     * be formed on the callback of the {@link Function}.
     *
     * @param setToMerge  The sets that contains objects that have the given {@link Function}, and the return of that
     *                    function will be merged.
     * @param setGetter   The method in the {@link Set}toMerge to retrieve the {@link Set}s to merge.
     * @param setProvider The provider for the merged {@link Set}
     *
     * @since 1.3.0
     */
    private MergeSets(Set<T> setToMerge, Function<T, Set<T>> setGetter, @NotNull Supplier<Set<T>> setProvider) {
        this.set = setToMerge;
        this.setGetter = setGetter;
        this.mergedSet = setProvider.get();
    }

    /**
     * Constructs the {@link MergeSets} and merges multiple {@link Set}s into one {@link Set}. The callback is
     * a {@link HashSet}. If it needs to be a sorted {@link Set} use this method with a {@link Comparator} as argument
     * or use this method with a setProvider to provide the {@link Set}. This provided set will be the merged
     * {@link Set}.
     *
     * @param setsToMerge The sets that need to be merged.
     * @param <T>         The type of the set that is going to be merged.
     *
     * @return The merged set that contains all the elements of given {@link Set}s.
     *
     * @since 1.3.0
     */
    public static <T> Set<T> mergeSets(Collection<Set<T>> setsToMerge) {
        MergeSets<T> setsMerger = new MergeSets<>(setsToMerge);
        setsMerger.start();
        return setsMerger.getSet();
    }


    /**
     * Constructs the {@link MergeSets} and merges multiple {@link Set}s into one {@link Set}. The callback is
     * a {@link TreeSet}. If it needs to be {@link Set} use this method without the {@link Comparator} as argument
     * or use this method with a setProvider to provide the {@link Set}. This provided set will be the merged
     * {@link Set}.
     *
     * @param setsToMerge The sets that need to be merged.
     * @param comparator  The {@link Set} {@link Comparator} on which the {@link Set} gets sorted.
     * @param <T>         The type of the set that is going to be merged.
     *
     * @return The merged set that contains all the elements of given {@link Set}s.
     *
     * @since 1.3.0
     */
    public static <T> Set<T> mergeSets(Collection<Set<T>> setsToMerge, Comparator<T> comparator) {
        MergeSets<T> setsMerger = new MergeSets<>(setsToMerge, comparator);
        setsMerger.start();
        return setsMerger.getSet();
    }

    /**
     * Constructs the {@link MergeSets} and merges multiple {@link Set}s into one {@link Set}. The callback is
     * a provided set type.
     *
     * @param setsToMerge The sets that need to be merged.
     * @param setProvider The provider for the merged {@link Set}.
     * @param <T>         The type of the set that is going to be merged.
     *
     * @return The merged set that contains all the elements of given {@link Set}s.
     *
     * @since 1.3.0
     */
    public static <T> Set<T> mergeSets(Collection<Set<T>> setsToMerge, Supplier<Set<T>> setProvider) {
        MergeSets<T> setsMerger = new MergeSets<>(setsToMerge, setProvider);
        setsMerger.start();
        return setsMerger.getSet();
    }

    /**
     * Constructs the {@link MergeSets} and merges multiple {@link Set}s into one {@link Set}. The merged set wil be
     * formed by the callback of the {@link Function}. The callback is merged sets of the return of the
     * {@link Function} as a {@link HashSet}. If it needs to be a sorted {@link Set} use this method with a
     * {@link Comparator} as argument or use this method with a setProvider to provide the {@link Set}. This provided
     * set will be the merged {@link Set}.
     *
     * @param setToMerge The sets that contains objects that have the given {@link Function}, and the return of that
     *                   function will be merged.
     * @param setGetter  The method in the {@link Set}toMerge to retrieve the {@link Set}s to merge.
     * @param <T>        The type of the set that is going to be merged.
     *
     * @return The merged set that contains all the elements of given {@link Set}s.
     *
     * @since 1.3.0
     */
    public static <T> Set<T> mergeSets(Set<T> setToMerge, Function<T, Set<T>> setGetter) {
        MergeSets<T> setsMerger = new MergeSets<>(setToMerge, setGetter);
        setsMerger.start();
        return setsMerger.getSet();
    }

    /**
     * Constructs the {@link MergeSets} and merges multiple {@link Set}s into one {@link Set}. The merged set wil be
     * formed by the callback of the {@link Function}. The callback is merged sets of the return of the
     * {@link Function} as a {@link TreeSet}. If it needs to be {@link Set} use this method without the
     * {@link Comparator} as argument or use this method with a setProvider to provide the {@link Set}. This provided
     * set will be the merged {@link Set}.
     *
     * @param setToMerge The sets that contains objects that have the given {@link Function}, and the return of that
     *                   function will be merged.
     * @param setGetter  The method in the {@link Set}toMerge to retrieve the {@link Set}s to merge.
     * @param comparator The {@link Set} {@link Comparator} on which the {@link Set} gets sorted.
     * @param <T>        The type of the set that is going to be merged.
     *
     * @return The merged set that contains all the elements of given {@link Set}s.
     *
     * @since 1.3.0
     */
    public static <T> Set<T> mergeSets(Set<T> setToMerge, Function<T, Set<T>> setGetter, Comparator<T> comparator) {
        MergeSets<T> setsMerger = new MergeSets<>(setToMerge, setGetter, comparator);
        setsMerger.start();
        return setsMerger.getSet();
    }

    /**
     * Constructs the {@link MergeSets} and merges multiple {@link Set}s into one {@link Set}. The merged set wil be
     * formed by the callback of the {@link Function}. The callback is merged sets of the return of the
     * {@link Function} as a provided set type.
     *
     * @param setToMerge  The sets that contains objects that have the given {@link Function}, and the return of that
     *                    function will be merged.
     * @param setGetter   The method in the {@link Set}toMerge to retrieve the {@link Set}s to merge.
     * @param setProvider The provider for the merged {@link Set}
     * @param <T>         The type of the set that is going to be merged.
     *
     * @return The merged set that contains all the elements of given {@link Set}s.
     *
     * @since 1.3.0
     */
    public static <T> Set<T> mergeSets(Set<T> setToMerge, Function<T, Set<T>> setGetter, Supplier<Set<T>> setProvider) {
        MergeSets<T> setsMerger = new MergeSets<>(setToMerge, setGetter, setProvider);
        setsMerger.start();
        return setsMerger.getSet();
    }


    /**
     * If this thread was constructed using a separate
     * {@code Runnable} run object, then that
     * {@code Runnable} object's {@code run} method is called;
     * otherwise, this method does nothing and returns.
     * <p>
     * Subclasses of {@code Thread} should override this method.
     *
     * @see #start()
     * @since 1.3.0
     */
    @Override
    public void run() {
        if (sets != null) {
            updateWhenSets();
        } else if (this.set != null && this.setGetter != null) {
            updateWhenSet();
        }

        this.providerCompletableFuture.complete(mergedSet);
    }

    /**
     * Update the {@link #mergedSet} when the {@link #sets} is provided.
     *
     * @since 1.3.0
     */
    private void updateWhenSets() {
        for (Set<T> t : sets) {
            this.mergedSet.addAll(t);
        }
    }

    /**
     * Update the {@link #mergedSet} when the {@link #set} and {@link #setGetter} is provided.
     *
     * @since 1.3.0
     */
    private void updateWhenSet() {
        for (T t : this.set) {
            Set<T> tSet = this.setGetter.apply(t);
            this.mergedSet.addAll(tSet);
        }
    }

    /**
     * The callback of the search. This can take a while, because it comes from the {@link #providerCompletableFuture}.
     *
     * @return The callback of the search.
     *
     * @since 1.3.0
     */
    public @Nullable Set<T> getSet() {
        try {
            return providerCompletableFuture.get();
        } catch (InterruptedException | ExecutionException ignored) {
            return null;
        }
    }
}

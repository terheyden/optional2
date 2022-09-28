package com.terheyden.optional2;

import javax.annotation.Nullable;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import io.vavr.CheckedConsumer;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedFunction1;
import io.vavr.CheckedFunction2;
import io.vavr.CheckedRunnable;
import io.vavr.Tuple;
import io.vavr.Tuple2;

/**
 * Handles two optional values, providing methods to verify them in various ways.
 */
public class Optional2<C, D> {

    // These generics aren't strongly typed, for later casting purposes.
    private static final Optional2 EMPTY = new Optional2(Optional.empty(), Optional.empty());

    private final Optional<C> obj1;
    private final Optional<D> obj2;

    /* package */ Optional2(@Nullable C obj1, @Nullable D obj2) {
        this.obj1 = Optional.ofNullable(obj1);
        this.obj2 = Optional.ofNullable(obj2);
    }

    /* package */ Optional2(Optional<C> obj1, Optional<D> obj2) {
        this.obj1 = obj1;
        this.obj2 = obj2;
    }

    public static <C> Optional1<C> of(C obj1) {
        return new Optional1<>(obj1);
    }

    public static <C, D> Optional2<C, D> of(C obj1, D obj2) {
        return new Optional2<>(obj1, obj2);
    }

    public static <C> Optional1<C> ofNullable(@Nullable C obj1) {
        return new Optional1<>(obj1);
    }

    public static <C, D> Optional2<C, D> ofNullable(@Nullable C obj1, @Nullable D obj2) {
        return new Optional2<>(obj1, obj2);
    }

    /**
     * Returns an empty immutable singleton {@code Optional2} instance.
     */
    @SuppressWarnings("unchecked")
    public static <C, D> Optional2<C, D> empty() {
        return (Optional2<C, D>) EMPTY;
    }

    /**
     * A chainable version of {@link #orElseThrow(CheckedFunction0)}.
     * Throws the given exception if either value is null.
     *
     * @return this, for chaining
     * @throws NoSuchElementException if either value is null
     */
    public Optional2<C, D> throwIfEmpty(CheckedFunction0<? extends Throwable> exceptionSupplier) {

        if (isEmpty()) {
            Optional2Utils.throwUnchecked(exceptionSupplier.unchecked().get());
        }

        return this;
    }

    /**
     * A chainable version of {@link #orElseThrow()}.
     * Throws if either value is null.
     *
     * @return this, for chaining
     * @throws NoSuchElementException if either value is null
     */
    public Optional2<C, D> throwIfEmpty() {
        return isPresent()
            ? this
            : throwIfEmpty(() -> new NoSuchElementException("One or more Optional2 values are empty: " + this));
    }

    /**
     * Returns a {@link Tuple2} containing both values, if present.
     * If either value is null, throws a {@link NoSuchElementException}.
     */
    public Tuple2<C, D> get() {
        return orElseThrow();
    }

    /**
     * Returns an {@link Optional} containing a {@link Tuple2} of the two values,
     * if present. If either value is null, {@link Optional#empty()} is returned.
     */
    public Optional<Tuple2<C, D>> getOptional() {
        return isEmpty()
            ? Optional.empty()
            : Optional.of(Tuple.of(obj1.get(), obj2.get()));
    }

    /**
     * Just like {@link Optional#get()}, returns the first value, if present,
     * or throws a {@link NoSuchElementException} if it's null.
     */
    public C getFirstValue() {
        return obj1.get();
    }

    /**
     * Just like {@link Optional#get()}, returns the second value, if present,
     * or throws a {@link NoSuchElementException} if it's null.
     */
    public D getSecondValue() {
        return obj2.get();
    }

    /**
     * If the second value is null, use the supplier to get a non-empty value to use instead.
     *
     * @see Optional#or(Supplier)
     */
    public Optional2<C, D> or(CheckedFunction0<? extends Optional1<D>> supplyIfEmpty) {

        if (obj1.isEmpty()) {
            return empty();
        }

        return obj2.isEmpty()
            ? new Optional2<>(obj1, supplyIfEmpty.unchecked().apply().getOptional())
            : this;
    }

    /**
     * If the second value is null, use the given replacement value instead.
     *
     * @see Optional#or(Supplier)
     */
    public Optional2<C, D> or(Optional1<D> useIfEmpty) {

        if (obj1.isEmpty()) {
            return empty();
        }

        return isEmpty()
            ? new Optional2<>(obj1, useIfEmpty.getOptional())
            : this;
    }

    /**
     * If the second value is null, use the given replacement value instead.
     *
     * @see Optional#or(Supplier)
     */
    public Optional2<C, D> or(Optional<D> useIfEmpty) {

        if (obj1.isEmpty()) {
            return empty();
        }

        return isEmpty()
            ? new Optional2<>(obj1, useIfEmpty)
            : this;
    }

    /**
     * If the second value is null, use the given replacement value instead.
     *
     * @see Optional#or(Supplier)
     */
    public Optional2<C, D> or(D useIfEmpty) {

        if (obj1.isEmpty()) {
            return empty();
        }

        return isEmpty()
            ? new Optional2<>(obj1, Optional.of(useIfEmpty))
            : this;
    }

    /**
     * Returns a {@link Tuple2} containing the first and second values, if present.
     * If the first value is null, the first alternate value is used instead.
     * Similarly, if the second value is null, the second alternate value is used.
     */
    public Tuple2<C, D> orElse(C elseValue1, D elseValue2) {
        return Tuple.of(obj1.orElse(elseValue1), obj2.orElse(elseValue2));
    }

    /**
     * Returns the value if present, else null.
     * Equivalent to: {@code orElse(null, null)}, but with proper {@code @Nullable} annotations.
     */
    @Nullable
    public Tuple2<C, D> orElseNull() {
        return isEmpty()
            ? null
            : get();
    }

    /**
     * Returns a {@link Tuple2} containing the first and second values.
     * If the first value is null, the first supplier is used instead.
     * Similarly, if the second value is null, the second supplier is used.
     */
    public Tuple2<C, D> orElseGet(
        CheckedFunction0<? extends C> elseSupplier1,
        CheckedFunction0<? extends D> elseSupplier2) {

        return Tuple.of(
            obj1.orElseGet(elseSupplier1.unchecked()),
            obj2.orElseGet(elseSupplier2.unchecked()));
    }

    /**
     * If both values are present, returns a {@link Tuple2} that contains them.
     * Otherwise, throws the provided exception.
     */
    public Tuple2<C, D> orElseThrow(CheckedFunction0<? extends Throwable> exceptionSupplier) {
        throwIfEmpty(exceptionSupplier);
        return get();
    }

    /**
     * If both values are present, returns a {@link Tuple2} that contains them.
     * Otherwise, throws a {@link NoSuchElementException}.
     *
     * @throws NoSuchElementException if either value is null
     */
    public Tuple2<C, D> orElseThrow() {
        throwIfEmpty();
        return get();
    }

    /**
     * True if both values are present.
     *
     * @see #isEmpty()
     */
    public boolean isPresent() {
        return obj1.isPresent() && obj2.isPresent();
    }

    /**
     * True if either value is not present.
     *
     * @see #isPresent()
     */
    public boolean isEmpty() {
        return !isPresent();
    }

    /**
     * If both values are present, apply the first consumer to the first value,
     * and the second consumer to the second value.
     * Chainable version of {@link Optional#ifPresent(Consumer)}.
     *
     * @return this, for chaining
     * @see Optional#ifPresent(Consumer)
     */
    public Optional2<C, D> ifPresent(CheckedConsumer<? super C> consumer1, CheckedConsumer<? super D> consumer2) {

        if (isEmpty()) {
            return this;
        }

        obj1.ifPresent(consumer1.unchecked());
        obj2.ifPresent(consumer2.unchecked());
        return this;
    }

    /**
     * If both values are present, apply the consumer to both values.
     * Chainable version of {@link Optional#ifPresent(Consumer)}.
     *
     * @return this, for chaining
     * @see Optional#ifPresent(Consumer)
     */
    public Optional2<C, D> ifPresent(CheckedConsumer2<? super C, ? super D> consumer) {

        if (isEmpty()) {
            return this;
        }

        consumer.unchecked().accept(obj1.get(), obj2.get());
        return this;
    }

    /**
     * If both values are present, apply the given {@link CheckedConsumer2} to both values.
     * Otherwise, run the given {@link CheckedRunnable}.
     * Chainable version of {@link Optional#ifPresentOrElse(Consumer, Runnable)}.
     *
     * @return this, for chaining
     */
    public Optional2<C, D> ifPresentOrElse(
        CheckedConsumer2<? super C, ? super D> presentConsumer,
        CheckedRunnable emptyAction) {

        if (isPresent()) {
            return ifPresent(presentConsumer);
        }

        emptyAction.unchecked().run();
        return this;
    }

    /**
     * Run the given {@link CheckedRunnable} if either value is null.
     *
     * @return this, for chaining
     * @see Optional#ifPresent(Consumer)
     */
    public Optional2<C, D> ifEmpty(CheckedRunnable runIfEmpty) {

        if (isEmpty()) {
            runIfEmpty.unchecked().run();
        }

        return this;
    }

    /**
     * If both values are present, apply the given filter.
     *
     * @return this, for chaining
     */
    public Optional2<C, D> filter(CheckedPredicate2<? super C, ? super D> predicate) {

        if (isEmpty()) {
            return this;
        }

        if (predicate.unchecked().test(obj1.get(), obj2.get())) {
            return this;
        }

        return empty();
    }

    /**
     * If both values are present, apply the first mapper to the first value,
     * and the second mapper to the second value.
     *
     * @see #reduce(CheckedFunction2)
     * @see #map(CheckedFunction2, CheckedFunction2)
     * @see Optional#map(Function)
     */
    public <A, E> Optional2<A, E> map(
        CheckedFunction1<? super C, ? extends A> mapper1,
        CheckedFunction1<? super D, ? extends E> mapper2) {

        return isEmpty()
            ? empty()
            : new Optional2<>(obj1.map(mapper1.unchecked()), obj2.map(mapper2.unchecked()));
    }

    /**
     * If both values are present, apply the first mapper to the first value,
     * and the second mapper to the second value.
     *
     * @see #reduce(CheckedFunction2)
     * @see #map(CheckedFunction1, CheckedFunction1)
     * @see Optional#map(Function)
     */
    public <A, E> Optional2<A, E> map(
        CheckedFunction2<? super C, ? super D, ? extends A> mapper1,
        CheckedFunction2<? super C, ? super D, ? extends E> mapper2) {

        return isEmpty()
            ? empty()
            : new Optional2<>(
                obj1.map(ob1 -> mapper1.unchecked().apply(ob1, obj2.orElseThrow())),
                obj2.map(ob2 -> mapper2.unchecked().apply(obj1.orElseThrow(), ob2)));
    }

    /**
     * If both values are present, combine down to an {@link Optional}.
     * Otherwise return {@link Optional#empty()}.
     * <p>
     * Combines down to an {@code Optional} and not an {@code Optional2}
     * because it's unlikely that users will build up an {@code Optional2},
     * reduce it down, then build yet another {@code Optional2} from the result.
     */
    public <E> Optional<E> reduce(CheckedFunction2<? super C, ? super D, ? extends E> mapper) {

        return isEmpty()
            ? Optional.empty()
            : Optional.ofNullable(mapper.unchecked().apply(obj1.get(), obj2.get()));
    }

    /**
     * Applies {@link Optional#flatMap(Function)} to both values individually.
     * <p>
     * We FlatMap from an {@code Optional} and not another {@code Optional2}
     * because {@code Optional}s are much more common, and {@code Optional2}s can
     * always be reduced to {@code Optional}s.
     *
     * @see Optional#flatMap(Function)
     */
    public <A, E> Optional2<A, E> flatMap(
        CheckedFunction1<? super C, Optional<A>> mapper1,
        CheckedFunction1<? super D, Optional<E>> mapper2) {

        return isEmpty()
            ? empty()
            : new Optional2<>(obj1.flatMap(mapper1.unchecked()), obj2.flatMap(mapper2.unchecked()));
    }

    /**
     * If both values are present, combine down to an {@link Optional}.
     * <p>
     * Combines down to an {@code Optional} and not an {@code Optional2}
     * because it's unlikely that users will build up an {@code Optional2},
     * reduce it down, then build yet another {@code Optional2} from the result.
     */
    public <E> Optional<E> flatMapReduce(CheckedFunction2<? super C, ? super D, Optional<E>> mapper) {

        return isEmpty()
            ? Optional.empty()
            : mapper.unchecked().apply(obj1.get(), obj2.get());
    }

    @Override
    public String toString() {
        return "Optional2[%s,%s]".formatted(
            obj1.isEmpty() ? "null" : obj1.get(),
            obj2.isEmpty() ? "null" : obj2.get());
    }
}

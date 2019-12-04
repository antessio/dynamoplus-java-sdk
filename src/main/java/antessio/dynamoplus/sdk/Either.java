package antessio.dynamoplus.sdk;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Either<Ok, Error> {
    public static <Ok, Error> Either<Ok, Error> ok(Ok value) {
        return new Either<>(Optional.of(value), Optional.empty());
    }

    public static <Ok, Error> Either<Ok, Error> error(Error value) {
        return new Either<>(Optional.empty(), Optional.of(value));
    }

    private final Optional<Ok> ok;
    private final Optional<Error> error;

    private Either(Optional<Ok> ok, Optional<Error> error) {
        this.ok = ok;
        this.error = error;
    }

    public Optional<Ok> ok() {
        return ok;
    }

    public Optional<Error> error() {
        return error;
    }

    public <T> T map(
            Function<? super Ok, ? extends T> okFunc,
            Function<? super Error, ? extends T> errorFunc) {
        return ok.<T>map(okFunc).orElseGet(() -> error.map(errorFunc).get());
    }

    public <T> Either<T, Error> mapOk(Function<? super Ok, ? extends T> okFunc) {
        return new Either<>(ok.map(okFunc), error);
    }

    public <T> Either<Ok, T> mapError(Function<? super Error, ? extends T> errorFunc) {
        return new Either<>(ok, error.map(errorFunc));
    }

    public void apply(Consumer<? super Ok> okFunc, Consumer<? super Error> errorFunc) {
        ok.ifPresent(okFunc);
        error.ifPresent(errorFunc);
    }
}

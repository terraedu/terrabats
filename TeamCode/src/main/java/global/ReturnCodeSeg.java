package global;

@FunctionalInterface
public interface ReturnCodeSeg<R> {
    R run();
}

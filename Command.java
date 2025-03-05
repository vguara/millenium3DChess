public interface Command {
    void execute();
    void undo();
    boolean validate();
}

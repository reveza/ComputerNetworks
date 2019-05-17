public abstract class TransmissionsHandler {
    public abstract <T> T readData();
    public abstract <T> void writeData(T object);
}

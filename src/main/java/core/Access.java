package core;

public enum Access {
    READ,
    WRITE,
    READ_PERSISTENT,
    WRITE_PERSISTENT;
    public static boolean isRead(Access a) {
        return(a==READ||a==READ_PERSISTENT);
    }
    public static boolean isWrite(Access a) {
        return(a==WRITE||a==WRITE_PERSISTENT);
    }
    public static boolean isPersistent(Access a) {
        return(a==READ_PERSISTENT||a==WRITE_PERSISTENT);
    }
}

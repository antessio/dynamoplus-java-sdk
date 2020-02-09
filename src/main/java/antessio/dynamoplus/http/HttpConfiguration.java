package antessio.dynamoplus.http;

public class HttpConfiguration {

    private int connectTimeoutInMilliseconds = 10_000;
    private int readTimeoutInMilliseconds = 10_000;
    private int writeTimeoutInMilliseconds = 10_000;

    public HttpConfiguration() {
    }

    public HttpConfiguration(int connectTimeoutInMilliseconds, int readTimeoutInMilliseconds, int writeTimeoutInMilliseconds) {
        this.connectTimeoutInMilliseconds = connectTimeoutInMilliseconds;
        this.readTimeoutInMilliseconds = readTimeoutInMilliseconds;
        this.writeTimeoutInMilliseconds = writeTimeoutInMilliseconds;
    }

    public int getReadTimeoutInMilliseconds() {
        return readTimeoutInMilliseconds;
    }

    public int getWriteTimeoutInMilliseconds() {
        return writeTimeoutInMilliseconds;
    }

    public int getConnectTimeoutInMilliseconds() {
        return connectTimeoutInMilliseconds;
    }
}

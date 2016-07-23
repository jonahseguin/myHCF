package myscoreboard;

/**
 * Created by jonahseguin on 2016-07-22.
 */
public class MyScoreboardException extends RuntimeException{

    public MyScoreboardException(String message) {
        super(message);
    }


    public MyScoreboardException(String message, Throwable cause) {
        super(message, cause);
    }

}

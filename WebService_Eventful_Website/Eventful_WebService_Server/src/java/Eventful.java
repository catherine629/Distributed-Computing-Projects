

/**
 * Event class aims to abstract the real event happened in the world.
 * Created by cathe on 11/13/2015.
 */
public class Eventful {

    private String title;
    private String time;
    private String location;

    public Eventful(){}
    public Eventful(String title, String time, String location) {
        this.title = title;
        this.time = time;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

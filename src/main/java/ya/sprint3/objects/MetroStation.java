package ya.sprint3.objects;

public class MetroStation {
    private String number;
    private String name;
    private String color;

    public MetroStation(String number, String name, String color) {
        this.number = number;
        this.name = name;
        this.color = color;
    }

    public MetroStation() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "MetroStation{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}

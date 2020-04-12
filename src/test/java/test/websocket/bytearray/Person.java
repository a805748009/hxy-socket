package test.websocket.bytearray;

/**
 * @Description
 * @Author xinyu.huang
 * @Time 2020/4/12 18:13
 */
public class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }
}

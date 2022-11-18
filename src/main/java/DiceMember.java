public class DiceMember implements Comparable<DiceMember> {

    private int num;
    private String name;


    public DiceMember(String author, int num) {
        this.num = num;
        this.name = author;
    }

    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(DiceMember o) {
        return o.num - this.num;
    }





}
import lab1.data.frame.DataFrame;

public class Main {
    public static void main(String[] args) {
        DataFrame df = new DataFrame(new String[] {"kol1", "kol2", "kol3"}, new String[]{"int","double","String"});
        df.addRow(1, 2.0, "lel");
        df.addRow(3, 232.12, "xd");
        df.addRow(3,12.23,"232");
        System.out.println(df);
    }
}

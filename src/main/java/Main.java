import lab1.data.frame.DataFrame;

public class Main {
    public static void main(String[] args) {
        DataFrame df = new DataFrame(new String[] {"kol1", "kol2", "kol3"}, new String[]{"int","double","String"});
        df.addRow(1, 2.0, "lel");
        df.addRow(3, 232.12, "11");
        df.addRow(3,12.23,"sss");
        df.addRow(3,12.23,"fff2");
        df.addRow(25,123.22,"xddd2");
        df.addRow(12,1212.3,"momo2");
        df.addRow(3,1123.231,"lelle");
        df.addRow(312,12.32,"232123");
        DataFrame df2 = df.get(new String[]{"kol1","kol2", "kol3"}, true);
        DataFrame df3 = df.get(new String[]{"kol1","kol3", "kol1"}, false);
        System.out.println(df2.equals(df3) + " " + df2.equals(df3));
        System.out.println(df2);

        System.out.println(df.iloc(2));
        System.out.println(" --- ");
        System.out.println(df.iloc(2, 10));
    }
}

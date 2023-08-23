package org.example;

import java.sql.*;

import static spark.Spark.*;

public class Main {
    private static final String connectionUrl = "jdbc:mysql://localhost:3306/my_sql_demo";
    private static final String username = "root";
    private static final String password = "topsecretpassword";
    private static Connection connection;

    public static void main(String[] args) throws SQLException {

        try {
            connection = DriverManager.getConnection(connectionUrl, username, password);

            get("/dolls", (req, res) -> {
                getAllDolls();
                return "Done";
            });

            get("/doll/:id", (req, res) -> {
                getDoll(Integer.parseInt(req.params(":id")));
                return "Done selecting";
            });

            post("/dolls/:nume/:pret/:stoc", (req, res) -> {
                insertDoll(req.params(":nume"), Double.parseDouble(req.params(":pret")), Integer.parseInt(req.params("stoc")));
                return "Done insert";
            });
            delete("/dolls/:id", (req, res) -> {
                deleteDoll(Integer.parseInt(req.params(":id")));
                return "Done delete";
            });
            put("/dolls/:id/:nume/:pret/:stoc", (req, res) -> {
                updateDoll(Integer.parseInt(req.params(":id")), req.params(":nume"), Double.parseDouble(req.params(":pret")), Integer.parseInt(req.params("stoc")));
                return "Done update";
            });

        } catch (SQLException e) {

            System.out.println(e.getStackTrace());

        }
    }

    private static void updateDoll(int id, String nume, double pret, int stoc) throws SQLException {

        PreparedStatement ps2 = connection.prepareStatement("UPDATE `doll` SET `nume` = ? , `pret` = ? ,`stoc` = ? WHERE id = ? ;");

        ps2.setString(1, nume);
        ps2.setDouble(2, pret);
        ps2.setInt(3, stoc);
        ps2.setInt(4, id);
        ps2.execute();

    }

    private static void insertDoll(String nume, double pret, int stoc) throws SQLException {

        PreparedStatement ps2 = connection.prepareStatement("INSERT INTO `doll` (`nume`, `pret`, `stoc`) VALUES ( ?, ?, ?);");
        ps2.setString(1, nume);
        ps2.setDouble(2, pret);
        ps2.setInt(3, stoc);
        ps2.execute();
    }

    public static void getAllDolls() throws SQLException {

        Statement ps = connection.createStatement();
        ResultSet rs = ps.executeQuery("SELECT * FROM doll;");
        while (rs.next()) {
            Doll c = new Doll(rs.getString("nume"), rs.getDouble("pret"), rs.getInt("stoc"));
            System.out.println(c);
        }
    }

    public static void getDoll(int id) throws SQLException {

        PreparedStatement ps = connection.prepareStatement("SELECT * FROM doll WHERE id = ?");
        ps.setInt(1, id);
        ps.execute();
    }

    private static void deleteDoll(int id) throws SQLException {

        PreparedStatement ps2 = connection.prepareStatement("DELETE FROM doll WHERE id = ? ;");
        ps2.setInt(1, id);
        ps2.execute();

    }

}

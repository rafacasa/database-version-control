
import connection.ConnectionData;
import dao.DAO;
import dao.MySqlDAO;
import java.nio.file.Paths;
import main.Dvs;
import utils.DvsException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rafacasa
 */
public class test {
    public static void main(String[] args) throws DvsException {
        ConnectionData dados = new ConnectionData("csgo", "rafacasa30.ddns.net", "3306", "sigprod2", "sigprod2");
        DAO dao = new MySqlDAO(dados);
        Dvs dvs = new Dvs(Paths.get("./"), dao);
        dvs.verifyVersion();
    }
}

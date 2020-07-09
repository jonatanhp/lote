    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entities.Lote;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import util.ErrorLogger;
import java.util.logging.Level;
import org.sqlite.SQLiteConfig;
/**
 *
 * @author jonatan
 */
public class LoteData {
    static Connection cn = Conn.connectSQLite();
    static PreparedStatement ps;
    static ErrorLogger log = new ErrorLogger(CienteData.class.getName());
    static Date dt = new Date();
    static SimpleDateFormat sdf = new SimpleDateFormat(SQLiteConfig.DEFAULT_DATE_STRING_FORMAT);
    
       public static int create(Lote d) {
        int rsId = 0;
        String[] returns = {"id"};
        String sql = "INSERT INTO lote(numero,direccion,valor_venta,fecha_venta,date_created) "
                + "VALUES(?,?,?,?,?)";
        int i = 0;
        try {
            String fecha = sdf.format(d.getFecha_venta());
            ps = cn.prepareStatement(sql, returns);
            ps.setString(++i, d.getNumero());
            ps.setString(++i, d.getDireccion());
            ps.setDouble(++i, d.getValor_venta());
            ps.setString(++i, fecha);
            
            ps.setString(++i, sdf.format(dt));
            rsId = ps.executeUpdate();// 0 no o 1 si commit
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    rsId = rs.getInt(1); // select tk, max(id)  from producto
                    //System.out.println("rs.getInt(rsId): " + rsId);
                }
                rs.close();
            }
        } catch (SQLException ex) {
            //System.err.println("create:" + ex.toString());
            log.log(Level.SEVERE, "create", ex);
        }
        return rsId;
    }
       
     public static int update(Lote d) {
        System.out.println("actualizar d.getId(): " + d.getId());
        int comit = 0;
        String sql = "UPDATE lote SET "
                + "numero=?, "
                + "direccion=?,"
                + "valor_venta=?,"
                + "fecha_venta=?,"
                + "date_created=? "
                
                + "WHERE id=?";
        int i = 0;
        try {
            String fecha = sdf.format(dt);
            ps = cn.prepareStatement(sql);
            ps.setString(++i, d.getNumero());
            ps.setString(++i, d.getDireccion());
            ps.setDouble(++i, d.getValor_venta());
            
            ps.setString(++i, fecha);
            ps.setString(++i, sdf.format(dt));
            ps.setInt(++i, d.getId());
            comit = ps.executeUpdate();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "update", ex);
        }
        return comit;
    }
     
     public static int delete(int id) throws Exception {
        int comit = 0;
        String sql = "DELETE FROM lote WHERE id = ?";
        try {
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            comit = ps.executeUpdate();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "delete", ex);
            // System.err.println("NO del " + ex.toString());
            throw new Exception("Detalle:" + ex.getMessage());
        }
        return comit;
    }
     
    public static List<Lote> list(String filter) {
        String filtert = null;
        if (filter == null) {
            filtert = "";
        } else {
            filtert = filter;
        }
        System.out.println("list.filtert:" + filtert);

        List<Lote> ls = new ArrayList();
        String sql = "";
        if (filtert.equals("")) {
            sql = "SELECT * FROM lote ORDER BY id";
        } else {
            sql = "SELECT * FROM lote WHERE (id LIKE'" + filter + "%' OR "
                    + "numero LIKE'" + filter +"%'OR direccion LIKE'"+filter+"%'OR valor_venta LIKE'"+filter+"%'OR fecha_venta LIKE'"+filter+ "%' OR date_created LIKE'" + filter + "%' OR "
                    + "id LIKE'" + filter + "%') "
                    + "ORDER BY numero";
        }
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Lote d = new Lote();
                d.setId(rs.getInt("id"));
                d.setNumero(rs.getString("numero"));
                d.setDireccion(rs.getString("direccion"));
                d.setValor_venta(rs.getDouble("valor_venta"));
                
                 String fecha = rs.getString("fecha_venta");
                 try {
                    Date date = sdf.parse(fecha);
                    System.out.println("Xlist.date:" + date);
                   
                    d.setFecha_venta(date);
                    d.setDate_created(sdf.parse(rs.getString("date_created")));
                   // System.out.println("list.date_created:" + rs.getString("date_created"));
                   // System.out.println("list.last_updated:" + rs.getString("last_updated"));
                   

                } catch (Exception e) {
                }
                
                ls.add(d);
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "list", ex);
        }
        return ls;
    }
    
    public static Lote getByPId(int id) {
        Lote d = new Lote();

        String sql = "SELECT * FROM lote WHERE id = ? ";
        int i = 0;
        try {
            ps = cn.prepareStatement(sql);
            ps.setInt(++i, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                d.setId(rs.getInt("id"));
                d.setNumero(rs.getString("numero"));
                d.setDireccion(rs.getString("direccion"));
                d.setValor_venta(rs.getDouble("valor_venta"));
                
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "getByPId", ex);
        }
        return d;
    }
}

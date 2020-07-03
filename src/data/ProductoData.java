/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entities.Producto;
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
public class ProductoData {
    static Connection cn = Conn.connectSQLite();
    static PreparedStatement ps;
    static ErrorLogger log = new ErrorLogger(CienteData.class.getName());
    static Date dt = new Date();
    static SimpleDateFormat sdf = new SimpleDateFormat(SQLiteConfig.DEFAULT_DATE_STRING_FORMAT);
    
       public static int create(Producto d) {
        int rsId = 0;
        String[] returns = {"id"};
        String sql = "INSERT INTO producto(nombres,cod,precio,cantidad,info_adic,fecha_creacion) "
                + "VALUES(?,?,?,?,?,?)";
        int i = 0;
        try {
            ps = cn.prepareStatement(sql, returns);
            ps.setString(++i, d.getNomprod());
            ps.setString(++i, d.getCodprod());
            ps.setDouble(++i, d.getPreciou());
            ps.setInt(++i, d.getCant());
            ps.setString(++i, d.getInfoadic());
            ps.setString(++i, sdf.format(d.getFecha_creacion()));
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
       
     public static int update(Producto d) {
        System.out.println("actualizar d.getId(): " + d.getIdprod());
        int comit = 0;
        String sql = "UPDATE producto SET "
                + "nombres=?, "
                + "cod=?,"
                + "precio=?,"
                + "cantidad=?,"
                + "info_adic=?, "
                + "fecha_creacion=?"
                + "WHERE id=?";
        int i = 0;
        try {
            ps = cn.prepareStatement(sql);
            ps.setString(++i, d.getNomprod());
            ps.setString(++i, d.getCodprod());
            ps.setDouble(++i, d.getPreciou());
            ps.setInt(++i, d.getCant());
            ps.setString(++i, d.getInfoadic());
            ps.setString(++i, sdf.format(dt));
            ps.setInt(++i, d.getIdprod());
            comit = ps.executeUpdate();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "update", ex);
        }
        return comit;
    }
     
     public static int delete(int id) throws Exception {
        int comit = 0;
        String sql = "DELETE FROM producto WHERE id = ?";
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
     
    public static List<Producto> list(String filter) {
        String filtert = null;
        if (filter == null) {
            filtert = "";
        } else {
            filtert = filter;
        }
        System.out.println("list.filtert:" + filtert);

        List<Producto> ls = new ArrayList();
        String sql = "";
        if (filtert.equals("")) {
            sql = "SELECT * FROM producto ORDER BY id";
        } else {
            sql = "SELECT * FROM producto WHERE (id LIKE'" + filter + "%' OR "
                    + "nombres LIKE'" + filter +"%'OR cod LIKE'"+filter+"%'OR precio LIKE'"+filter+"%'OR cantidad LIKE'"+filter+ "%' OR infoadic LIKE'" + filter + "%' OR "
                    + "id LIKE'" + filter + "%') "
                    + "ORDER BY nombres";
        }
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Producto d = new Producto();
                d.setIdprod(rs.getInt("id"));
                d.setNomprod(rs.getString("nombres"));
                d.setCodprod(rs.getString("cod"));
                d.setPreciou(rs.getDouble("precio"));
                d.setCant(rs.getInt("cantidad"));
                d.setInfoadic(rs.getString("info_adic"));
                 String fecha = rs.getString("fecha_creacion");
                 try {
                    Date date = sdf.parse(fecha);
                    System.out.println("Xlist.date:" + date);
                   

                    d.setFecha_creacion(sdf.parse(rs.getString("fecha_creacion")));
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
    
    public static Producto getByPId(int id) {
        Producto d = new Producto();

        String sql = "SELECT * FROM producto WHERE id = ? ";
        int i = 0;
        try {
            ps = cn.prepareStatement(sql);
            ps.setInt(++i, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                d.setIdprod(rs.getInt("id"));
                d.setNomprod(rs.getString("nombres"));
                d.setCodprod(rs.getString("cod"));
                d.setPreciou(rs.getDouble("precio"));
                d.setCant(rs.getInt("cantidad"));
                d.setInfoadic(rs.getString("info_adic"));
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "getByPId", ex);
        }
        return d;
    }
}

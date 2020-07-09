package igu.ventas.lote;

import igu.compras.productos.*;
import data.LoteData;
import entities.Lote;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Asullom
 */
public class LoteTableModel extends AbstractTableModel {

    private List<Lote> lis = new ArrayList();
    private String[] columns = {"#", "numero","direccion","valor_venta","fecha_venta", "date_created"};
    private Class[] columnsType = {Integer.class, String.class, String.class,Double.class,Date.class,Date.class};

    public LoteTableModel() {
        lis = LoteData.list("");
    }

    public LoteTableModel(String filter) {
        lis = LoteData.list(filter);
    }

    @Override
    public Object getValueAt(int row, int column) {
        Lote d = (Lote) lis.get(row);
        switch (column) {
            case 0:
                return row + 1;
            case 1:
                return d.getNumero();
            case 2:
                return d.getDireccion();
            case 3:
                return d.getValor_venta();
            case 4:
                return d.getFecha_venta();
             
            case 5:
                return d.getDate_created();
            
            default:
                return null;
        }
    }
/*
    @Override
    public void setValueAt(Object valor, int row, int column) {
        Lote d = (Lote) lis.get(row);
        switch (column) {
            
           // case 0:
           //     int id = 0;
           //     try {
            //        if (valor instanceof Number) {
           //             id = Integer.parseInt("" + valor);
           //         }
           //     } catch (NumberFormatException nfe) {
            //        System.err.println("" + nfe);
             //   }
            //    d.setId(id);
             //   break;
             
            case 1:
                d.setNombres("" + valor);
                break;
            case 2:
                d.setInfoadic("" + valor);
                break;

        }
        this.fireTableRowsUpdated(row, row);
        //fireTableCellUpdated(row, row);
    }
*/
    @Override
    public boolean isCellEditable(int row, int column) {
        //Lote c = (Lote) lis.get(row);
        if (column >= 0 && column != 0) {
            //return true;
        }
        return false;//bloquear edicion
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Class getColumnClass(int column) {
        return columnsType[column];
    }

    @Override
    public int getRowCount() {
        return lis.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    public void addRow(Lote d) { // con db no se usa
        this.lis.add(d);
        //this.fireTableDataChanged();
        this.fireTableRowsInserted(lis.size(), lis.size());
    }

    public void removeRow(int linha) { // con db no se usa
        this.lis.remove(linha);
        this.fireTableRowsDeleted(linha, linha);
    }

    public Object getRow(int row) { // usado para paintForm
        this.fireTableRowsUpdated(row, row);
        return lis.get(row);
    }

}

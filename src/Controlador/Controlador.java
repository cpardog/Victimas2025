package Controlador;

import Utilidades.Funciones;
import Modelo.Conexion;
import Modelo.Resolucion;
import Modelo.ResolucionDAO;
import Vista.FrmResoluciones;
import Vista.FrmPrincipal;
import Vista.FrmResumen;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import java.sql.Connection;
import java.util.ArrayList;
import net.sf.jasperreports.engine.JRException;
import Modelo.*;
import Vista.FrmUsuarios;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author Admin
 */
public class Controlador implements ActionListener {

    private Resolucion resol;
    private ResolucionDAO resolDAO;
    public FrmPrincipal frP;
    public FrmResoluciones frC;
    public FrmResumen frR;
    public BeneficiarioDAO beneDAO;
    public Beneficiario bene;
    public FrmUsuarios frU;
    public UserDAO usrDAO;
    public User usr;

    /**
     * Este es ek constructor del controlador
     */
    public Controlador() {
        frC = new FrmResoluciones();
        frP = new FrmPrincipal();
        resol = new Resolucion();
        resolDAO = new ResolucionDAO();
        bene = new Beneficiario();
        beneDAO = new BeneficiarioDAO();
        frR = new FrmResumen();
        frU = new FrmUsuarios();
        usr = new User();
        usrDAO = new UserDAO();
    }

    /**
     * Método para inicializar y adicionar objetos al listener de eventos de
     * Java
     */
    public void iniciar() {
        frC.setTitle("Ayuda Humanitaria Inmediata -AHI-");
        frP.mnuresoluciones.addActionListener(this);
        frP.mnutotales.addActionListener(this);
        frP.mnurpttotales.addActionListener(this);

        //Para El Formulario de Usuarios
        frP.mnuUsuarios.addActionListener(this);

        frC.optdocumento.addActionListener(this);
        frC.optresolucion.addActionListener(this);
        frC.txtidresolucion.enable(false);

        frC.btnLimpiar.addActionListener(this);
        frC.btnAgregar.addActionListener(this);
        frC.btneliminar.addActionListener(this);

        frC.btnActualizar.addActionListener(this);
        frC.btnimprimir.addActionListener(this);
        frC.btnimprimir1.addActionListener(this);
        frC.btnbuscar.addActionListener(this);
        frC.btnbeneficiarios.addActionListener(this);

        frC.btneliminarb.addActionListener(this);
        frC.btnActualizarb.addActionListener(this);
        frC.btnAgregarb.addActionListener(this);
        frC.btnLimpiarb.addActionListener(this);

        //Agregar los elementos del Formulario de Usuarios.
        frU.btnlimpiar.addActionListener(this);
        frU.btnactualizar.addActionListener(this);
        frU.btnagregar.addActionListener(this);
        frU.btneliminar.addActionListener(this);
        frU.btnimprimir.addActionListener(this);
        frU.optidusuario.addActionListener(this);
        frU.optlogin.addActionListener(this);

        frR.btnporfecha.addActionListener(this);
        frR.btnpormes.addActionListener(this);
        frP.setLocationRelativeTo(null);
        frP.setVisible(true);
    }

    /**
     * Este método permite Actuar de acuerdo al menú Principal
     *
     * @param e Este parámetro se utiliza para determinar que acción tomar o que
     * opción escoger
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {

            case "Resoluciones" -> {
                frP.setExtendedState(Frame.MAXIMIZED_BOTH);
                frP.getEscritorio().add(frC);
                frC.setVisible(true);
                llenarTabla(resolDAO.select());
            }

            case "Usuarios" -> {
                frP.setExtendedState(Frame.MAXIMIZED_BOTH);
                frP.getEscritorio().add(frU);
                frU.setVisible(true);
                llenarTablaUsuarios(usrDAO.select());
            }
            case "Por Fecha" -> {
                limpiarTabla((DefaultTableModel) frR.tablaresumen.getModel());
                frR.iniciotablaPorFecha();
                frR.lblaviso.setText("Resumen Por Fecha");
                llenarTablaResumen(resolDAO.selectPorFecha());
                double[] arrResumen = new double[3];
                arrResumen = calcularTotales(resolDAO.selectPorFecha());
                frR.txttotalpersonas.setText(String.valueOf(arrResumen[0]));
                frR.txttotalmontos.setText(String.valueOf(FormatoNum(arrResumen[1])));
                frR.txttotalbeneficarios.setText(String.valueOf(FormatoNum(arrResumen[2])));

                frR.txttotalpersonas.enable(false);
                frR.txttotalmontos.enable(false);
                frR.txttotalbeneficarios.enable(false);
            }
            case "Por Mes" -> {
                limpiarTabla((DefaultTableModel) frR.tablaresumen.getModel());
                frR.iniciotablaPorMes();
                frR.lblaviso.setText("Resumen Por Mes");
                llenarTablaResumen(resolDAO.selectPorMes());
            }
            case "Totales" -> {
                frP.setExtendedState(Frame.MAXIMIZED_BOTH);
                frP.getEscritorio().add(frR);
                frR.setVisible(true);
                //llenarTabla(resolDAO.select());
            }
            case "Limpiar" -> {
                limpiarCampos();
            }
            case "Limpiar_Bene" -> {
                limpiarCamposbene();
            }
            case "Agregar" -> {
                if (frC.jdcfresol.getDate().toString().isEmpty() || frC.txtbeneficiarias.getText().isEmpty()
                        || Double.parseDouble(frC.txtmonto.getText()) <= 0) {
                    JOptionPane.showMessageDialog(frC, "No se admiten campos en blanco");
                } else {
                    resolDAO = new ResolucionDAO();
                    resol.setNum_resolucion(frC.txtnumresolucion.getText());
                    resol.setCedula_principal(frC.txtcedulaprincipal.getText());
                    resol.setNom_principal(frC.txtnomprincipal.getText());
                    resol.setApe_principal(frC.txtapeprincipal.getText());
                    resol.setFecha_resolucion(Funciones.convertirDateASql(frC.jdcfresol.getDate()));
                    resol.setFam_beneficiarias(Integer.parseInt(frC.txtbeneficiarias.getText()));
                    resol.setMonto_resolucion(Double.parseDouble(frC.txtmonto.getText()));
                    resol.setEmitida_por(frC.txtemitidapor.getText());
                    resol.setFecha_registro(Funciones.convertirDateASql(frC.jdcfregistro.getDate()));
                    resolDAO.insert(resol);
                    limpiarCampos();
                }
                limpiarTabla((DefaultTableModel) frC.tabla.getModel());
                llenarTabla(resolDAO.select());
            }
            case "AgregarU" -> {
                if (frU.txtlogin.getText().isEmpty() || frU.txtclave.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frC, "No se admiten campos en blanco");
                } else {
                    usrDAO = new UserDAO();
                    usr.setLogin(frU.txtlogin.getText());
                    usr.setClave(frU.txtclave.getText());
                    usr.setEmail(frU.txtcorreo.getText());
                    usr.setActivo(frU.txtestado.getText());
                    usr.setRolNombre(frU.cbonombrerol.getSelectedItem().toString());
                    usrDAO.insert(usr);
                    limpiarCamposU();
                }
                limpiarTabla((DefaultTableModel) frU.tablausuarios.getModel());
                llenarTablaUsuarios(usrDAO.select());
            }
            case "Eliminar" -> {
                if (!frC.txtidresolucion.getText().isEmpty()) {
                    if (JOptionPane.showConfirmDialog(frC, "Borrado", "Confirmación Borrado", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        eliminareg();
                        limpiarTabla((DefaultTableModel) frC.tabla.getModel());
                        llenarTabla(resolDAO.select());
                        limpiarCampos();
                    }
                } else {
                    JOptionPane.showMessageDialog(frC, "Debe seleccionar un registro");
                }
            }
            case "EliminarU" -> {
                if (!frU.txtidusuario.getText().isEmpty()) {
                    if (JOptionPane.showConfirmDialog(frU, "Borrado", "Confirmación Borrado", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        eliminaregU();
                        limpiarTabla((DefaultTableModel) frU.tablausuarios.getModel());
                        llenarTablaUsuarios(usrDAO.select());
                        limpiarCampos();
                    }
                } else {
                    JOptionPane.showMessageDialog(frU, "Debe seleccionar un registro");
                }
            }
            case "Eliminar_Bene" -> {
                if (frC.txtidbeneficiario.getText().isEmpty() || frC.txtnombrebeneficiario.getText().isEmpty() || frC.txtapellidobeneficiario.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frC, "No se admiten campos en blanco");
                    JOptionPane.showMessageDialog(frC, "Debe seleccionar un registro");
                } else if (JOptionPane.showConfirmDialog(frC, "Borrado", "Confirmación Borrado", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    {
                        eliminaregbene();
                        limpiarTabla((DefaultTableModel) frC.tablab.getModel());
                        llenarTablaBene(beneDAO.selectbene(Integer.parseInt(frC.txtnumresolucionb.getText())));
                        limpiarCamposbene();
                    }
                }
            }
            case "Beneficiarios" -> {
                if (frC.txtidresolucion.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro");
                    //eliminareg();
                    //limpiarTabla();
                    //llenarTabla(resolDAO.select());
                    //limpiarCampos();
                } else {
                    String numres = frC.txtidresolucion.getText();
                    //JOptionPane.showMessageDialog(null, frC.txtidresolucion.getText());
                    frC.pestResoluciones.setSelectedIndex(1);
                    frC.txtnumresolucionb.setText(numres);
                    frC.txtnumresolucionb.enable(false);
                    llenarTablaBene(beneDAO.selectbene(Integer.parseInt(numres)));
                    int nbenef = beneDAO.selectcantbene(Integer.parseInt((frC.txtidresolucion.getText())));
                    //JOptionPane.showMessageDialog(null, nbenef);
                }

            }

            case "Actualizar" -> {
                if (frC.jdcfresol.getDate()==null || frC.txtbeneficiarias.getText().isEmpty() 
                        || frC.jdcfregistro.getDate()==null  || Double.parseDouble(frC.txtbeneficiarias.getText()) <=0 ) {
                    JOptionPane.showMessageDialog(frC, "No se admiten campos en blanco");
                } else {
                    resolDAO = new ResolucionDAO();
                    resol.setId_resolucion(Integer.parseInt(frC.txtidresolucion.getText()));
                    resol.setCedula_principal(frC.txtcedulaprincipal.getText());
                    resol.setNom_principal(frC.txtnomprincipal.getText());
                    resol.setApe_principal(frC.txtapeprincipal.getText());
                    resol.setNum_resolucion(frC.txtnumresolucion.getText());

                    resol.setFecha_resolucion(Funciones.convertirDateASql(frC.jdcfresol.getDate()));
                    resol.setFam_beneficiarias(Integer.parseInt(frC.txtbeneficiarias.getText()));
                    resol.setMonto_resolucion(Double.parseDouble(frC.txtmonto.getText()));
                    resol.setEmitida_por(frC.txtemitidapor.getText());
                    resol.setFecha_registro(Funciones.convertirDateASql(frC.jdcfregistro.getDate()));
                    
                    if (resolDAO.update(resol)==1) {
                        JOptionPane.showMessageDialog(frP, "Registro Actualizado correctamente");
                    }
                    else    {
                        JOptionPane.showMessageDialog(frP, "Favor Revisar los datos");
                    }
                    
                    limpiarCampos();
                }
                limpiarTabla((DefaultTableModel) frC.tabla.getModel());   
                llenarTabla(resolDAO.select());
            }
            case "ActualizarU" -> {
                if (frU.txtidusuario.toString().isEmpty() || frU.txtlogin.getText().isEmpty() || frU.txtclave.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frU, "No se admiten campos en blanco");
                } else {
                    usrDAO = new UserDAO();
                    usr.setId_usuario(Integer.parseInt(frU.txtidusuario.getText()));
                    usr.setLogin(frU.txtlogin.getText());
                    usr.setClave(frU.txtclave.getText());
                    usr.setEmail(frU.txtcorreo.getText());
                    // Para validar ya que no todos deben estar activos
                    usr.setActivo(frU.txtestado.getText());
                    usr.setRolNombre(frU.cbonombrerol.getSelectedItem().toString());
                    usrDAO.update(usr);
                    //limpiarCampos();
                }
                limpiarTabla((DefaultTableModel) frU.tablausuarios.getModel());
                llenarTablaUsuarios(usrDAO.select());
            }
            case "Act_Bene" -> {
                if (this.frC.txtidbeneficiario.getText().isEmpty()
                        || this.frC.txtnombrebeneficiario.getText().isEmpty() || this.frC.txtapellidobeneficiario.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frC, "No se admiten campos en blanco");
                } else {
                    beneDAO = new BeneficiarioDAO();
                    bene.setId_beneficiario(this.frC.txtidbeneficiario.getText());
                    bene.setNombre_beneficiario(this.frC.txtnombrebeneficiario.getText());
                    bene.setApellido_beneficiario(this.frC.txtapellidobeneficiario.getText());
                    int filasect = this.frC.tablab.getSelectedRow();
                    String strbene = String.valueOf(this.frC.tablab.getValueAt(filasect, 1));
                    beneDAO.update(bene, strbene);
                    limpiarCamposbene();
                }
                limpiarTabla((DefaultTableModel) frC.tablab.getModel());
                llenarTablaBene(beneDAO.selectbene(Integer.parseInt(this.frC.txtnumresolucionb.getText())));
            }
            case "Agregar_Bene" -> {
                if (frC.txtnumresolucionb.getText().isEmpty() || frC.txtidbeneficiario.getText().isEmpty()
                        || frC.txtnombrebeneficiario.getText().isEmpty() || frC.txtapellidobeneficiario.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frC, "No se admiten campos en blanco");
                } else {
                    beneDAO = new BeneficiarioDAO();
                    bene.setResolucion_id(Integer.parseInt(frC.txtnumresolucionb.getText()));
                    bene.setId_beneficiario(frC.txtidbeneficiario.getText());
                    bene.setNombre_beneficiario(frC.txtnombrebeneficiario.getText());
                    bene.setApellido_beneficiario(frC.txtapellidobeneficiario.getText());

                    beneDAO.insert(bene);
                    limpiarCamposbene();
                }
                //Pendiente (Estos dos)
                limpiarTabla((DefaultTableModel) frC.tablab.getModel());
                llenarTablaBene(beneDAO.selectbene(bene.getResolucion_id()));
            }
            case "Buscar" -> {
                //1.Limpiamos la ventana antes de mostrar nada
                limpiarTabla((DefaultTableModel) frC.tabla.getModel());
                ArrayList<Resolucion> listaresoluciones;
                //2. Abrimos la Conexión
                //conn= Conexion.obtenercoinexion();
                //Obtenemos el nombre del producto a buscar
                String nombrebuscar = frC.txtbuscar.getText();
                //4. Buscamos el usuario segun nombre cedula
                if (frC.optdocumento.isSelected()) {
                    listaresoluciones = resolDAO.buscarPorCedulaPrincipal(nombrebuscar);
                } else {
                    listaresoluciones = resolDAO.buscarPorNumeroResolucion(nombrebuscar);
                }
                //5.Cargamos la tabla con los resultados
                //llenartabla(llenarTabla(resolDAO.select()););
                llenarTabla(listaresoluciones);
                // 6.Cerramos los conexión
                //conn.close();
            }
            case "Imprimir" -> {
                String reporte = "Resoluciones.jasper";
                imprimir(reporte);
            }

            case "Imprimir_Bene" -> {
                String reporte = "Beneficiarios.jasper";
                imprimir(reporte);
            }
            case "Reporte Totales" -> {
                String reporte = "RptResumen.jasper";
                imprimir(reporte);
            }
        }
    }

    public String FormatoNum(double par) {

        NumberFormat numberformat = new DecimalFormat("#,###,###,###.##");
        return numberformat.format(par);
    }

    /**
     * Método para generar informes en la aplicción Se envía el nombre del
     * reporte como parámetro String
     *
     * @param rptImprimir
     */
    public void imprimir(String rptImprimir) {

        Connection conn;

        try {

            String Path;
            Path = rptImprimir;
            conn = Conexion.getConexion();
            JasperPrint informe;
            informe = JasperFillManager.fillReport(Path, null, conn);
            JasperViewer ventanavisor;
            ventanavisor = new JasperViewer(informe, false);
            ventanavisor.setVisible(true);
        } catch (JRException e) {
            System.err.println("Error :" + e.getMessage());
            System.err.println("Error :" + e.getMessageKey());
        } catch (ClassNotFoundException ex) {
            System.err.println("Error" + ex.getMessage());
        }
    }

    /**
     * Metodo para visualizar los registros en pantalla desde la base de datos
     * Persistente LLenar la tabla con Resoluciones - Principales
     *
     * @param listado - permite saber con que datos se llena dicha tabla
     */
    public void llenarTabla(ArrayList<Resolucion> listado) {

        DefaultTableModel tabla;
        tabla = (DefaultTableModel) frC.tabla.getModel();
        limpiarTabla(tabla);
        String[] camposResol = new String[10];
        for (Resolucion res : listado) {
            camposResol[0] = String.valueOf(res.getId_resolucion());
            camposResol[1] = String.valueOf(res.getNum_resolucion());
            camposResol[2] = String.valueOf(res.getCedula_principal());
            camposResol[3] = String.valueOf(res.getNom_principal());
            camposResol[4] = String.valueOf(res.getApe_principal());
            camposResol[5] = String.valueOf(res.getFecha_resolucion());
            camposResol[6] = String.valueOf(res.getFam_beneficiarias());
            camposResol[7] = String.valueOf(res.getMonto_resolucion());
            camposResol[8] = String.valueOf(res.getEmitida_por());
            camposResol[9] = String.valueOf(res.getFecha_registro());
            tabla.addRow(camposResol);
        }
    }

    public void llenarTablaUsuarios(ArrayList<User> listado) {

        DefaultTableModel tablausuarios;
        tablausuarios = (DefaultTableModel) frU.tablausuarios.getModel();
        limpiarTabla(tablausuarios);
        String[] camposUser = new String[6];
        for (User usr : listado) {
            camposUser[0] = String.valueOf(usr.getId_usuario());
            camposUser[1] = String.valueOf(usr.getLogin());
            camposUser[2] = String.valueOf(usr.getClave());
            camposUser[3] = String.valueOf(usr.getEmail());
            camposUser[4] = String.valueOf(usr.getActivo());
             camposUser[5] = String.valueOf(usr.getRolNombre());
            tablausuarios.addRow(camposUser);
        }
    }

    /**
     * Metodo para visualizar los registros en pantalla desde la base de datos
     * Persistente LLenar la tabla con Beneficiarios
     *
     * @param listado - permite saber con que datos se llena dicha tabla
     */
    public void llenarTablaBene(ArrayList<Beneficiario> listado) {
        limpiarTabla((DefaultTableModel) frC.tablab.getModel());
        //ResolucionJDBC resJDBC = new ResolucionDAO();
        DefaultTableModel tabla;
        tabla = (DefaultTableModel) frC.tablab.getModel();
        String[] camposBene = new String[4];
        for (Beneficiario bene : listado) {
            camposBene[0] = String.valueOf(bene.getResolucion_id());
            camposBene[1] = String.valueOf(bene.getId_beneficiario());
            camposBene[2] = String.valueOf(bene.getNombre_beneficiario());
            camposBene[3] = String.valueOf(bene.getApellido_beneficiario());
            tabla.addRow(camposBene);
        }
    }

    /**
     * Llenamos la tabla Resumen
     *
     * @param listado Con los datos de este Listado se llena la tabla
     */
    public void llenarTablaResumen(ArrayList<Resumen> listado) {
        limpiarTabla((DefaultTableModel) frC.tabla.getModel());
        //ResolucionJDBC resJDBC = new ResolucionDAO();
        DefaultTableModel tabla;
        tabla = (DefaultTableModel) frR.tablaresumen.getModel();
        String[] camposResumen = new String[5];
        for (Resumen res : listado) {

            camposResumen[0] = String.valueOf(res.getCampo1());
            camposResumen[1] = String.valueOf(res.getCampo2());
            camposResumen[2] = String.valueOf(res.getCampo3());
            camposResumen[3] = formatear(String.valueOf(res.getCampo4()));
            camposResumen[4] = String.valueOf(res.getCampo5());
            tabla.addRow(camposResumen);
        }
    }

    public double[] calcularTotales(ArrayList<Resumen> listado) {
        //limpiarTabla();
        //ResolucionJDBC resJDBC = new ResolucionDAO();
        //DefaultTableModel tabla;
        //tabla = (DefaultTableModel) frR.tablaresumen.getModel();
        double[] camposTotales = new double[3];
        double totalResoluciones = 0;
        double totalMontos = 0;
        double totalBeneficiarios = 0;
        for (Resumen res : listado) {
            totalResoluciones += Double.valueOf(res.getCampo2());
            totalMontos += Double.valueOf(res.getCampo4());
            totalBeneficiarios += Double.valueOf(res.getCampo5());
        }
        camposTotales[0] = totalResoluciones;
        camposTotales[1] = totalMontos;
        camposTotales[2] = totalBeneficiarios;
        return camposTotales;
    }

    /**
     * Rutina que limpia campos del formulario Esto se hace al inicio o después
     * de alguna operación importante
     */
    /**
     * Rutina que sirve para limpiar campos del formulario Esto se hace después
     * de alguna operación importante Esto se hace igualmente con el formulario
     * de Beneficiarios
     */
    public void limpiarCamposbene() {
//      frC.txtidresolucion.setText("");
        frC.txtidbeneficiario.setText("");
        frC.txtnombrebeneficiario.setText("");
        frC.txtapellidobeneficiario.setText("");
        frC.txtbuscarb.setText("");
    }

    /**
     * Sirve para limpiar una determianda tabla de acuerdo al modelo creado y
     * segun necesidades
     *
     * @param tablaModelo - Depende de la tabla aplica a varias tablas
     */
    public void limpiarTabla(DefaultTableModel tablaModelo) {

        DefaultTableModel temp = tablaModelo;
        int filas = temp.getRowCount();

        for (int a = 0; filas > a; a++) {
            temp.removeRow(0);
        }
    }
//
//    public void limpiarTablaResumen() {
//
//        DefaultTableModel temp = (DefaultTableModel) frR.tablaresumen.getModel();
//        int filas = frR.tablaresumen.getRowCount();
//
//        for (int a = 0; filas > a; a++) {
//            temp.removeRow(0);
//        }
//    }

    /**
     * Eliminar Registro de la Base de Datos Está sujete a Integridad
     * Referencial
     */
    public void eliminareg() {
        // Procesa Registros de Resoluciones
        ResolucionDAO resDAO = new ResolucionDAO();
        Resolucion res = new Resolucion();
        int fila = this.frC.tabla.getSelectedRow();
        res.setId_resolucion(Integer.parseInt(String.valueOf(this.frC.tabla.getValueAt(fila, 0))));
        res.setNum_resolucion(String.valueOf(this.frC.tabla.getValueAt(fila, 1)));
        res.setCedula_principal(String.valueOf(this.frC.tabla.getValueAt(fila, 2)));
        res.setNom_principal(String.valueOf(this.frC.tabla.getValueAt(fila, 3)));
        res.setApe_principal(String.valueOf(this.frC.tabla.getValueAt(fila, 4)));
        res.setFecha_resolucion(Funciones.cadenaAfecha(String.valueOf(this.frC.tabla.getValueAt(fila, 5))));
        res.setFam_beneficiarias(Integer.parseInt(String.valueOf(this.frC.tabla.getValueAt(fila, 6))));
        res.setMonto_resolucion(Double.parseDouble(String.valueOf(this.frC.tabla.getValueAt(fila, 7))));
        res.setEmitida_por(String.valueOf(this.frC.tabla.getValueAt(fila, 8)));
        res.setFecha_resolucion(Funciones.cadenaAfecha(String.valueOf(this.frC.tabla.getValueAt(fila, 9))));
        resDAO.delete(res);
    }

    public void eliminaregU() {
        // Procesa Registros de Resoluciones
        UserDAO usrDAO = new UserDAO();
        User usr = new User();
        int fila = this.frU.tablausuarios.getSelectedRow();
        usr.setId_usuario(Integer.parseInt(String.valueOf(this.frU.tablausuarios.getValueAt(fila, 0))));
        usr.setLogin(String.valueOf(this.frU.tablausuarios.getValueAt(fila, 1)));
        usr.setClave(String.valueOf(this.frU.tablausuarios.getValueAt(fila, 2)));
        usr.setEmail(String.valueOf(this.frU.tablausuarios.getValueAt(fila, 3)));
        usr.setActivo(String.valueOf(this.frU.tablausuarios.getValueAt(fila, 4)));

        usrDAO.delete(usr);
    }

    public void eliminaregbene() {
        // Declara variable DAO
        BeneficiarioDAO beneDAO = new BeneficiarioDAO();
        Beneficiario bene = new Beneficiario();
        int fila = this.frC.tablab.getSelectedRow();
        bene.setResolucion_id(Integer.parseInt(String.valueOf(frC.tablab.getValueAt(fila, 0))));
        bene.setId_beneficiario(String.valueOf(frC.tablab.getValueAt(fila, 1)));
        bene.setNombre_beneficiario(String.valueOf(frC.tablab.getValueAt(fila, 2)));
        bene.setApellido_beneficiario(String.valueOf(this.frC.tablab.getValueAt(fila, 3)));
        beneDAO.delete(bene);
    }

    /**
     * Sirve para formatear Texto númerico
     *
     * @param texto a formatear
     * @return Dato formateado de acuerdo al Patrón establecido
     */
    public String formatear(String texto) {
        String numero = null;
        DecimalFormat df = new DecimalFormat("###,###,##0.00");
        try {
            numero = df.format(Double.valueOf(texto));
        } catch (NumberFormatException e) {
            System.err.println("Error en la operación" + e.getMessage());
        }
        return numero;

    }

    /**
     * Aqui se limpiar los campos del Formulario de Resoluciones
     */
    public void limpiarCampos() {
        frC.txtidresolucion.setText("");
        frC.txtnumresolucion.setText("");
        frC.txtnumresolucionb.setText("");
        frC.txtcedulaprincipal.setText("");
        frC.txtnomprincipal.setText("");
        frC.txtapeprincipal.setText("");
        frC.jdcfresol.setDate(null);
        frC.txtbeneficiarias.setText("");
        frC.txtmonto.setText("");
        frC.txtemitidapor.setText("");
        frC.jdcfregistro.setDate(null);
        frC.txtbuscar.setText("");
    }

    public void limpiarCamposU() {
        frU.txtidusuario.setText("");
        frU.txtlogin.setText("");
        frU.txtclave.setText("");
        frU.txtcorreo.setText("");
        frU.txtestado.setText("");
        frU.cbonombrerol.setSelectedItem(null);
    }
}

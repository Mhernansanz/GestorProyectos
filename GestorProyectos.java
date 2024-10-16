/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ActividadesPropuestasLibro1;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

/**
 *
 * @author tarde
 */
public class GestorProyectos {

    private static Connection con = null;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error al cargar el driver");
        }

        String URL = "jdbc:mysql://localhost:3306/GestorProyectos";
        String USER = "root";
        String PASS = "root";

        try {
            con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("-------------------------------");
            menu(sc);
            System.out.println("-------------------------------");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean insertarEmpleados(Scanner sc) throws SQLException {
        boolean creado = false;
        System.out.println("Introduzca el nombre del empleado: ");
        String nombre = sc.nextLine();
        System.out.println("Introduzca el DNI del empleado(8 números y 1 letra): ");
        String dni = sc.nextLine();
        String sql = "INSERT INTO Empleado VALUES(?,?)";
        try (PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, nombre);
            pstm.setString(2, dni);
            int a = pstm.executeUpdate();
            if (a == 1) {
                creado = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return creado;
    }

    public static void insertarProyectos(Scanner sc) throws SQLException {
        System.out.println("Introduzca el nombre del proyecto:");
        String nameProy = sc.nextLine();
        System.out.println("Intriduzca el DNI del jefe del proyecto(DNI del Empleado)");
        String dniJefeProy = sc.nextLine();
        System.out.println("Introduzca la fecha de inicio del proyecto(yyyy-mm-dd):");
        LocalDate fechaInicio = LocalDate.parse(sc.nextLine());
        System.out.println("Introduzca la fecha de fin del proyecto(yyyy-mm-dd):");
        LocalDate fechaFin = LocalDate.parse(sc.nextLine());
        String sql = "INSERT INTO Proyecto(Nombre, DNI_Jefe_Proy, Fec_Inicio, Fec_Fin) VALUES(?,?,?,?)";
        try (PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, nameProy);
            pstm.setString(2, dniJefeProy);
            if (fechaInicio.equals(null)) {
                pstm.setDate(3, Date.valueOf(LocalDate.now()));
            } else {
                pstm.setDate(3, Date.valueOf(fechaInicio));
            }
            if (fechaFin.equals(null)) {
                pstm.setDate(4, null);
            } else {
                pstm.setDate(4, Date.valueOf(fechaFin));
            }
            pstm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        consultaNumeroProyecto();
    }

    public static int consultaNumeroProyecto() throws SQLException {
        String sql = "SELECT Num_proy FROM Proyecto";
        try (PreparedStatement pstm = con.prepareStatement(sql); ResultSet rs = pstm.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    public static void menu(Scanner sc) throws SQLException {
        System.out.println("--------MENÚ--------");
        System.out.println("(1)-Insertar empleados");
        System.out.println("(2)-Insertar proyectos");
        System.out.println("Introduzca una opción(fin para terminar): ");
        String opcion = sc.nextLine();
        do {
            if (opcion.equals("1")) {
                insertarEmpleados(sc);
            } else if (opcion.equals("2")) {
                insertarProyectos(sc);
            } else {
                System.out.println("Introduzca una opción correcta");
            }
        } while (opcion.equalsIgnoreCase("fin"));
    }
}

package aprende.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import aprende.conexion.Conexion;
import aprende.model.Producto;

/**
 * La clase ProductoDAO se encarga de gestionar las operaciones CRUD (crear, leer, 
 * actualizar y eliminar) relacionadas con la tabla "productos" en la base de datos.
 */
public class ProductoDAO {
    private Connection connection;
    private PreparedStatement statement;
    private boolean estadoOperacion;

    /**
     * Guarda un nuevo producto en la base de datos. Si el producto ya existe (basado en 
     * el nombre), no se inserta el producto.
     *
     * @param producto El objeto Producto a ser guardado.
     * @return true si el producto se guardó correctamente, false si el producto ya existe o se produce un error.
     * @throws SQLException Si ocurre un error de SQL.
     */
    public boolean guardar(Producto producto) throws SQLException {
        String sql = null;
        estadoOperacion = false;
        connection = obtenerConexion();

        try {
            connection.setAutoCommit(false);

            // Verificar si el producto ya existe basado en el nombre
            String sqlVerificar = "SELECT COUNT(*) FROM productos WHERE nombre = ?";
            PreparedStatement verificarStmt = connection.prepareStatement(sqlVerificar);
            verificarStmt.setString(1, producto.getNombre());

            ResultSet rs = verificarStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);  // Obtenemos el número de coincidencias por nombre

            // Validaciones adicionales de nombre, cantidad y precio
            if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
                count = 1;
            }
            if (producto.getCantidad() < 0) {
                count = 1;
            }
            if (producto.getPrecio() < 0) {
                count = 1;
            }

            if (count > 0) {
                System.out.println("El producto con nombre '" + producto.getNombre() + "' ya existe.");
                estadoOperacion = false;
            } else {
                // Inserta el nuevo producto
                sql = "INSERT INTO productos (nombre, cantidad, precio, fecha_crear, fecha_actualizar) VALUES(?,?,?,?,?)";
                statement = connection.prepareStatement(sql);

                statement.setString(1, producto.getNombre());
                statement.setDouble(2, producto.getCantidad());
                statement.setDouble(3, producto.getPrecio());
                statement.setTimestamp(4, producto.getFechaCrear());
                statement.setTimestamp(5, producto.getFechaActualizar());

                estadoOperacion = statement.executeUpdate() > 0;

                if (estadoOperacion) {
                    System.out.println("Producto insertado correctamente.");
                }
            }

            connection.commit();
            verificarStmt.close();
            if (statement != null) statement.close();
            connection.close();
        } catch (SQLException e) {
            connection.rollback();
            System.out.println(e.getMessage());
        }

        return estadoOperacion;
    }

    /**
     * Edita un producto existente en la base de datos.
     *
     * @param producto El objeto Producto con los datos actualizados.
     * @return true si el producto se editó correctamente, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    public boolean editar(Producto producto) throws SQLException {
        String sql = null;
        estadoOperacion = false;
        connection = obtenerConexion();
        try {
            connection.setAutoCommit(false);
            sql = "UPDATE productos SET nombre=?, cantidad=?, precio=?, fecha_actualizar=? WHERE id=?";
            statement = connection.prepareStatement(sql);

            statement.setString(1, producto.getNombre());
            statement.setDouble(2, producto.getCantidad());
            statement.setDouble(3, producto.getPrecio());
            statement.setTimestamp(4, producto.getFechaActualizar());
            statement.setInt(5, producto.getId());

            estadoOperacion = statement.executeUpdate() > 0;
            connection.commit();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }

        return estadoOperacion;
    }

    /**
     * Elimina un producto de la base de datos basado en su ID.
     *
     * @param idProducto El ID del producto a eliminar.
     * @return true si el producto se eliminó correctamente, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    public boolean eliminar(int idProducto) throws SQLException {
        String sql = null;
        estadoOperacion = false;
        connection = obtenerConexion();
        try {
            connection.setAutoCommit(false);
            sql = "DELETE FROM productos WHERE id=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, idProducto);

            estadoOperacion = statement.executeUpdate() > 0;
            connection.commit();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }

        return estadoOperacion;
    }

    /**
     * Obtiene una lista de todos los productos almacenados en la base de datos.
     *
     * @return Una lista de objetos Producto.
     * @throws SQLException Si ocurre un error de SQL.
     */
    public List<Producto> obtenerProductos() throws SQLException {
        ResultSet resultSet = null;
        List<Producto> listaProductos = new ArrayList<>();

        String sql = null;
        estadoOperacion = false;
        connection = obtenerConexion();

        try {
            sql = "SELECT * FROM productos";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Producto p = new Producto();
                p.setId(resultSet.getInt(1));
                p.setNombre(resultSet.getString(2));
                p.setCantidad(resultSet.getDouble(3));
                p.setPrecio(resultSet.getDouble(4));
                p.setFechaCrear(resultSet.getTimestamp(5));
                p.setFechaActualizar(resultSet.getTimestamp(6));
                listaProductos.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaProductos;
    }

    /**
     * Obtiene un producto específico de la base de datos basado en su ID.
     *
     * @param idProducto El ID del producto a obtener.
     * @return El objeto Producto correspondiente al ID, o null si no se encuentra.
     * @throws SQLException Si ocurre un error de SQL.
     */
    public Producto obtenerProducto(int idProducto) throws SQLException {
        ResultSet resultSet = null;
        Producto p = new Producto();

        String sql = null;
        estadoOperacion = false;
        connection = obtenerConexion();

        try {
            sql = "SELECT * FROM productos WHERE id =?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, idProducto);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                p.setId(resultSet.getInt(1));
                p.setNombre(resultSet.getString(2));
                p.setCantidad(resultSet.getDouble(3));
                p.setPrecio(resultSet.getDouble(4));
                p.setFechaCrear(resultSet.getTimestamp(5));
                p.setFechaActualizar(resultSet.getTimestamp(6));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return p;
    }

    /**
     * Obtiene una conexión a la base de datos utilizando un pool de conexiones.
     *
     * @return Un objeto Connection que representa la conexión a la base de datos.
     * @throws SQLException Si ocurre un error de SQL al intentar conectar.
     */
    private Connection obtenerConexion() throws SQLException {
        return Conexion.getConnection();
    }
}

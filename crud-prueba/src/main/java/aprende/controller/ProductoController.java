package aprende.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import aprende.dao.ProductoDAO;
import aprende.model.Producto;

/**
 * Servlet implementation class ProductoController
 * 
 * Este servlet administra las peticiones relacionadas con la gestión de la tabla "productos".
 * Ofrece funciones como crear, listar, editar y eliminar productos.
 */
@WebServlet(description = "Administra peticiones para la tabla productos", urlPatterns = { "/productos" })
public class ProductoController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor predeterminado.
	 */
	public ProductoController() {
		super();
	}

	/**
	 * Maneja las solicitudes GET recibidas. Dependiendo del parámetro "opcion", 
	 * realiza las siguientes operaciones: crear, listar, editar o eliminar productos.
	 *
	 * @param request  El objeto HttpServletRequest que contiene la solicitud del cliente.
	 * @param response El objeto HttpServletResponse que contiene la respuesta para el cliente.
	 * @throws ServletException Si ocurre un error en la solicitud del servlet.
	 * @throws IOException      Si ocurre un error de E/S.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Obtener la opción de la solicitud
		String opcion = request.getParameter("opcion");

		// Si la opción es "crear", redirige al formulario de creación
		if ("crear".equals(opcion)) {
			System.out.println("Usted ha presionado la opción crear");
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/crear.jsp");
			requestDispatcher.forward(request, response);
		
		// Si la opción es "listar", obtiene la lista de productos y redirige a la vista de listado
		} else if ("listar".equals(opcion)) {
			ProductoDAO productoDAO = new ProductoDAO();
			List<Producto> lista = new ArrayList<>();
			try {
				lista = productoDAO.obtenerProductos();
				for (Producto producto : lista) {
					System.out.println(producto);
				}
				request.setAttribute("lista", lista);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/listar.jsp");
				requestDispatcher.forward(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("Usted ha presionado la opción listar");
		
		// Si la opción es "meditar", carga el producto por ID y redirige a la vista de edición
		} else if ("meditar".equals(opcion)) {
			int id = Integer.parseInt(request.getParameter("id"));
			System.out.println("Editar id: " + id);
			ProductoDAO productoDAO = new ProductoDAO();
			Producto p = new Producto();
			try {
				p = productoDAO.obtenerProducto(id);
				System.out.println(p);
				request.setAttribute("producto", p);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/editar.jsp");
				requestDispatcher.forward(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		// Si la opción es "eliminar", elimina el producto y actualiza la lista
		} else if ("eliminar".equals(opcion)) {
			ProductoDAO productoDAO = new ProductoDAO();
			int id = Integer.parseInt(request.getParameter("id"));
			try {
				productoDAO.eliminar(id);
				System.out.println("Registro eliminado satisfactoriamente...");
				
				// Actualizar la lista de productos y redirigir a la vista de listado
				try {
					List<Producto> listaActualizada = productoDAO.obtenerProductos();
					request.setAttribute("lista", listaActualizada);

				} catch (SQLException e) {
					e.printStackTrace();
				}
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/listar.jsp");
				requestDispatcher.forward(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Maneja las solicitudes POST recibidas. Dependiendo del parámetro "opcion", realiza 
	 * las operaciones de guardar o editar productos.
	 *
	 * @param request  El objeto HttpServletRequest que contiene la solicitud del cliente.
	 * @param response El objeto HttpServletResponse que contiene la respuesta para el cliente.
	 * @throws ServletException Si ocurre un error en la solicitud del servlet.
	 * @throws IOException      Si ocurre un error de E/S.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Boolean saved = null;
		String opcion = request.getParameter("opcion");

		// Timestamp actual para establecer la fecha y hora de creación o actualización
		Timestamp fechaActual = new Timestamp(System.currentTimeMillis());

		// Guardar un nuevo producto
		if ("guardar".equals(opcion)) {
			ProductoDAO productoDAO = new ProductoDAO();
			Producto producto = new Producto();
			producto.setNombre(request.getParameter("nombre"));
			producto.setCantidad(Double.parseDouble(request.getParameter("cantidad")));
			producto.setPrecio(Double.parseDouble(request.getParameter("precio")));
			producto.setFechaCrear(fechaActual); // Fecha de creación con día y hora

			try {
				if (productoDAO.guardar(producto)) {
					saved = true;
					System.out.println(saved);
					request.setAttribute("saved", saved);
					RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/crear.jsp");
					requestDispatcher.forward(request, response);
				} else {
					saved = false;
					request.setAttribute("saved", saved);
					RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/crear.jsp");
					requestDispatcher.forward(request, response);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		// Guardar desde la lista de productos
		} else if ("guardarList".equals(opcion)) {
			ProductoDAO productoDAO = new ProductoDAO();
			Producto producto = new Producto();
			producto.setNombre(request.getParameter("nombre"));
			producto.setCantidad(Double.parseDouble(request.getParameter("cantidad")));
			producto.setPrecio(Double.parseDouble(request.getParameter("precio")));
			producto.setFechaCrear(fechaActual); // Fecha de creación con día y hora
			String mensaje = null;
			try {
				if (productoDAO.guardar(producto)) {
					saved = true;
					System.out.println(saved);

					// Obtener la lista actualizada de productos para mostrarla en la vista
					try {
						List<Producto> listaActualizada = productoDAO.obtenerProductos();
						request.setAttribute("lista", listaActualizada);

					} catch (SQLException e) {
						e.printStackTrace();
					}
					RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/listar.jsp");
					requestDispatcher.forward(request, response);
					mensaje= "Producto guardado satisfactoriamente";
					request.setAttribute("mensaje", mensaje);
				} else {
					RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/listar.jsp");
					requestDispatcher.forward(request, response);
					request.setAttribute("mensaje", " ");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				request.setAttribute("mensaje", " ");
			}

		// Editar un producto existente
		} else if ("editar".equals(opcion)) {
			Producto producto = new Producto();
			ProductoDAO productoDAO = new ProductoDAO();


			producto.setId(Integer.parseInt(request.getParameter("id")));
			producto.setNombre(request.getParameter("nombre"));
			producto.setCantidad(Double.parseDouble(request.getParameter("cantidad")));
			producto.setPrecio(Double.parseDouble(request.getParameter("precio")));
			producto.setFechaActualizar(fechaActual); // Fecha de actualización con día y hora

			try {
				productoDAO.editar(producto);
				saved = true;
				String mensaje= null;
				System.out.println(saved);

				// Obtener la lista actualizada de productos para mostrarla en la vista
				try {
					List<Producto> listaActualizada = productoDAO.obtenerProductos();
					request.setAttribute("lista", listaActualizada);
					mensaje= "Producto guardado satisfactoriamente";
					request.setAttribute("mensaje", mensaje);
				
				} catch (SQLException e) {
					request.setAttribute("mensaje", " ");
					e.printStackTrace();
				}
				mensaje= "Producto guardado satisfactoriamente";
				request.setAttribute("mensaje", mensaje);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/listar.jsp");
				requestDispatcher.forward(request, response);
				
			} catch (SQLException e) {
				request.setAttribute("mensaje", " ");
				e.printStackTrace();
			}
		}
	}
}

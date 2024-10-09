<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="estilos/estilogeneral.css">
<title>Listar Productos</title>

<script>
	/**
	 * Función que valida los campos del formulario antes de su envío.
	 * Verifica que los campos Nombre, Cantidad y Precio sean válidos.
	 * Muestra mensajes de error si algún campo no es válido.
	 * 
	 * @returns {boolean} True si todos los campos son válidos, de lo contrario False.
	 */
	function validarFormulario() {
		// Limpiar mensajes de error previos
		document.getElementById("errorNombre").innerHTML = "";
		document.getElementById("errorCantidad").innerHTML = "";
		document.getElementById("errorPrecio").innerHTML = "";

		// Obtener valores de los campos
		var nombre = document.forms["productoForm"]["nombre"].value;
		var cantidad = document.forms["productoForm"]["cantidad"].value;
		var precio = document.forms["productoForm"]["precio"].value;

		var isValid = true; // Variable para controlar si el formulario es válido

		// Validación del nombre
		if (nombre.trim() === "") {
			document.getElementById("errorNombre").innerHTML = "Por favor, introduzca un nombre de producto.";
			isValid = false;
		}

		// Validación de la cantidad (número entero positivo)
		if (isNaN(cantidad) || cantidad <= 0
				|| !Number.isInteger(Number(cantidad))) {
			document.getElementById("errorCantidad").innerHTML = "Por favor, introduzca una cantidad válida (entero positivo).";
			isValid = false;
		}

		// Validación del precio (número positivo)
		if (isNaN(precio) || precio <= 0) {
			document.getElementById("errorPrecio").innerHTML = "Por favor, introduzca un precio válido (número positivo).";
			isValid = false;
		}

		// Si isValid es falso, el formulario no se enviará
		return isValid;

	}
</script>
<%
// Obtener el atributo 'saved' que viene desde el servlet
Boolean vsaved = (Boolean) request.getAttribute("saved");

//Obtener atributo mensaje
Object vmensaje = request.getAttribute("mensaje");
%>

</head>
<body>

	<!-- Título de la página -->
	<h1>Listar Productos</h1>

	<div class="cont">
		<!-- Tabla para listar los productos -->
		<table border="1">
			<tr>
				<td>Id</td>
				<td>Nombre</td>
				<td>Cantidad</td>
				<td>Precio</td>
				<td>Fecha Creacion</td>
				<td>Fecha Actualizacion</td>
				<td colspan="2">Accion</td>
			</tr>
			<!-- Iterar sobre la lista de productos -->
			<c:forEach var="producto" items="${lista}">
				<tr>
					<td><c:out value="${producto.id}"></c:out></td>
					<td><c:out value="${producto.nombre}"></c:out></td>
					<td><c:out value="${producto.cantidad}"></c:out></td>
					<td><c:out value="${producto.precio}"></c:out></td>
					<td><fmt:formatDate value="${producto.fechaCrear}"
							pattern="EEEE, dd MMMM yyyy HH:mm:ss" /></td>
					<td><fmt:formatDate value="${producto.fechaActualizar}"
							pattern="EEEE, dd MMMM yyyy HH:mm:ss" /></td>
					<td><a
						href="productos?opcion=eliminar&id=<c:out value='${producto.id}'/>">
							Eliminar </a></td>
					<td>
						<!-- Formulario para modificar un producto -->
						<form action="productos" method="get" style="margin: 0;">
							<input type="hidden" name="opcion" value="meditar"> <input
								type="hidden" name="id" value="<c:out value='${producto.id}'/>">
							<button type="submit">Modificar</button>
						</form>
					</td>
				</tr>
			</c:forEach>
		</table>

		<!-- Formulario para agregar un nuevo producto -->
		<h3>Formulario de Producto</h3>
		<form name="productoForm" action="productos" method="post"
			onsubmit="return validarFormulario()">
			<input type="hidden" name="opcion" value="guardarList">

			<div id="form">
				<table border="1">
					<tr>
						<td>Nombre:</td>
						<td><input type="text" name="nombre" size="50"></td>
					</tr>
					<tr>
						<td colspan="2"><span id="errorNombre" class="error"></span>
						</td>
					</tr>

					<tr>
						<td>Cantidad:</td>
						<td><input type="text" name="cantidad" size="50"></td>
					</tr>
					<tr>
						<td colspan="2"><span id="errorCantidad" class="error"></span>
						</td>
					</tr>

					<tr>
						<td>Precio:</td>
						<td><input type="text" name="precio" size="50"></td>
					</tr>
					<tr>
						<td colspan="2"><span id="errorPrecio" class="error"></span>
						</td>
					</tr>

				</table>
				
				<!-- Mostrar mensajes dependiendo del valor de 'mensaje' -->
				<%
				if (vmensaje != null) {
				%>
				<div class=exito>Producto guardado satisfactoriamente</div>
				<%
				}
				%>


			</div>
			<input type="submit" value="Guardar">
			<button onclick="window.location.href='index.jsp';">Volver</button>
		</form>

	</div>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" 
    pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Editar Producto</title>
    <link rel="stylesheet" type="text/css" href="estilos/estilogeneral.css">
    
    <script>
        /**
         * Funci�n que valida los campos del formulario antes de su env�o.
         * @returns {boolean} True si todos los campos son v�lidos, de lo contrario False.
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

            var isValid = true; // Variable para controlar si el formulario es v�lido

            // Validaci�n del nombre
            if (nombre.trim() === "") {
                document.getElementById("errorNombre").innerHTML = 
                    "Por favor, introduzca un nombre de producto.";
                isValid = false;
            }

            // Validaci�n de la cantidad (n�mero entero positivo)
            if (isNaN(cantidad) || cantidad <= 0 || !Number.isInteger(Number(cantidad))) {
                document.getElementById("errorCantidad").innerHTML = 
                    "Por favor, introduzca una cantidad v�lida (entero positivo).";
                isValid = false;
            }

            // Validaci�n del precio (n�mero positivo)
            if (isNaN(precio) || precio <= 0) {
                document.getElementById("errorPrecio").innerHTML = 
                    "Por favor, introduzca un precio v�lido (n�mero positivo).";
                isValid = false;
            }

            // Si isValid es falso, el formulario no se enviar�
            return isValid;
        }
    </script>
</head>
<body>

    <!-- T�tulo de la p�gina -->
    <h1>Editar Producto</h1>
    
    <div class="cont">
        <!-- Formulario para editar el producto -->
        <form name="productoForm" action="productos" method="post" 
            onsubmit="return validarFormulario()">
            <input type="hidden" name="opcion" value="editar">
            
            <!-- Campo oculto para definir la acci�n como guardar -->
            <input type="hidden" name="opcion" value="guardar">
            
            <!-- Variable producto obtenida desde el controlador -->
            <c:set var="producto" value="${producto}"></c:set>
            
            <!-- Campo oculto para definir la acci�n como editar -->
            <input type="hidden" name="opcion" value="editar">
            
            <!-- Campo oculto para enviar el id del producto -->
            <input type="hidden" name="id" value="${producto.id}">

            <!-- Tabla con los campos del producto -->
            <table border="1">
                <!-- Campo para el nombre del producto -->
                <tr>
                    <td>Nombre:</td>
                    <td><input type="text" name="nombre" size="50" value="${producto.nombre}"></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <span id="errorNombre" class="error"></span>
                    </td>
                </tr>

                <!-- Campo para la cantidad del producto -->
                <tr>
                    <td>Cantidad:</td>
                    <td><input type="text" name="cantidad" size="50" value="${producto.cantidad}"></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <span id="errorCantidad" class="error"></span>
                    </td>
                </tr>

                <!-- Campo para el precio del producto -->
                <tr>
                    <td>Precio:</td>
                    <td><input type="text" name="precio" size="50" value="${producto.precio}"></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <span id="errorPrecio" class="error"></span>
                    </td>
                </tr>
            </table>

            <!-- Botones de acci�n -->
            <input type="submit" value="Guardar">
            <button type="button" onclick="history.back();">Volver</button>
        </form>
    </div>
</body>
</html>

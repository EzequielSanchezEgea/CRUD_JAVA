<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="estilos/estilogeneral.css">
    <title>Crear Producto</title>
    
    <script>
        /**
         * Función para validar el formulario de creación de productos.
         * 
         * Esta función se ejecuta al enviar el formulario. Se encarga de validar
         * que los campos de nombre, cantidad y precio cumplan con los requisitos.
         * 
         * @returns {boolean} Devuelve true si el formulario es válido; de lo contrario, false.
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

            // Validación de la cantidad
            if (isNaN(cantidad) || cantidad <= 0 || !Number.isInteger(Number(cantidad))) {
                document.getElementById("errorCantidad").innerHTML = "Por favor, introduzca una cantidad válida (entero positivo).";
                isValid = false;
            }

            // Validación del precio
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
    %>

</head>
<body>
    <h1>Crear Producto</h1>

    <div class="cont">
        <form name="productoForm" action="productos" method="post" onsubmit="return validarFormulario()">
            <input type="hidden" name="opcion" value="guardar">

            <table border="1">
                <tr>
                    <td>Nombre:</td>
                    <td><input type="text" name="nombre" size="50"></td>
                </tr>
                <tr>
                    <td colspan="2"><span id="errorNombre" class="error"></span></td>
                </tr>

                <tr>
                    <td>Cantidad:</td>
                    <td><input type="text" name="cantidad" size="50"></td>
                </tr>
                <tr>
                    <td colspan="2"><span id="errorCantidad" class="error"></span></td>
                </tr>

                <tr>
                    <td>Precio:</td>
                    <td><input type="text" name="precio" size="50"></td>
                </tr>
                <tr>
                    <td colspan="2"><span id="errorPrecio" class="error"></span></td>
                </tr>
            </table>

            <!-- Mostrar mensajes dependiendo del valor de 'saved' -->
            <% if (vsaved != null) { %>
                <% if (vsaved) { %>
                    <div class="exito">
                        Producto guardado satisfactoriamente
                    </div>
                <% } else { %>
                    <div class="error">
                        Error. Producto ya existente.
                    </div>
                <% } %>
            <% } %>

            <input type="submit" value="Guardar">
            <button type="button" onclick="window.location.href='index.jsp';">Volver</button>
        </form>
    </div>
</body>
</html>

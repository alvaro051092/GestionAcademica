<%-- 
    Document   : solPswRecovery
    Created on : 28-ago-2017, 15:03:12
    Author     : alvar
--%>

<%@page import="Utiles.Utilidades"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Enumerado.NombreSesiones"%>
<%

    Utilidades utilidad = Utilidades.GetInstancia();
    String urlSistema = (String) session.getAttribute(NombreSesiones.URL_SISTEMA.getValor());

    String js_redirect = "window.location.replace('" + urlSistema + "');";

%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Recuperar contraseña</title>
        <jsp:include page="/masterPage/head.jsp"/>
    </head>
    <body>
        <jsp:include page="/masterPage/NotificacionError.jsp"/>
        <div class="wrapper">
            <jsp:include page="/masterPage/menu_izquierdo.jsp" />

            <div id="contenido" name="contenido" class="main-panel">

                <div class="contenedor-cabezal">
                    <jsp:include page="/masterPage/cabezal.jsp"/>
                </div>

                <div class="contenedor-principal">
                    <div class="col-sm-11 contenedor-texto-titulo-flotante">
                        <div class="contenedor-titulo">    
                            <p>Recuperar contraseña</p>
                        </div>
                        <div style="height: 30px;"></div>
                        <form>
                            <div>Usuario: <input type="text" required="true" name="usuario" id="usuario"></div>
                            <input name="btn_guardar" id="btn_guardar" value="Guardar" class="btn btn-success" type="button" />
                        </form>
                        
                    </div>
                </div>
            </div>
        </div>
        <script>
            $(document).ready(function () {
                $('#btn_guardar').click(function (event) {
                    var usuario = $('#usuario').val();
                    
                    
                    // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                    $.post('<% out.print(urlSistema); %>ABM_Persona', {
                        usuario: usuario,
                        pAction: "SOL_PSW_RECOVERY"
                    }, function (responseText) {
                        var obj = JSON.parse(responseText);

                        MostrarMensaje(obj.tipoMensaje, obj.mensaje);

                        if (obj.tipoMensaje != 'ERROR')
                        {
                            <%=js_redirect%>
                        } 

                    });
                    
                });
            });
        </script>
    </body>
</html>


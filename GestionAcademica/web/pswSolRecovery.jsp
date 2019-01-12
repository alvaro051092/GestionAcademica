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
    String urlSistema = utilidad.GetUrlSistema();

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
    <body class="body_clase">
        <div class="login_fondo">		
            <div class="login_contenedor">
                
                <div class="login_contenedorImg"><img src="Imagenes/ctc.png" /></div>
                <h1 class="login_titulo">RECUPERAR CONTRASEÑA</h1>
                <p class="login_texto">Bienvenido a Gestión, el servicio a estudiantes del Instituto CTC - Colonia. Indique su usuario, y se le enviará un email con las instrucciones para recuperar su cuenta</p>
                <form>
                    <div class="login_form">
                        <input type="text" class="form-control login_inputNumero" required="true" name="usuario" id="usuario" placeholder="Usuario">
                    </div>

                    <button type="button" name="btnRecuperar" id="btnRecuperar" class="login_boton">RECUPERAR</button>
                </form>


            </div>
        </div>

        <div>
            <div id="div_pop_bkgr" name="div_pop_bkgr"></div>

            <div id="div_cargando" name="div_cargando">
                <div class="loading"></div>
            </div>

        </div>

        <div id="msgError" name="msgError" class="alert alert-success div_msg" style="display: none;"> 
            <label id="txtError" name="txtError">Error</label>
        </div>

        <script>
            $(document).ready(function () {
                MostrarCargando(false);

                $('#btnRecuperar').click(function (event) {
                    MostrarCargando(true);

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

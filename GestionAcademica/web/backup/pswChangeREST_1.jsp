<%-- 
    Document   : pswChange
    Created on : 28-ago-2017, 19:29:24
    Author     : alvar
--%>

<%@page import="Enumerado.Constantes"%>
<%@page import="Logica.LoPersona"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Enumerado.NombreSesiones"%>
<%

    Utilidades utilidad = Utilidades.GetInstancia();
    String urlSistema = utilidad.GetUrlSistema();

    String tkn = request.getParameter("tkn");
    String usr = request.getParameter("usr");
    String usuario = Seguridad.GetInstancia().decrypt(usr, Constantes.ENCRYPT_VECTOR_INICIO.getValor(), Constantes.ENCRYPT_SEMILLA.getValor());
    String referrer = request.getHeader("referer");
    String js_redirect = "window.location.replace('" + referrer + "');";
    
    if(tkn != null && usr != null)
    {
        Retorno_MsgObj retorno = LoPersona.GetInstancia().ValCambiarPasswordExterno(usr, tkn);
        if(retorno.SurgioError())
        {
            response.sendRedirect(urlSistema);
        }
    }
    else
    {
        response.sendRedirect(urlSistema);
    }
    
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Cambiar contraseña</title>
        <jsp:include page="/masterPage/head.jsp"/>
    </head>
    <body>
        <jsp:include page="/masterPage/NotificacionError.jsp"/>
        <div class="wrapper">

            <div id="contenido" name="contenido" class="">


                <div class="contenedor-principal">
                    <div class="col-sm-11 contenedor-texto-titulo-flotante">
                        <div class="contenedor-titulo">    
                            <p>Cambiar contraseña</p>
                        </div>
                        <div style="height: 30px;"></div>
                        <form>
                            <input type="hidden" name="usuario" id="usuario" value="<%=usuario%>">
                            <div>Contraseña actual: <input type="password" required="true" name="pswActual" id="pswActual"></div>
                            <div>Contraseña nueva: <input type="password" required="true" name="pswNueva" id="pswNueva"></div>
                            <div>Confirme contraseña: <input type="password" required="true" name="pswConfirmacion" id="pswConfirmacion"></div>
                            <input name="btn_guardar" id="btn_guardar" value="Guardar" class="btn btn-success" type="button" />
                            <input value="Cancelar" class="btn btn-default" type="button" onclick="<%=js_redirect%>" />
                        </form>
                        
                    </div>
                </div>
            </div>
        </div>
        <script>
            $(document).ready(function () {
                $('#btn_guardar').click(function (event) {
                    var usuario = $('#usuario').val();
                    var pswActual = $('#pswActual').val();
                    var pswNueva = $('#pswNueva').val();
                    var pswConfirmacion = $('#pswConfirmacion').val();
                    
                    
                    // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                    $.post('<% out.print(urlSistema); %>ABM_Persona', {
                        usuario: usuario,
                        pswActual: pswActual,
                        pswNueva: pswNueva,
                        pswConfirmacion: pswConfirmacion,
                        pAction: "CAMBIAR_PSW"
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

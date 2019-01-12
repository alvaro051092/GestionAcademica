<%-- 
    Document   : pswRecovery
    Created on : 28-ago-2017, 15:02:54
    Author     : alvar
--%>

<%@page import="Enumerado.Constantes"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Enumerado.NombreSesiones"%>
<%

    Utilidades utilidad = Utilidades.GetInstancia();
    String urlSistema = (String) session.getAttribute(NombreSesiones.URL_SISTEMA.getValor());

    String js_redirect = "window.location.replace('" + urlSistema + "');";
    
    String token = request.getParameter("tkn");

    String tknDecr = null;
    String perCod = null;
    String tkn = null;
    
    if(token != null)
    {
    
        tknDecr = Seguridad.GetInstancia().decrypt(token, 
                Constantes.ENCRYPT_VECTOR_INICIO.getValor(), 
                Constantes.ENCRYPT_SEMILLA.getValor());

        perCod = tknDecr.substring(0, tknDecr.indexOf(Constantes.SEPARADOR.getValor()));

        tkn = tknDecr.substring(tknDecr.indexOf(Constantes.SEPARADOR.getValor()), tknDecr.length());
        tkn = tkn.replace(Constantes.SEPARADOR.getValor(), "");
    
    }

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
                            <div>
                                <input type="hidden" required="true" name="PerCod" id="PerCod" value='<%=perCod%>'>
                                <input type="hidden" required="true" name="tkn" id="tkn" value='<%=tkn%>'>
                            </div>
                            <div>Contraseña nueva: <input type="password" required="true" name="pswNueva" id="pswNueva"></div>
                            <div>Confirme contraseña: <input type="password" required="true" name="pswConfirmacion" id="pswConfirmacion"></div>
                            <input name="btn_guardar" id="btn_guardar" value="Guardar" class="btn btn-success" type="button" />
                        </form>
                        
                    </div>
                </div>
            </div>
        </div>
        <script>
            $(document).ready(function () {
                
                var PerCod = $('#PerCod').val();
                if(PerCod == null)
                {
                    MostrarMensaje("ERROR", "Token incorrecto");
                }
                
                $('#btn_guardar').click(function (event) {
                    var PerCod = $('#PerCod').val();
                    var tkn = $('#tkn').val();
                    var pswNueva = $('#pswNueva').val();
                    var pswConfirmacion = $('#pswConfirmacion').val();
                    
                    // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                    $.post('<% out.print(urlSistema); %>ABM_Persona', {
                        PerCod: PerCod,
                        tkn: tkn,
                        pswNueva: pswNueva,
                        pswConfirmacion: pswConfirmacion,
                        pAction: "PSW_RECOVERY"
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